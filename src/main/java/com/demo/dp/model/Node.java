package com.demo.dp.model;

public class Node {

    private int bandWidth;
    private int capacity;
    private Package[][] priorityQueue;

    public Node(int bandWidth, int capacity) {
        this.bandWidth = bandWidth;
        this.capacity = capacity;

        priorityQueue = new Package[3][capacity];
    }

    public int getBandWidth() {
        return bandWidth;
    }

    public int getCapacity() {
        return capacity;
    }
}
