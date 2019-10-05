package com.demo.dp.model;

public class Node {

    private Package[] highPriorityPackages = new Package[Consts.MAX_CAPACITY_OF_QUEUE];
    private Package[] middlePriorityPackages = new Package[Consts.MAX_CAPACITY_OF_QUEUE];
    private Package[] lowPriorityPackages = new Package[Consts.MAX_CAPACITY_OF_QUEUE];

    private byte bandWidth;
    private byte capacity;

    public byte getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(byte bandWidth) {
        this.bandWidth = bandWidth;
    }

    public byte getCapacity() {
        return capacity;
    }

    public void setCapacity(byte capacity) {
        this.capacity = capacity;
    }
}
