package com.demo.dp;

import com.demo.dp.model.*;
import com.demo.dp.model.Package;

import java.util.*;

public class Util {
    public static Network updateNetWorkByRoute(final Map<Integer, EnterNode> solution, final Network oldNetwork, boolean isTrueProcess) {
        //根据路由更新网络，注意这个方法会被SA算法用，oldNetwork不能被改变，需要生成新的网络，需要深拷贝
        Network newNetWork = isTrueProcess ? oldNetwork : deepCopyNetWork(oldNetwork);

        {
            //TO DO......
        }

        //如果是一次真实的调度,则需要重新排序每个结点中等待的数据包的顺序,按照计划出发时间重新排序
        if (isTrueProcess) {
            orderAwaitPacks(newNetWork);
        }


        return newNetWork;
    }

    private static Network deepCopyNetWork(Network oldNetwork) {
        return new Network(oldNetwork.getTicks() + 1);
    }

    public static void orderAwaitPacks(Network network) {
        //TO DO......
        //初始化网络或者每次调度完调用此函数

    }


    /*
    由于SA算法是计算最小值的
     */
    public static double calcProfit(final Map<Integer, EnterNode> solution, final Network oldNetwork,boolean isMin) {
        //TO DO......
        //计算本次调度的收益
        long profit = 0;
        Network newNetwork = updateNetWorkByRoute(solution, oldNetwork,false);

        Node[] oldNodes = oldNetwork.getNodes();
        Node[] newNodes = newNetwork.getNodes();
        for (int nodeIndex = 0; nodeIndex < newNodes.length; nodeIndex++) {
            Node oldNode = oldNodes[nodeIndex];
            Node newNode = newNodes[nodeIndex];
            //1.计算丢包的收益
            profit = profit + calcLossProfit(oldNode, newNode,newNodes.length);
            //2.计算送出的包的收益
            profit = profit + calcSendProfit(oldNode, newNode);
            //3.计算延迟的收益
            profit = profit + calcDelayProfit(newNode);

        }
        profit = profit + calcLoadBalance(newNetwork);
        return isMin?-1*profit:profit;
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
            // 1.当前的延迟
            // 2.如果队列前面有阻挡,则延迟取最大值
        }
        return 0;
    }

    private static long calcSendProfit(Node oldNode, Node newNode) {
        int sendHigh = newNode.getFinishedPriorityPackages()[PkgPriority.HIGH.value()].length-
                oldNode.getFinishedPriorityPackages()[PkgPriority.HIGH.value()].length;
        int sendMiddle = newNode.getFinishedPriorityPackages()[PkgPriority.MIDDLE.value()].length-
                oldNode.getFinishedPriorityPackages()[PkgPriority.MIDDLE.value()].length;
        int sendLow = newNode.getFinishedPriorityPackages()[PkgPriority.LOW.value()].length-
                oldNode.getFinishedPriorityPackages()[PkgPriority.LOW.value()].length;
        return sendHigh*Consts.SEND_HIGH_WEIGHT+sendMiddle*Consts.SEND_MIDDLE_WEIGHT+sendLow*Consts.SEND_LOW_WEIGHT;
    }

    private static long calcLossProfit(Node oldNode, Node newNode,int nodeNum) {
        if (isReachLossDeadLine(newNode,nodeNum)) {
            return Consts.NOT_ACCEPT;

        }
        int lossHigh = newNode.getHistoryLossPriorityQueue()[PkgPriority.HIGH.value()].length -
                oldNode.getHistoryLossPriorityQueue()[PkgPriority.HIGH.value()].length;
        int lossMiddle = newNode.getHistoryLossPriorityQueue()[PkgPriority.MIDDLE.value()].length -
                oldNode.getHistoryLossPriorityQueue()[PkgPriority.MIDDLE.value()].length;
        int lossLow = newNode.getHistoryLossPriorityQueue()[PkgPriority.LOW.value()].length -
                oldNode.getHistoryLossPriorityQueue()[PkgPriority.LOW.value()].length;
        return lossHigh*Consts.LOSS_HIGH_WEIGHT + lossMiddle*Consts.LOSS_MIDDLE_WEIGHT+lossLow*Consts.LOSS_LOW_WEIGHT;

    }

    private static boolean isReachLossDeadLine(Node newNode, int nodeNum) {
        //TO DO....
        {
            //判断丢包率达到阈值
        }
        return false;
    }


    public static Set<Integer> generateRandomList(int num, int min, int max) {
        //TO DO....
        //生成[min,max]内的num个随机数
        return null;
    }

    public static Map<Integer, EnterNode> deepCopySolution(Map<Integer, EnterNode> oldSolution) {
        //TO DO....
        Map<Integer, EnterNode> newSolution = new HashMap<>();
        return newSolution;
    }

    public static EnterNode getNextRandNode(EnterNode oldNode, List<EnterNode> allReachbleNodes) {
        //TO DO....
        //随机找到一个与当前解不一样的解
        return null;
    }


    /*
    非丢包流程,取出当前结点中需要调度的数据包
     */
    public static List<Package> takeOutSendPacks(Node netWorkNode, Network oldNetwork) {
        //参考文档8.4规则
        //TO DO...
        int nexTick = oldNetwork.getTicks() + 1;
        Package[][] priorityQueue = netWorkNode.getPriorityQueue();
        int bandWidth = netWorkNode.getBandWidth();
        List<List<Package>> sendMaxPriorityQueue = new ArrayList<List<Package>>(3);
        for (int i = 0; i < 3; i++) {
            Package[] curQue = priorityQueue[i];
            int sendNum = 0;
            for (Package curPack : curQue) {
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
        for (Node netWorkNode : oldNetwork.getNodes()) {
            int nextNodeId = netWorkNode.getId();
            if (nextNodeId == curNodeId) {
                continue;
            }
            if (isBroken(curNodeId, nextNodeId, nextTick, nextDelays)) {
                continue;
            }
            {
                //TO DO....
                //再想一些约束规则
            }
            EnterNode enterNode = new EnterNode(netWorkNode, nextTick, nextDelays[curNodeId][nextNodeId],QuePriority.UNKNOWN);
            nextNodes.add(enterNode);
        }
        if (nextNodes.size() == 0) {
            System.out.println("all route is broken");
            nextNodes.add(new EnterNode(Consts.INVALIDNODE, Consts.INVALID, Consts.INVALID,QuePriority.UNKNOWN));

        }
        return nextNodes;
    }

    public static boolean isBroken(int curNodeId, int nextNodeId, int nextTick, int[][] nextDelays) {

        return nextDelays[curNodeId][nextNodeId] == -1;


    }
}
