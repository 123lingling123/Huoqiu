package com.lsl.huoqiu.bean;

/**
 * Created by Forrest on 16/6/28.
 */
public class StructBean {
    private float rate;
    private boolean current;


    public StructBean(float rate, boolean current) {
        this.rate = rate;
        this.current = current;
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
}
