package com.xiaoluo.statistics.search;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.List;

/**
 * Created by Caedmon on 2015/12/24.
 */
public class SimpleStatResult {
    private long pv;
    private double uv;
    private double ip;
    private String from;
    private String to;
    private long cost;
    private List<Terms.Bucket> termsBuckets;
    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }

    public double getIp() {
        return ip;
    }

    public void setIp(double ip) {
        this.ip = ip;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Terms.Bucket> getTermsBuckets() {
        return termsBuckets;
    }

    public void setTermsBuckets(List<Terms.Bucket> termsBuckets) {
        this.termsBuckets = termsBuckets;
    }
}
