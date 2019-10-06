package com.demo.dp;

import com.demo.dp.model.Network;
import com.demo.dp.model.Node;

import java.util.*;

public class Util {
    public static Network updateNetWorkByRoute(final List<Node> route,final Network oldNetwork) {
        //TO DO......
        //根据路由更新网络，注意这个方法会被SA算法用，oldNetwork不能被改变，需要生成新的网络，需要深拷贝
        return new Network(oldNetwork.getTicks());
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
}
