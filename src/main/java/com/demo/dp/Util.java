package com.demo.dp;

import com.demo.dp.model.Delays;
import com.demo.dp.model.Network;
import com.demo.dp.model.Node;
import com.demo.dp.model.Package;

import java.util.*;

public class Util {
    public static Network updateNetWorkByRoute(final List<Node> route,final Network oldNetwork,boolean isTrueProcess) {
        //根据路由更新网络，注意这个方法会被SA算法用，oldNetwork不能被改变，需要生成新的网络，需要深拷贝
        Network newNetWork = isTrueProcess?oldNetwork:deepCopyNetWork(oldNetwork);

        {
            //TO DO......
        }

        //如果是一次真实的调度,则需要重新排序每个结点中等待的数据包的顺序,按照计划出发时间重新排序
        if (isTrueProcess) {
            orderAwaitPacks(newNetWork);
        }


        return  newNetWork;
    }

    private static Network deepCopyNetWork(Network oldNetwork) {
        return new Network(oldNetwork.getTicks() + 1);
    }

    public  static void orderAwaitPacks(Network network) {
        //TO DO......
        //初始化网络或者每次调度完调用此函数

    }

    public static double calcProfit(final Map<Integer,Node> solution, final Network network) {
        //TO DO......
        //计算本次调度的收益
        return 0;
    }

    public static Set<Integer> generateRandomList(int num, int min, int max) {
        //TO DO....
        //生成[min,max]内的num个随机数
        return null;
    }

    public static Map<Integer, Node> deepCopySolution(Map<Integer, Node> oldSolution) {
        //TO DO....
        Map<Integer, Node> newSolution = new HashMap<>();
        return newSolution;
    }

    public static Node getNextRandNode(Node oldNode, List<Node> allReachbleNodes) {
        //TO DO....
        //随机找到一个与当前解不一样的解
        return null;
    }

    public static List<Package> takeOutSendPacks(Package[][] priorityQueue, Network oldNetwork) {
        //参考文档8.4规则
        //TO DO...

        return null;
    }

    public static List<Node> generateValidNextNodes(Package pack, Network oldNetwork, int[][] nextDelays){
        //路径合法性参考文档8.8,此外需要想新的约束规则缩小
        List<Node> nextNodes = new ArrayList<Node>();
        int curNodeId = pack.getRoute().getPathNodes().peek();
        int nextTick = oldNetwork.getTicks()+1;
        for (Node netWorkNode : oldNetwork.getNodes()) {
            int nextNodeId = netWorkNode.getId();
            if ( nextNodeId == curNodeId) {
                continue;
            }
            if (isBroken(curNodeId, nextNodeId, nextTick, nextDelays)) {
                continue;
            }
            {
                //TO DO....
                //再想一些约束规则
            }
            nextNodes.add(netWorkNode);
        }
        if (nextNodes.size() == 0) {
            System.out.println("all route is broken");
            nextNodes.add(oldNetwork.getNodes()[pack.getTargetNodeIndex()]);

        }
        return nextNodes;
    }

    public static boolean isBroken(int curNodeId, int nextNodeId, int nextTick, int[][] nextDelays) {

        return nextDelays[curNodeId][nextNodeId] == -1;


    }
}
