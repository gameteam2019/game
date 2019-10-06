package com.demo.dp.model;

public class SendQueInfo {
    public SendQueInfo(int sendLimit, int canSendMax) {
        this.sendLimit = sendLimit;
        this.canSendMax = canSendMax;
        this.moreCapacity = sendLimit -canSendMax>0?sendLimit -canSendMax:0;
    }
    int sendLimit;
    int canSendMax;
    int moreCapacity;

    public int getSendLimit() {
        return sendLimit;
    }

    public int getCanSendMax() {
        return canSendMax;
    }

    public int getMoreCapacity() {
        return moreCapacity;
    }

    public void setMoreCapacity(int moreCapacity) {
        this.moreCapacity = moreCapacity;
    }



}
