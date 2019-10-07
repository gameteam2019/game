package com.demo.dp.model;


public class Solution {
    private NodeSolution[] initPackSolu;
    private int initBeginIndex;
    private NodeSolution[] netPackSolu;
    private int allPackNum;

    public void setInitPackSolu(NodeSolution[] initPackSolu) {
        this.initPackSolu = initPackSolu;
    }

    public void setNetPackSolu(NodeSolution[] netPackSolu) {
        this.netPackSolu = netPackSolu;
    }

    public int getAllPackNum() {
        return allPackNum;
    }

    public void setAllPackNum(int allPackNum) {
        this.allPackNum = allPackNum;
    }



    public void setInitBeginIndex(int initBeginIndex) {
        this.initBeginIndex = initBeginIndex;
    }
    public NodeSolution[] getInitPackSolu() {
        return initPackSolu;
    }

    public int getInitBeginIndex() {
        return initBeginIndex;
    }

    public NodeSolution[] getNetPackSolu() {
        return netPackSolu;
    }




}
