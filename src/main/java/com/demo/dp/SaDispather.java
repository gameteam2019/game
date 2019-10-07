package com.demo.dp;

import com.demo.dp.model.*;
import com.demo.dp.model.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SaDispather {
    private final int BEGIN_T = 3000;//退火起始温度
    private final double END_T = Math.exp(-8);//退火终止温度
    private final double DERATE = 0.98;//温度衰减率
    private final int RANDOMLIMIT = 1000;//从1个非优解开始连续接收非优的上限
    private final int INNERLOOP = 100;//一个温度下的迭代次数
    private final int OUTLOOP = 20;//温度的迭代次数
    private final double UPDATERATIO = 0.2;


    public Solution dispatchNetwork(Network oldNetwork, int[][] nextDelays) throws Exception {
        //TO DO.....
        //1.初始化解空间
        Solution solutionSpace = initSolutionSpace(oldNetwork, nextDelays);
        //2.生成初解
        Solution oldSolution = generateFirstSolution(solutionSpace);
        double oldProfit = Util.calcProfit(oldSolution, oldNetwork, true);
        //3.
        double t = BEGIN_T;
        int randomNum = 0;
        int outLoop = 0;
        Solution bestRoute = oldSolution;
        double bestProfit = oldProfit;
        while (t > END_T && outLoop < OUTLOOP) {
            for (int iloop = 1; iloop < INNERLOOP; iloop++) {
               Solution nextSolution = generateNextSolution(oldSolution, solutionSpace);
                double nextProfit = Util.calcProfit(nextSolution, oldNetwork, true);
                if (nextProfit < oldProfit) {
                    oldSolution = nextSolution;
                    oldProfit = nextProfit;
                    randomNum = 0;
                    outLoop = 0;
                    if (bestProfit < nextProfit) {
                        bestRoute = nextSolution;
                        bestProfit = nextProfit;
                    }

                } else {
                    if (nextProfit == Consts.NOT_ACCEPT) {
                        continue;
                    }
                    double acceptProbability = Math.random();
                    double acceptLimit = Math.exp(-1 * (oldProfit - nextProfit) / t);
                    if (acceptProbability > acceptLimit) {
                        oldProfit = nextProfit;
                        oldSolution = nextSolution;
                        randomNum++;
                    }
                }
                if (randomNum > RANDOMLIMIT) {
                    outLoop++;
                    break;
                }
                t = t * DERATE;
            }

        }
        return bestRoute;
    }

    private Solution generateNextSolution(Solution oldSolution, Solution solutionSpace) {
        //TO Do......
        //随机选取旧解中的一部分(按照比例)、
        int updateNum = (int) (oldSolution.getAllPackNum() * UPDATERATIO);
        updateNum = updateNum < 1 ? 1 : updateNum;
        Set<Integer> updateIndexs = Util.generateRandomList(updateNum, 0, oldSolution.getAllPackNum() - 1);
        Solution newSolution = Util.deepCopySolution(oldSolution);
        {
            //对于可以出去的只有一个解的就不要参与随机了，看下怎么实现
        }
        {


            //TO Do......

        }
        {
            //TO Do......

        }

//        int cnt = 0;
//        Iterator<Map.Entry<Integer, EnterNode>> iterator = newSolution.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Integer, EnterNode> packNextNode = iterator.next();
//            if (updateIndexs.contains(cnt)) {
//                int pid = packNextNode.getKey();
//                EnterNode randNode = Util.getNextRandNode(packNextNode.getValue(), solutionSpace.get(pid));
//                //对于初始化的数据包,有可能需要继续等待,则从解中移除
//                if (randNode.getId() == Consts.INVALID) {
//                    iterator.remove();
//                    continue;
//                }
//                packNextNode.setValue(randNode);
//            }
//        }

        return newSolution;
    }


    private Solution generateFirstSolution(Solution solutionSpace) throws Exception {

        //TO Do......
        //可以先随机生成,但是看到网上有人说可以优化初解，比如用爬山先生成一个局部优解最为起点
        if (solutionSpace == null || solutionSpace.getAllPackNum()==0) {
            System.out.println("solutionSpace is Invalid");
            return null;
        }

        //TO DO......
        {
            //需要对每个结点上的解做优先级和编号排序的处理
        }
        return null;





//        Map<Integer, EnterNode> result = new HashMap<Integer, EnterNode>(solutionSpace.size());
//        for (Map.Entry<Integer, List<EnterNode>> pack : solutionSpace.entrySet()) {
//            int pid = pack.getKey();
//            List<EnterNode> allReachableNodes = solutionSpace.get(pid);
//            if (null == allReachableNodes || allReachableNodes.size() == 0) {
//                System.out.println("solutions is Invalid:" + pid);
//                throw new Exception("solutions is Invalid:");
//            }
//            if (allReachableNodes.size() == 1) {
//                result.put(pid, allReachableNodes.get(0));
//                continue;
//            }
//            int randomNodeIndex = (int) (1 + Math.random() * (allReachableNodes.size() - 1 + 1));
//            result.put(pid, allReachableNodes.get(randomNodeIndex - 1));
//
//        }
//        return result;
    }

    private Solution initSolutionSpace(final Network oldNetwork, int[][] nextDelays) {

        NodeSolution[] netPackSolu = new NodeSolution[oldNetwork.getNodes().length];
        Solution solutionSpace = new Solution();
        int netEndIndex = initInNetPacks(solutionSpace, oldNetwork, nextDelays);
        initAwaitPacks(solutionSpace, oldNetwork, netEndIndex);

        return solutionSpace;
    }

    /*
    做单元测试
     */
    private int initInNetPacks(Solution solutionSpace, Network oldNetwork, int[][] nextDelays) {
        int netPackNum = 0;
        NodeSolution[] netNodeSolution = new NodeSolution[oldNetwork.getNodes().length];
        solutionSpace.setNetPackSolu(netNodeSolution);
        for (int nodeIdx = 0; nodeIdx < oldNetwork.getNodes().length; nodeIdx++) {
            Node node = oldNetwork.getNodes()[nodeIdx];
            NodeSolution curNodeSolu = new NodeSolution(node);
            curNodeSolu.setBeginIndex(netPackNum);
            List<Package> needSendPacks = Util.takeOutSendPacks(node, oldNetwork);
            List<PackSolution> packSolutions = new ArrayList<PackSolution>(needSendPacks.size());
            int curNodePackNum = 0;
            for (Package pack : needSendPacks) {
                netPackNum++;
                curNodePackNum++;
                List<EnterNode> enterNodes = Util.generateValidNextNodes(pack, oldNetwork, nextDelays);
                PackSolution packSolution = new PackSolution(pack, enterNodes);
                packSolutions.add(packSolution);
            }
            if (curNodePackNum == 0) {
                continue;
            } else {
                curNodeSolu.setEndIndex(netPackNum);
                curNodeSolu.setPackSolutions(packSolutions);
                netNodeSolution[nodeIdx] = curNodeSolu;
            }
        }
        return netPackNum;
    }

    /*
    做单元测试
     */
    private void initAwaitPacks(Solution solutionSpace, Network oldNetwork, int netEndIndex) {
        NodeSolution[] initPackSolu = new NodeSolution[oldNetwork.getNodes().length];
        solutionSpace.setInitPackSolu(initPackSolu);

        int initNum = 0;
        for (int nodeIdx = 0; nodeIdx < oldNetwork.getNodes().length; nodeIdx++) {
            Node node = oldNetwork.getNodes()[nodeIdx];
            List<Package> awaitPacks = node.getAwaitPackages();
            if (awaitPacks == null || awaitPacks.size() < 1) {
                continue;
            }
            int nextTick = oldNetwork.getTicks() + 1;
            List<PackSolution> packSolutions = new ArrayList<PackSolution>();
            int curNodePack = 0;
            for (Package pack : awaitPacks) {
                //需要保证awaitPacks是按照计划出发时间有序
                if (pack.getStartTick() > nextTick) {
                    break;
                }
                initNum++;
                curNodePack++;
                List<EnterNode> reachNodes = new ArrayList<EnterNode>(2);
                reachNodes.add(new EnterNode(Consts.WAITNODE, nextTick, Consts.FIRSTDELAY, QuePriority.UNKNOWN));
                Node nextNode = oldNetwork.getNodes()[pack.getStartNodeIndex()];
                reachNodes.add(new EnterNode(nextNode, nextTick, Consts.FIRSTDELAY, QuePriority.UNKNOWN));
                PackSolution packSolution = new PackSolution(pack, reachNodes);
                packSolutions.add(packSolution);
            }
            if (curNodePack == 0) {
                continue;
            }
            NodeSolution nodeSolution = new NodeSolution(node);
            initPackSolu[nodeIdx] = nodeSolution;
            nodeSolution.setBeginIndex(netEndIndex + initNum);

        }
        int initBeginIndex = initNum == 0 ? Consts.INVALID : netEndIndex + 1;
        solutionSpace.setInitBeginIndex(initBeginIndex);
        solutionSpace.setAllPackNum(initBeginIndex + initNum);
    }


}
