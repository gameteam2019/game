package com.demo.dp.model;

public class Network {
    private final Node[] nodes = new Node[Consts.MAX_NODES];
    private Delays delays;
    private int ticks;
    private boolean isBeginProcessLoss = false;
    private int lossIndex;

    private int packCnt;

    public int getLossIndex() {
        return lossIndex;
    }

    public void setLossIndex(int lossIndex) {
        this.lossIndex = lossIndex;
    }

    public int getPackCnt() {
        return packCnt;
    }

    public void setPackCnt(int packCnt) {
        this.packCnt = packCnt;
    }



    public Node[] getNodes() {
        return nodes;
    }

    public Delays getDelays() {
        return delays;
    }

    public void setDelays(Delays delays) {
        this.delays = delays;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }


    public Network(int ticks,int packCnt) {
        this.ticks = ticks;
        this.packCnt = packCnt;

    }

    public int getTicks() {
        return ticks;
    }
    public boolean isBeginProcessLoss() {
        return isBeginProcessLoss;
    }

    public void setBeginProcessLoss(boolean beginProcessLoss) {
        isBeginProcessLoss = beginProcessLoss;
    }
}
