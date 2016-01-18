package com.xiaoluo.statistics.entity;

/**
 * Created by Caedmon on 2015/12/24.
 */
public class ActionReport {
    private String prefix_page;
    private String uid;
    private String current_page;
    private String channel;
    private String terminal;
    private String event;
    private String key_word;
    private long time;
    private String ip;
    private String version;
    public String getPrefix_page() {
        return prefix_page;
    }

    public void setPrefix_page(String prefix_page) {
        this.prefix_page = prefix_page;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
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

    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
