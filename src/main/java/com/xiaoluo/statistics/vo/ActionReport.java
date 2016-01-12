package com.xiaoluo.statistics.vo;

/**
 * Created by Caedmon on 2015/12/24.
 */
public class ActionReport {
    private String prefixPage;
    private String uid;
    private String currentPage;
    private String channel;
    private String terminal;
    private String event;
    private String keyWord;
    private long time;
    private String ip;
    public String getPrefixPage() {
        return prefixPage;
    }

    public void setPrefixPage(String prefixPage) {
        this.prefixPage = prefixPage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
