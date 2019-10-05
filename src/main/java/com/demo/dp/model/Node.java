package com.demo.dp.model;

import java.util.List;

public class Node {

    private int bandWidth;
    private int capacity;
    private Package[][] priorityQueue;
    private List<Package> finishedPackages;
    private Package[][] awaitQueue;


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
