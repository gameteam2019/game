package com.demo.dp.model;

public class Delays {
    private int[][] delays = new int[Consts.MAX_NODES][Consts.MAX_NODES];
    private int ticks;

    public Delays(int ticks) {
        this.ticks = ticks;
    }
}
