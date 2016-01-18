package com.xiaoluo.statistics.dao.filter;

/**
 * Created by Caedmon on 2016/1/15.
 */
public class SearchTemplateFilter extends Filter{
    private int type;
    private String name;

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
}
