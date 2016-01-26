package com.xiaoluo.statistics.vo;

/**
 * Created by Caedmon on 2016/1/18.
 */
public class SearchStatResult {

    private long pv;
    private double uv;
    private double ip;
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

    public void setTo(String to) {
        this.to = to;
    }
    public static class TermsResult{
        protected String value;
        protected long count;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
    public static class ExtraTermsResult extends TermsResult{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
