package com.lsl.huoqiu.bean;

/**
 * Created by Forrest on 16/8/15.
 */
public class RecentlyIncomeBean {

    private String date;
    private String amount;

    public RecentlyIncomeBean(String date, String amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
