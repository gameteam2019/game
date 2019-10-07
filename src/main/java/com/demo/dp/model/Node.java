package com.demo.dp.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Node {

    private int id;
    private int index;
    private int bandWidth;
    private int capacity;
    private Queue<Package>[] priorityQueue;
    private Queue<Package>[] finishedPriorityPackages;
    private List<Package> awaitPackages; //需要保证awaitPacks是按照实际出发时间有序排列
    private Queue<Package> lossQueue = new LinkedList<Package>();//没有重复
    private Queue<Package>[] historyLossPriorityQueue;//历史上所有的丢包记录,会有重复



    private Queue<Package> opticalFiber = new LinkedList<Package>();//在队列中缓存的
    public Node(int id,int bandWidth, int capacity,int index) {
        this.bandWidth = bandWidth;
        this.capacity = capacity;
        this.id = id;
        this.index = index;
        initQueueArray(this.priorityQueue);
        initQueueArray(this.historyLossPriorityQueue);
    }

    private void initQueueArray(Queue<Package>[] queArray) {
        queArray = (Queue<Package>[]) new Package[3];
        queArray[0] = new LinkedList<Package>();
        queArray[1] = new LinkedList<Package>();
        queArray[2] = new LinkedList<Package>();

    }
    public Queue<Package>[] getHistoryLossPriorityQueue() {
        return historyLossPriorityQueue;
    }

    public void setHistoryLossPriorityQueue(Queue<Package>[] historyLossPriorityQueue) {
        this.historyLossPriorityQueue = historyLossPriorityQueue;
    }

    public int getIndex() {
        return index;
    }

    public Queue<Package> getLossQueue() {
        return lossQueue;
    }

    public void setLossQueue(Queue<Package> lossQueue) {
        this.lossQueue = lossQueue;
    }


    public int getId() {
        return id;
    }

    public void setBandWidth(int bandWidth) {
        this.bandWidth = bandWidth;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Queue<Package>[] getPriorityQueue() {
        return priorityQueue;
    }

    public void setPriorityQueue(Queue<Package>[] priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    public Queue<Package>[] getFinishedPriorityPackages() {
        return finishedPriorityPackages;
    }

    public void setFinishedPriorityPackages(Queue<Package>[] finishedPriorityPackages) {
        this.finishedPriorityPackages = finishedPriorityPackages;
    }

    public List<Package> getAwaitPackages() {
        return awaitPackages;
    }

    public void setAwaitPackages(List<Package> awaitPackages) {
        this.awaitPackages = awaitPackages;
    }




    public int getBandWidth() {
        return bandWidth;
    }

    public int getCapacity() {
        return capacity;
    }
    public Queue<Package> getOpticalFiber() {
        return opticalFiber;
    }

    public void setOpticalFiber(Queue<Package> opticalFiber) {
        this.opticalFiber = opticalFiber;
    }
}
