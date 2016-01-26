package com.xiaoluo.statistics.entity;

/**
 * Created by Administrator on 2016/1/19.
 */
public class Dict {
    private String id;
    private int type;
    private String description;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
