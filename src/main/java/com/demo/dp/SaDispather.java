package com.demo.dp;

import com.demo.dp.model.Network;
import com.demo.dp.model.Node;

import java.util.*;

public class SaDispather {
    private final int BEGIN_T = 3000;//退火起始温度
    private final double END_T = Math.exp(-8);//退火终止温度
    private final double DERATE = 0.98;//温度衰减率
    private final int RANDOMLIMIT = 1000;//从1个非优解开始连续接收非优的上限
    private final int INNERLOOP = 100;//一个温度下的迭代次数
    private final int OUTLOOP = 20;//温度的迭代次数
    private final double  UPDATERATIO =0.2;



    public  Map<Integer,Node> dispatchNetwork(Network network) throws Exception {
        //TO DO.....
        //1.初始化解空间
        Map<Integer,List<Node>> solutionSpace = initSolutionSpace(network);
        //2.生成初解
        Map<Integer,Node> oldSolution = generateFirstSolution(solutionSpace, network);
        double oldProfit = Util.calcProfit(oldSolution, network);
        //3.
        double t = BEGIN_T;
        int randomNum = 0;
        int outLoop =0;
        Map<Integer,Node> bestRoute = oldSolution;
        double bestProfit = oldProfit;
        while (t > END_T && outLoop<OUTLOOP){
            for (int iloop = 1; iloop < INNERLOOP; iloop++) {
                Map<Integer,Node> nextSolution = generateNextSolution(oldSolution, solutionSpace, network);
                double nextProfit = Util.calcProfit(nextSolution, network);
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
                t = t*DERATE;
            }

        }
        return bestRoute;
    }

    private Map<Integer,Node> generateNextSolution(Map<Integer,Node> oldSolution, Map<Integer,List<Node>> solutionSpace, Network network) {
        //TO Do......
        //随机选取旧解中的一部分(按照比例)
        int updateNum = (int) (oldSolution.size()*UPDATERATIO);
        updateNum = updateNum<1?1:updateNum;
        Set<Integer> updateIndexs = Util.generateRandomList(updateNum, 0, oldSolution.size() - 1);
        Map<Integer,Node> newSolution = Util.deepCopySolution(oldSolution);
        int cnt = 0;
        for (Map.Entry<Integer, Node> packNode : newSolution.entrySet()) {
            if (updateIndexs.contains(cnt)) {
                int pid = packNode.getKey();
                Node randNode = Util.getNextRandNode(packNode.getValue(),solutionSpace.get(pid));
                packNode.setValue(randNode);
            }

        }

        return newSolution;
    }


    private Map<Integer,Node> generateFirstSolution(Map<Integer,List<Node>> solutionSpace, Network network) throws Exception{

        //TO Do......
        //可以先随机生成,但是看到网上有人说可以优化初解，比如用爬山先生成一个局部优解最为起点
        if (solutionSpace == null || solutionSpace.size() == 0) {
            System.out.println("solutionSpace is Invalid");
            return null;
        }

        Map<Integer,Node> result = new HashMap<Integer, Node>(solutionSpace.size());
        for (Map.Entry<Integer, List<Node>> pack : solutionSpace.entrySet()) {
            int pid = pack.getKey();
            List<Node> allReachableNodes = solutionSpace.get(pid);
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

    private Map<Integer,List<Node>> initSolutionSpace(final Network network) {
        //To Do......

        return null;
    }


}
