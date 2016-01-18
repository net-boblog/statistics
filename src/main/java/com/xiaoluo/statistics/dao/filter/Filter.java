package com.xiaoluo.statistics.dao.filter;

/**
 * author  living.li
 * date    2015/8/13.
 */
public class Filter  {

    protected String orderType;

    protected int pageSize=30;

    protected int pageIndex=1;

    public int getStart(){
        if(pageIndex<=0){
            pageIndex=1;
        }
        return (pageIndex-1)*pageSize;
    }

    public int getEnd(){
        return pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        if(pageIndex<1){
            pageIndex=1;
        }
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if(pageSize<=0){
            pageSize=30;
        }
        this.pageSize = pageSize;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
