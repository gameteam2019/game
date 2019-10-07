package com.demo.dp.model;

import java.util.List;

public class PackSolution {
    private Package pack;
    private List<EnterNode> nextNodes;



    private EnterNode bestNode;

    public PackSolution(Package pack, List<EnterNode> nextNodes) {
        this.pack = pack;
        this.nextNodes = nextNodes;
    }
    public Package getPack() {
        return pack;
    }
    public List<EnterNode> getNextNodes() {
        return nextNodes;
    }
    public EnterNode getBestNode() {
        return bestNode;
    }

    public void setBestNode(EnterNode bestNode) {
        this.bestNode = bestNode;
    }



}
