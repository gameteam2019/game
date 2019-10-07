package com.demo.dp.model;

public class Consts {
    public static final int MAX_CAPACITY_OF_QUEUE = 200;

    public static final int MAX_NODES = 30;
    public static final int INVALID =-1;
    public static final int ENDNODEID =-2;
    public static final Node WAITNODE = new Node(INVALID,INVALID,INVALID,INVALID);
    public static final Node ENDNODE = new Node(ENDNODEID,INVALID,INVALID,INVALID);
    public static final EnterNode ENTEREND = new EnterNode(ENDNODE,INVALID,INVALID,null);
    public static final int  FIRSTDELAY = 1;
    public static final double  HIGHSENDRADIO = 0.5;
    public static final double  MIDDLESENDRADIO = 0.3;
    public static final double  LOWSENDRADIO = 0.2;
    public static final long LOSS_HIGH_WEIGHT = -5000;
    public static final long LOSS_MIDDLE_WEIGHT = -3000;
    public static final long LOSS_LOW_WEIGHT = -2000;
    public static final long SEND_HIGH_WEIGHT = 500;
    public static final long SEND_MIDDLE_WEIGHT = 300;
    public static final long SEND_LOW_WEIGHT = 200;
    public static final double LOSS_ACTUAL_RADIO=0.3;
    public static final double LOSS_CUSTOM_RADIO=LOSS_ACTUAL_RADIO*0.7;
    public static final long NOT_ACCEPT=Long.MAX_VALUE;
}
