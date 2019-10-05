package com.demo.dp.model;

public class Network {

    private final Node[] nodes = new Node[Consts.MAX_NODES];
    private Delays delays;
    private int ticks;

    public Network(int ticks) {
        this.ticks = ticks;

    }
    public int getTicks() {
        return ticks;
    }
}
