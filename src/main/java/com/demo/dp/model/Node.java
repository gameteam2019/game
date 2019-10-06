package com.demo.dp.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Node {


    private int id;
    private int bandWidth;
    private int capacity;
    private Package[][] priorityQueue;
    private List<Package> finishedPackages;



    private List<Package> awaitPackages; //需要保证awaitPacks是按照实际出发时间有序排列
    private Queue<Package> lossQueue = new LinkedList<Package>();

    public Node(Node node) {
        this.bandWidth = node.getBandWidth();
        this.capacity = node.getCapacity();
        this.id = node.getId();
        this.priorityQueue =node.getPriorityQueue();
    }

    public Queue<Package> getLossQueue() {
        return lossQueue;
    }

    public void setLossQueue(Queue<Package> lossQueue) {
        this.lossQueue = lossQueue;
    }

    public Node(int id,int bandWidth, int capacity) {
        this.bandWidth = bandWidth;
        this.capacity = capacity;
        this.id = id;
        priorityQueue = new Package[3][capacity];
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

    public Package[][] getPriorityQueue() {
        return priorityQueue;
    }

    public void setPriorityQueue(Package[][] priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    public List<Package> getFinishedPackages() {
        return finishedPackages;
    }

    public void setFinishedPackages(List<Package> finishedPackages) {
        this.finishedPackages = finishedPackages;
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
}
