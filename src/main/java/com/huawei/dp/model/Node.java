package com.huawei.dp.model;

import com.sun.org.apache.bcel.internal.Const;

public class Node {
    private Package[] highPriorityPackages = new Package[Consts.MAX_CAPACITY_OF_QUEUE];
    private Package[] middlePriorityPackages = new Package[Consts.MAX_CAPACITY_OF_QUEUE];
    private Package[] lowPriorityPackages = new Package[Consts.MAX_CAPACITY_OF_QUEUE];


}
