package com.lsl.huoqiu.bean;


import java.util.HashMap;
import java.io.Serializable;
/**
 * Created by Forrest on 16/6/20.
 */
public class AlarmBean implements Serializable{

    private static final long serialVersionUID = -4138683214655288775L;

    private String title;
    private String url;
    private String content;
    private String type;
    private long time;
    private int msg_id;
    private boolean needLight = true;
    private boolean needVibrate = true;
    private boolean needSound = true;
    private int cls;//1：火球计划,2:小火球 (根据此字段获取不同产品定时时间)
    private HashMap<String, String> extraMap;


    public AlarmBean(int msg_id, String title, String url, String content, String type,
                     long time,int cls) {
        this.msg_id = msg_id;
        this.title = title;
        this.url = url;
        this.content = content;
        this.type = type;
        this.time = time;
        this.cls = cls;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String documentId) {
        this.url = documentId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getMsg_id() {
        return msg_id;
    }
    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }
    public boolean needLight() {
        return needLight;
    }
    public void setNeedLight(boolean needLight) {
        this.needLight = needLight;
    }
    public boolean needVibrate() {
        return needVibrate;
    }
    public void setNeedVibrate(boolean needVibrate) {
        this.needVibrate = needVibrate;
    }
    public boolean needSound() {
        return needSound;
    }
    public void setNeedSound(boolean needSound) {
        this.needSound = needSound;
    }
    public int getCls() {
        return cls;
    }
    public void setCls(int cls) {
        this.cls = cls;
    }

    public HashMap<String, String> getExtraMap() {
        return extraMap;
    }
    public void setExtraMap(HashMap<String, String> extraMap) {
        this.extraMap = extraMap;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlarmBean other = (AlarmBean) obj;
        if(other.content.equals(content)&&other.time == time&&other.title.equals(title)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "title is "+title+", content is "+content+",url is "+url+",time is "+time;
    }
}