package com.xiaoluo.statistics.entity;

/**
 * Created by Caedmon on 2016/1/15.
 */
public class SearchTemplate {
    private int id;
    private int type;
    private String name;
    private String params;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
