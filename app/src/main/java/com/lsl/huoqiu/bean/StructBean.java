package com.lsl.huoqiu.bean;

/**
 * Created by Forrest on 16/6/28.
 */
public class StructBean {
    private float rate;
    private boolean current;
    private String name;
    private int type;
    private String content;

    public StructBean(float rate, boolean current) {
        this.rate = rate;
        this.current = current;
    }

    public StructBean(String name, int type, String content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
