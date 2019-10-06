package com.demo.dp;

import com.demo.dp.model.*;
import com.demo.dp.model.Package;

import java.util.*;

public class SaDispather {
    private final int BEGIN_T = 3000;//退火起始温度
    private final double END_T = Math.exp(-8);//退火终止温度
    private final double DERATE = 0.98;//温度衰减率
    private final int RANDOMLIMIT = 1000;//从1个非优解开始连续接收非优的上限
    private final int INNERLOOP = 100;//一个温度下的迭代次数
    private final int OUTLOOP = 20;//温度的迭代次数
    private final double UPDATERATIO = 0.2;


    public Map<Integer, EnterNode> dispatchNetwork(Network oldNetwork,int[][] nextDelays) throws Exception {
        //TO DO.....
        //1.初始化解空间
        Map<Integer, List<EnterNode>> solutionSpace = initSolutionSpace(oldNetwork, nextDelays);
        //2.生成初解
        Map<Integer, EnterNode> oldSolution = generateFirstSolution(solutionSpace);
        double oldProfit = Util.calcProfit(oldSolution, oldNetwork,true);
        //3.
        double t = BEGIN_T;
        int randomNum = 0;
        int outLoop = 0;
        Map<Integer, EnterNode> bestRoute = oldSolution;
        double bestProfit = oldProfit;
        while (t > END_T && outLoop < OUTLOOP) {
            for (int iloop = 1; iloop < INNERLOOP; iloop++) {
                Map<Integer, EnterNode> nextSolution = generateNextSolution(oldSolution, solutionSpace);
                double nextProfit = Util.calcProfit(nextSolution, oldNetwork,true);
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

    private Map<Integer, EnterNode> generateNextSolution(Map<Integer, EnterNode> oldSolution, Map<Integer, List<EnterNode>> solutionSpace) {
        //TO Do......
        //随机选取旧解中的一部分(按照比例)
        int updateNum = (int) (oldSolution.size() * UPDATERATIO);
        updateNum = updateNum < 1 ? 1 : updateNum;
        Set<Integer> updateIndexs = Util.generateRandomList(updateNum, 0, oldSolution.size() - 1);
        Map<Integer, EnterNode> newSolution = Util.deepCopySolution(oldSolution);
        int cnt = 0;
        Iterator<Map.Entry<Integer, EnterNode>> iterator = newSolution.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, EnterNode> packNextNode = iterator.next();
            if (updateIndexs.contains(cnt)) {
                int pid = packNextNode.getKey();
                EnterNode randNode = Util.getNextRandNode(packNextNode.getValue(), solutionSpace.get(pid));
                //对于初始化的数据包,有可能需要继续等待,则从解中移除
                if (randNode.getId() == Consts.INVALID) {
                    iterator.remove();
                    continue;
                }
                packNextNode.setValue(randNode);
            }
        }

        return newSolution;
    }


    private Map<Integer, EnterNode> generateFirstSolution(Map<Integer, List<EnterNode>> solutionSpace) throws Exception {

        //TO Do......
        //可以先随机生成,但是看到网上有人说可以优化初解，比如用爬山先生成一个局部优解最为起点
        if (solutionSpace == null || solutionSpace.size() == 0) {
            System.out.println("solutionSpace is Invalid");
            return null;
        }

        Map<Integer, EnterNode> result = new HashMap<Integer, EnterNode>(solutionSpace.size());
        for (Map.Entry<Integer, List<EnterNode>> pack : solutionSpace.entrySet()) {
            int pid = pack.getKey();
            List<EnterNode> allReachableNodes = solutionSpace.get(pid);
            if (null == allReachableNodes || allReachableNodes.size() == 0) {
                System.out.println("solutions is Invalid:" + pid);
                throw new Exception("solutions is Invalid:");
            }
            if (allReachableNodes.size() == 1) {
                result.put(pid, allReachableNodes.get(0));
                continue;
            }
            int randomNodeIndex = (int) (1 + Math.random() * (allReachableNodes.size() - 1 + 1));
            result.put(pid, allReachableNodes.get(randomNodeIndex - 1));

        }
        return result;
    }

    private Map<Integer, List<EnterNode>> initSolutionSpace(final Network oldNetwork,int[][] nextDelays) {
        Map<Integer, List<EnterNode>> solutionSpace = new HashMap<Integer, List<EnterNode>>();
        for (Node node : oldNetwork.getNodes()) {
            initAwaitPacks(node.getAwaitPackages(), solutionSpace,oldNetwork);
            initInNetPacks(node, solutionSpace,oldNetwork,nextDelays);
        }
        return solutionSpace;
    }

    private void initInNetPacks(Node node, Map<Integer, List<EnterNode>> solutionSpace,Network oldNetwork,int[][] nextDelays) {
        List<Package> needSendPacks = Util.takeOutSendPacks(node, oldNetwork);
        for (Package pack : needSendPacks) {
            solutionSpace.put(pack.getId(), Util.generateValidNextNodes(pack, oldNetwork,nextDelays));
        }


    }


    private void initAwaitPacks(List<Package> awaitPacks, Map<Integer, List<EnterNode>> solutionSpace,Network oldNetwork) {
        if (awaitPacks == null || awaitPacks.size() < 1) {
            return;
        }
        int nextTick = oldNetwork.getTicks()+1;
        for (Package pack : awaitPacks) {
            //需要保证awaitPacks是按照计划出发时间有序排列
            if (pack.getStartTick() > nextTick) {
                break;
            }
            List<EnterNode> reachNodes = new ArrayList<EnterNode>(2);
            reachNodes.add(new EnterNode(Consts.INVALIDNODE,nextTick,Consts.FIRSTDELAY,QuePriority.UNKNOWN));
            Node nextNode = oldNetwork.getNodes()[pack.getTargetNodeIndex()];
            reachNodes.add(new EnterNode(nextNode,nextTick,Consts.FIRSTDELAY,QuePriority.UNKNOWN));
            solutionSpace.put(pack.getId(), reachNodes);
        }


    }


}
