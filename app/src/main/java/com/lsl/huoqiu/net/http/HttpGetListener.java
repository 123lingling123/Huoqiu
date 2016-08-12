package com.lsl.huoqiu.net.http;

/**
 * 数据请求回调接口
 * Created by Forrest on 16/7/13.
 */
public interface HttpGetListener {
    void preLoad();
    void postExecute();
    void loadHttpFail();
    void loadHttpSuccess();
}
