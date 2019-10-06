package com.demo.dp.model;

public enum QuePriority {
    HIGH(0), MIDDLE(1), LOW(2),UNKNOWN(-1);

    private int priority;

    QuePriority(int priority) {
        this.priority = priority;
    }

    public int value() {
        return priority;
    }
}
