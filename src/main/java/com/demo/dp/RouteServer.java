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
            int[][] nextDelays = getNextDelays(delays,network.getTicks(),network.getTicks()+1);
            network = dispatchNetwork(network,nextDelays);
        }
        generateAnswer(network,packages);

        return  packages;
    }

    private int[][] getNextDelays(List<String[]> delays, int curTick,int nextTick) {
        //TO DO......
        {
            //可以对delays信息做排序等处理后存储，因为delays太大了,比如分段存储,二分法查找等,再比如历史的时延信息就可以丢弃,缩小空间


        }

        return null;
    }

    private void generateAnswer(Network network, List<String[]> packages) {
        //TO DO.....

        //读取最后状态的network,遍历network中的Nodes上的finishedPackages(注意:package调度完成后,存储在最后目标Node上),从package中读取route信息回填到answer中
    }

    private Network dispatchNetwork(Network network,int[][] nextDelays) {
        //TO DO.....
        //利用SA算法得到本次tick的最优解
        try {

            if (network.isBeginProcessLoss()) {
                return dispatchLossPacks(network);
            }

            SaDispather saDispather = new SaDispather();

            Map<Integer, Node> bestRoute = saDispather.dispatchNetwork(network,nextDelays);
            return new Network(network.getTicks() + 1);
        } catch (Exception e) {
            return null;
        }
    }

    /*
    1.当且仅当网络中所有的包都处理完，才开始处理丢包，即：一旦开始处理丢包，则网络中所有的包都是丢包,
    2.丢包的实际出发时间由系统规则确定(详见8.9),不能延迟
    3.丢包需要重复原有的路径，当原有的路径走到头还没有达到目的地，则开始用算法调度
     */
    private Network dispatchLossPacks(Network network) {
        //TO DO.....
        return new Network(network.getTicks()+1);
    }

    private boolean checkFinish(Network network) {
        //TO DO.....
        //遍历network中的Nodes上的awaitQueue,如果都为空则调度结束


        return false;

    }

    private Network initNetWork(List<String[]> packages, List<String[]> nodes, List<String[]> delays) {
        //TO DO.....
        {

            //数据中的nodeId 是乱序的,而netWork中的nodes对象是按照数组存储的,需要把对应关系映射出来
            //delay是不是需要提前生成
        }

        return new Network(0);
    }
}
