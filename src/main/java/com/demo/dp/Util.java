package com.demo.dp;

import com.demo.dp.model.*;
import com.demo.dp.model.Package;
import sun.nio.ch.Net;

import java.util.*;

public class Util {
    public static Network updateNetWorkByRoute(final Solution solution, final Network oldNetwork, boolean isTrueProcess) {
        //根据路由更新网络，注意这个方法会被SA算法用，oldNetwork不能被改变，需要生成新的网络，需要深拷贝
        Network newNetWork = isTrueProcess ? oldNetwork : deepCopyNetWork(oldNetwork);

        {

            sendPackToOpticalFiber(newNetWork, solution.getNetPackSolu());
            sendPackToNode(newNetWork);
            sendInitPackToNode(newNetWork, solution.getInitPackSolu());

        }

        //如果是一次真实的调度,则需要重新排序每个结点中等待的数据包的顺序,按照计划出发时间重新排序
        if (isTrueProcess) {
            orderAwaitPacks(newNetWork);
        }


        return newNetWork;
    }

    private static void sendInitPackToNode(Network newNetWork, NodeSolution[] initPackSolu) {
        for (NodeSolution nodeSolution : initPackSolu) {
            Node node = nodeSolution.getNode();
            int bandWidth = node.getBandWidth();
            Queue<Package>[] temp = new Queue[3];
            temp[0] = new LinkedList<>();
            temp[1] = new LinkedList<>();
            temp[2] = new LinkedList<>();
            for (PackSolution packSolution : nodeSolution.getPackSolutions()) {
                Package pack = packSolution.getPack();
                temp[pack.getPriority().value()].offer(pack);
            }
            sendToQueFromTemp(newNetWork, node, bandWidth, temp);
        }


    }

    private static void sendPackToNode(Network newNetWork) {

        for (int nodeIdx = 0; nodeIdx < newNetWork.getNodes().length; nodeIdx++) {
            Node node = newNetWork.getNodes()[nodeIdx];
            int bandWidth = node.getBandWidth();
            Queue<Package>[] temp = new Queue[3];
            temp[0] = new LinkedList<>();
            temp[1] = new LinkedList<>();
            temp[2] = new LinkedList<>();
            for (Package pack : node.getOpticalFiber()) {
                temp[pack.getPriority().value()].offer(pack);
            }
            sendToQueFromTemp(newNetWork, node, bandWidth, temp);
        }

    }

    private static void sendToQueFromTemp(Network newNetWork, Node node, int bandWidth, Queue<Package>[] temp) {
        for (int i = 0; i < 3; i++) {
            Queue<Package> needSend = temp[i];
            Queue<Package> curQue = node.getPriorityQueue()[i];
            int canAccept = bandWidth - curQue.size();
            if (canAccept >= needSend.size()) {
                curQue.addAll(needSend);
            } else {
                intoQue(needSend, curQue, canAccept);
            }
        }
        putPackIntoLower(node, bandWidth, temp);
        processLoss(temp, newNetWork);
    }

    private static void processLoss(Queue<Package>[] packages, Network network) {
        for (Queue<Package> queue : packages) {
            while (!queue.isEmpty()) {
                network.setLossIndex(network.getLossIndex() + 1);
                Package pack = queue.element();
                pack.setStartTick(network.getLossIndex());
                network.getNodes()[pack.getStartNodeIndex()].getLossQueue().offer(pack);
            }

        }
    }

    private static void putPackIntoLower(Node node, int bandWidth, Queue<Package>[] temp) {
        Queue<Queue<Package>> highAcceptQues = new LinkedList<Queue<Package>>();
        highAcceptQues.offer(node.getPriorityQueue()[QuePriority.MIDDLE.value()]);
        highAcceptQues.offer(node.getPriorityQueue()[QuePriority.LOW.value()]);
        sendToNode(highAcceptQues, temp[QuePriority.HIGH.value()], bandWidth);
        Queue<Queue<Package>> middleAcceptQues = new LinkedList<Queue<Package>>();
        middleAcceptQues.offer(node.getPriorityQueue()[QuePriority.LOW.value()]);
        sendToNode(middleAcceptQues, temp[QuePriority.MIDDLE.value()], bandWidth);
    }

    private static void sendToNode(Queue<Queue<Package>> acceptQues, Queue<Package> needSend, int bandWidth) {
        int srcBegin = 0;
        int srcEnd = needSend.size();

        while (!acceptQues.isEmpty() && srcBegin < srcEnd) {
            Queue<Package> dest = acceptQues.element();
            srcBegin = srcBegin + intoQue(needSend, dest, bandWidth - dest.size());
        }
    }

    private static int intoQue(Queue<Package> src, Queue<Package> dest, int destCapacity) {
        int needSend = src.size();
        int canSend = needSend <= destCapacity ? needSend : destCapacity;
        for (int index = 0; index < canSend; index++) {
            dest.offer(src.element());
        }
        return canSend;
    }

    private static void sendPackToOpticalFiber(Network newNetWork, NodeSolution[] netPackSolu) {
        for (int nodeIdx = 0; nodeIdx < newNetWork.getNodes().length; nodeIdx++) {
            NodeSolution curNodeSol = netPackSolu[nodeIdx];
            for (PackSolution packSolution : curNodeSol.getPackSolutions()) {
                EnterNode bestNode = packSolution.getBestNode();
                Package pack = packSolution.getPack();
                pack.getRoute().getPathNodes().offer(bestNode);
                if (bestNode == Consts.ENTEREND) {
                    newNetWork.getNodes()[bestNode.getIndex()]
                            .getFinishedPriorityPackages()[pack.getPriority().value()].offer(pack);

                } else {
                    newNetWork.getNodes()[bestNode.getIndex()].getOpticalFiber().offer(pack);
                }


            }
        }


    }

    private static Network deepCopyNetWork(Network oldNetwork) {
        return new Network(oldNetwork.getTicks() + 1, oldNetwork.getPackCnt());
    }

    public static void orderAwaitPacks(Network network) {
        //TO DO......
        //初始化网络或者每次调度完调用此函数

    }


    /*
    由于SA算法是计算最小值的
     */
    public static double calcProfit(final Solution solution, final Network oldNetwork, boolean isMin) {
        //TO DO......
        //计算本次调度的收益
        long profit = 0;
        Network newNetwork = updateNetWorkByRoute(solution, oldNetwork, false);

        Node[] oldNodes = oldNetwork.getNodes();
        Node[] newNodes = newNetwork.getNodes();
        for (int nodeIndex = 0; nodeIndex < newNodes.length; nodeIndex++) {
            Node oldNode = oldNodes[nodeIndex];
            Node newNode = newNodes[nodeIndex];
            //1.计算丢包的收益
            profit = profit + calcLossProfit(oldNode, newNode, newNetwork);
            //2.计算下次送出的包的收益
            profit = profit + calcSendNextProfit(oldNode, newNode);
            //3.计算延迟的收益
            profit = profit + calcDelayProfit(newNode);

        }
        profit = profit + calcLoadBalance(newNetwork);
        return isMin ? -1 * profit : profit;
    }

    private static long calcLoadBalance(Network newNetwork) {
        //TO DO...
        {
            //求网络结点中所有结点上包个数的方差,方差越小收益越大

        }
        return 0;
    }

    private static long calcDelayProfit(Node node) {
        //TO Do...
        {

            //可以简单判断下：
            // 1.每个包的延迟
            // 2.如果队列前面有阻挡,则延迟取最大值
        }
        return 0;
    }

    private static long calcSendNextProfit(Node oldNode, Node newNode) {
        //TO Do...
        {
            //经过调度后,下个时间段能立马出去的
        }
        return 0;
    }

    private static long calcLossProfit(Node oldNode, Node newNode, Network network) {
        if (network.getLossIndex() / network.getPackCnt() >= Consts.LOWSENDRADIO) {
            return Consts.NOT_ACCEPT;
        }
        int lossHigh = newNode.getHistoryLossPriorityQueue()[PkgPriority.HIGH.value()].size() -
                oldNode.getHistoryLossPriorityQueue()[PkgPriority.HIGH.value()].size();
        int lossMiddle = newNode.getHistoryLossPriorityQueue()[PkgPriority.MIDDLE.value()].size() -
                oldNode.getHistoryLossPriorityQueue()[PkgPriority.MIDDLE.value()].size();
        int lossLow = newNode.getHistoryLossPriorityQueue()[PkgPriority.LOW.value()].size() -
                oldNode.getHistoryLossPriorityQueue()[PkgPriority.LOW.value()].size();
        return lossHigh * Consts.LOSS_HIGH_WEIGHT + lossMiddle * Consts.LOSS_MIDDLE_WEIGHT + lossLow * Consts.LOSS_LOW_WEIGHT;

    }


    public static Set<Integer> generateRandomList(int num, int min, int max) {
        //TO DO....
        //生成[min,max]内的num个随机数
        return null;
    }

    public static Solution deepCopySolution(Solution oldSolution) {
        //TO DO....
        Solution newSolution = new Solution();
        return newSolution;
    }

    public static EnterNode getNextRandNode(EnterNode oldNode, List<EnterNode> allReachbleNodes) {
        //TO DO....
        //随机找到一个与当前解不一样的解
        return null;
    }


    /*
    非丢包流程,取出当前结点中需要调度的数据包,按照优先级大小有序排列
     */
    public static List<Package> takeOutSendPacks(Node netWorkNode, Network oldNetwork) {
        //参考文档8.4规则
        //TO DO...
        int nexTick = oldNetwork.getTicks() + 1;
        Queue<Package>[] priorityQueue = netWorkNode.getPriorityQueue();
        int bandWidth = netWorkNode.getBandWidth();
        List<List<Package>> sendMaxPriorityQueue = new ArrayList<List<Package>>(3);
        for (int i = 0; i < 3; i++) {
            Queue<Package> curQue = priorityQueue[i];
            int sendNum = 0;
            while (!curQue.isEmpty()) {
                Package curPack = curQue.element();
                EnterNode lastPathNode = curPack.getRoute().getPathNodes().peek();
                int delay = lastPathNode.getDelay();
                int enterTick = lastPathNode.getEnterTick();
                if (nexTick - enterTick < delay) {
                    break;
                }
                if (sendNum > bandWidth) {
                    break;
                }
                sendNum++;
                sendMaxPriorityQueue.get(i).add(curPack);
            }
        }
        List<Package> sendPackages = enterSendQueByRule(sendMaxPriorityQueue, bandWidth);

        return sendPackages;
    }

    private static List<Package> enterSendQueByRule(List<List<Package>> sendMaxPriorityQueue, int bandWidth) {
        SendQueInfo highInfo = new SendQueInfo((int) (bandWidth * Consts.HIGHSENDRADIO),
                sendMaxPriorityQueue.get(PkgPriority.HIGH.value()).size());
        SendQueInfo middleInfo = new SendQueInfo((int) (bandWidth * Consts.MIDDLESENDRADIO),
                sendMaxPriorityQueue.get(PkgPriority.MIDDLE.value()).size());
        SendQueInfo lowInfo = new SendQueInfo((int) (bandWidth * Consts.LOWSENDRADIO),
                sendMaxPriorityQueue.get(PkgPriority.LOW.value()).size());
        int highSendNum = getSendNum(highInfo, middleInfo, lowInfo);
        int middleSendNum = getSendNum(middleInfo, highInfo, lowInfo);
        int lowSendNum = getSendNum(lowInfo, highInfo, middleInfo);
        List<Package> result = new ArrayList<Package>();
        for (int quePri = 0; quePri < sendMaxPriorityQueue.size(); quePri++) {
            List<Package> curQue = sendMaxPriorityQueue.get(quePri);
            int packNum = (quePri == QuePriority.HIGH.value()) ? highSendNum :
                    (quePri == QuePriority.MIDDLE.value()) ? middleSendNum : lowSendNum;
            for (int i = 0; i < packNum; i++) {
                result.add(curQue.get(i));
            }
        }


        return result;
    }

    private static int getSendNum(SendQueInfo cur, SendQueInfo higher, SendQueInfo lower) {
        int sendNum = cur.getSendLimit();
        if (cur.getCanSendMax() <= cur.getSendLimit()) {
            return sendNum;
        }

        cur.setMoreCapacity(0);
        int diff = cur.getCanSendMax() - sendNum;
        if (diff <= higher.getMoreCapacity()) {
            higher.setMoreCapacity(higher.getMoreCapacity() - diff);
            return sendNum;
        }
        sendNum = sendNum + higher.getMoreCapacity();
        higher.setMoreCapacity(0);
        diff = cur.getCanSendMax() - sendNum;
        if (diff <= lower.getMoreCapacity()) {
            lower.setMoreCapacity(lower.getMoreCapacity() - diff);
            return sendNum;
        }
        sendNum = sendNum + lower.getMoreCapacity();
        lower.setMoreCapacity(0);
        return sendNum;

    }

    private static List<Package> enterSendQueByRule(List<List<Package>> sendPriorityQueue) {
        return null;
    }

    public static List<EnterNode> generateValidNextNodes(Package pack, Network oldNetwork, int[][] nextDelays) {
        //路径合法性参考文档8.8,此外需要想新的约束规则缩小
        List<EnterNode> nextNodes = new ArrayList<EnterNode>();
        int curNodeId = pack.getRoute().getPathNodes().peek().getId();
        int nextTick = oldNetwork.getTicks() + 1;
        if (checkEnd(pack, nextTick)) {
            nextNodes.add(Consts.ENTEREND);
            return nextNodes;
        }
        for (Node netWorkNode : oldNetwork.getNodes()) {
            int nextNodeId = netWorkNode.getId();
            if (nextNodeId == curNodeId) {
                continue;
            }
            if (isBroken(curNodeId, nextNodeId, nextTick, nextDelays)) {
                continue;
            }
            EnterNode enterNode = new EnterNode(netWorkNode, nextTick, nextDelays[curNodeId][nextNodeId], QuePriority.UNKNOWN);
            nextNodes.add(enterNode);
        }
        if (nextNodes.size() == 0) {
            System.out.println("all route is broken");
            nextNodes.add(new EnterNode(Consts.WAITNODE, Consts.INVALID, Consts.INVALID, QuePriority.UNKNOWN));

        }
        return nextNodes;
    }

    private static boolean checkEnd(Package pack, int nextTick) {
        EnterNode lastNode = pack.getRoute().getPathNodes().peek();
        return lastNode.getDelay() >= nextTick - lastNode.getEnterTick();
    }

    public static boolean isBroken(int curNodeId, int nextNodeId, int nextTick, int[][] nextDelays) {

        return nextDelays[curNodeId][nextNodeId] == -1;


    }
}
