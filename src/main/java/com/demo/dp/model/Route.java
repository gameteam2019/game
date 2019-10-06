package com.demo.dp.model;

import java.util.LinkedList;

public class Route {
    private int id;
    private int actualStart;
    private LinkedList<EnterNode> pathNodes;//队列

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActualStart() {
        return actualStart;
    }

    public void setActualStart(int actualStart) {
        this.actualStart = actualStart;
    }

    public LinkedList<EnterNode> getPathNodes() {
        return pathNodes;
    }

    public void setPathNodes(LinkedList<EnterNode> pathNodes) {
        this.pathNodes = pathNodes;
    }


}
