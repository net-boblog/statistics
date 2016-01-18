package com.xiaoluo.statistics.constant;

/**
 * Created by Administrator on 2016/1/14.
 */
public enum DictType {
    PAGE(1),EVENT(2),CHANNEL(3),TERMINAL(4),UNKOWN(5);
    public int value;
    DictType(int value){
        this.value=value;
    }
    public static DictType valueOf(int value){
        switch (value){
            case 1:
                return PAGE;
            case 2:
                return EVENT;
            case 3:
                return CHANNEL;
            case 4:
                return TERMINAL;
            default:
                return UNKOWN;
        }
    }
}
