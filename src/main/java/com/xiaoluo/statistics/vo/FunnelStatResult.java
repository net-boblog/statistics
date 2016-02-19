package com.xiaoluo.statistics.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/19.
 */
public class FunnelStatResult {
    private Map<String,Integer> funnelResult=new HashMap<String, Integer>();
    private Date from;
    private Date to;

    public Map<String, Integer> getFunnelResult() {
        return funnelResult;
    }

    public void setFunnelResult(Map<String, Integer> funnelResult) {
        this.funnelResult = funnelResult;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
