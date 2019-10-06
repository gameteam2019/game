package com.demo.dp.model;

public class Package {
    private int id;
    private int startNodeIndex;
    private int targetNodeIndex;
    private PkgPriority priority;
    private int startTick;
    private Route route;

    public int getStartNodeIndex() {
        return startNodeIndex;
    }

    public void setStartNodeIndex(int startNodeIndex) {
        this.startNodeIndex = startNodeIndex;
    }

    public int getTargetNodeIndex() {
        return targetNodeIndex;
    }

    public void setTargetNodeIndex(int targetNodeIndex) {
        this.targetNodeIndex = targetNodeIndex;
    }

    public PkgPriority getPriority() {
        return priority;
    }

    public void setPriority(PkgPriority priority) {
        this.priority = priority;
    }

    public int getStartTick() {
        return startTick;
    }

    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
