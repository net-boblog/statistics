package com.xiaoluo.statistics.vo;

import java.util.List;

/**
 * Created by Caedmon on 2016/1/18.
 */
public class SearchStatResult {

    private long pv;
    private double uv;
    private double ip;
    private List<TermsResult> termsResults;
    private String from;
    private String to;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public List<TermsResult> getTermsResults() {
        return termsResults;
    }

    public void setTermsResults(List<TermsResult> termsResults) {
        this.termsResults = termsResults;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public static class TermsResult{
        private String key;
        private long count;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

}
