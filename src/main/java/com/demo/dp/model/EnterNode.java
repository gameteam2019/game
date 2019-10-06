package com.demo.dp.model;
/*
记录每个数据包进入结点的时刻,需要等待的时延
 */
public class EnterNode extends Node{
    private int enterTick;
    private int delay;
    private QuePriority enterQuePriority;
    public EnterNode(Node node,int enterTick,int delay,QuePriority enterQuePriority){
        super(node);
        this.enterTick = enterTick;
        this.delay = delay;
        this.enterQuePriority = enterQuePriority;
    }


    public int getEnterTick() {
        return enterTick;
    }

    public int getDelay() {
        return delay;
    }



}
