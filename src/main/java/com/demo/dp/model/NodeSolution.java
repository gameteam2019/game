package com.demo.dp.model;

import java.util.List;

public class NodeSolution {
    private Node node;
    private List<PackSolution> packSolutions;
    private int beginIndex;
    private int endIndex;

    public NodeSolution(Node node) {
        this.node = node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setPackSolutions(List<PackSolution> packSolutions) {
        this.packSolutions = packSolutions;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }



    public Node getNode() {
        return node;
    }

    public List<PackSolution> getPackSolutions() {
        return packSolutions;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }




}
