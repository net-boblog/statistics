package com.xiaoluo.statistics.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Caedmon on 2015/12/24.
 */
public class SearchParams {
    private Date from;
    private Date to;
    private List<String> prefixPages;
    private List<String> currentPages;
    private List<String> channels;
    private List<String> terminals;
    private List<String> events;
    private Map<String,String> extra;
    private int interval;
    private int unit;
    private List<String> uids;
    public enum SearchIntervalUnit{
        MINUTE(1),HOUR(2),DAY(3),MONTH(4);
        public int value;
        SearchIntervalUnit(int value){
            this.value=value;
        }
        public static SearchIntervalUnit valueOf(int value){
            switch (value){
                case 1:
                    return MINUTE;
                case 2:
                    return HOUR;
                case 3:
                    return DAY;
                case 4:
                    return MONTH;
                default:
                    throw new IllegalArgumentException("Mismatch unit value");
            }
        }
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

    public List<String> getPrefixPages() {
        return prefixPages;
    }

    public void setPrefixPages(List<String> prefixPages) {
        this.prefixPages = prefixPages;
    }

    public List<String> getCurrentPages() {
        return currentPages;
    }

    public void setCurrentPages(List<String> currentPages) {
        this.currentPages = currentPages;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }


    public static SearchParams copyFrom(SearchParams src){
        SearchParams target=new SearchParams();
        target.setFrom(src.getFrom());
        target.setTo(src.getTo());
        target.setPrefixPages(src.getPrefixPages());
        target.setCurrentPages(src.getCurrentPages());
        target.setChannels(src.getChannels());
        target.setTerminals(src.getTerminals());
        target.setEvents(src.getEvents());
        target.setExtra(src.getExtra());
        target.setInterval(src.getInterval());
        target.setUnit(src.getUnit());
        target.setUids(src.getUids());
        return target;
    }

}
