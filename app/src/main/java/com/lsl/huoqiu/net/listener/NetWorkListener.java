package com.lsl.huoqiu.net.listener;

import android.net.NetworkInfo;

/**
 * 监听网络变化的接口
 * Created by Forrest on 16/7/12.
 */
public interface NetWorkListener {
    /**
     * 连接wifi时
     * Wifi网络连接的相关信息
     * @param
     */
    void onWifiConnected(NetworkInfo networkInfo);
    /**
     * 连接手机数据网络时
     * 手机网络连接的相关信息
     * @param
     */
    void onMobileConnected(NetworkInfo networkInfo);

    /**
     * 断开网络连接时
     * 断开网络的网络信息
     * @param
     */
    void onDisconnected(NetworkInfo networkInfo);

}
