package com.maomaoyu.toutiao.async;

/**
 * maomaoyu    2018/12/9_20:22
 **/
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    REGSUCCESS(4),
    KAP(5),
    READMESSAGE(6);

    private int value;

    EventType(int value) {
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
