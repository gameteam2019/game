package com.demo.dp;

import com.demo.dp.model.Network;
import com.demo.dp.model.Node;

import java.util.List;
import java.util.Map;

public class RouteServer {

    public  List<String[]> calculate(List<String[]> packages, List<String[]> nodes, List<String[]> delays) {
        //1.初始化网络状态T=0
        Network network = initNetWork(packages, nodes, delays);
        //2.调度过程
        while (!checkFinish(network)) {
            network = dispatchNetwork(network);
        }
        generateAnswer(network,packages);

        return  packages;
    }

    private void generateAnswer(Network network, List<String[]> packages) {
        //TO DO.....

        //读取最后状态的network,遍历network中的Nodes上的finishedPackages(注意:package调度完成后,存储在最后目标Node上),从package中读取route信息回填到answer中
    }

    private Network dispatchNetwork(Network network) {
        //TO DO.....
        //利用SA算法得到本次tick的最优解
        try {
            SaDispather saDispather = new SaDispather();
            Map<Integer, Node> bestRoute = saDispather.dispatchNetwork(network);
            return new Network(network.getTicks() + 1);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean checkFinish(Network network) {
        //TO DO.....
        //遍历network中的Nodes上的awaitQueue,如果都为空则调度结束


        return false;

    }

    private Network initNetWork(List<String[]> packages, List<String[]> nodes, List<String[]> delays) {
        //TO DO.....

        return new Network(0);
    }
}
