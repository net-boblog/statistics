package com.xiaoluo.statistics.search;

import java.util.Date;

/**
 * Created by Caedmon on 2015/12/24.
 */
public class SearchParams {
    private Date from;
    private Date to;
    private String prefixPages;
    private String currentPages;
    private String channels;
    private String terminals;
    private String events;
    private String keyWords;
    private int interval;
    private int unit;
    private String uid;
    private int minTermsCount;
    private String termsCountField;
    public enum SearchIntervalUnit{
        MINUTE(0),HOUR(1),DAY(2),MONTH(3);
        public int value;
        SearchIntervalUnit(int value){
            this.value=value;
        }
        public static SearchIntervalUnit valueOf(int value){
            switch (value){
                case 0:
                    return MINUTE;
                case 1:
                    return HOUR;
                case 2:
                    return DAY;
                case 3:
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

    public String getPrefixPages() {
        return prefixPages;
    }

    public void setPrefixPages(String prefixPages) {
        this.prefixPages = prefixPages;
    }

    public String getCurrentPages() {
        return currentPages;
    }

    public void setCurrentPages(String currentPages) {
        this.currentPages = currentPages;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getTerminals() {
        return terminals;
    }

    public void setTerminals(String terminals) {
        this.terminals = terminals;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getMinTermsCount() {
        return minTermsCount;
    }

    public void setMinTermsCount(int minTermsCount) {
        this.minTermsCount = minTermsCount;
    }

    public String getTermsCountField() {
        return termsCountField;
    }

    public void setTermsCountField(String termsCountField) {
        this.termsCountField = termsCountField;
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
        target.setKeyWords(src.getKeyWords());
        target.setInterval(src.getInterval());
        target.setUnit(src.getUnit());
        target.setMinTermsCount(src.getMinTermsCount());
        target.setUid(src.getUid());
        target.setTermsCountField(src.getTermsCountField());
        return target;
    }

}
