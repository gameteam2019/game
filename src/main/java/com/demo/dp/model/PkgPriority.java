package com.demo.dp.model;

public enum PkgPriority {
    HIGH(0), MIDDLE(1), LOW(2),LOSS(3);

    private int priority;

    PkgPriority(int priority) {
        this.priority = priority;
    }

    public int value() {
        return priority;
    }
}
