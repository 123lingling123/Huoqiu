package com.lsl.huoqiu.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lsl.huoqiu.net.listener.NetWorkListener;
import com.lsl.huoqiu.utils.LogUtils;

import java.util.LinkedList;

/**
 * Created by Forrest on 16/7/13.
 */
public class NetWorkReceiver extends BaseBroadcastReceiver {
    private LinkedList<NetWorkListener> mListeners=new LinkedList<>();

    /**
     * FIXME 这里的注册也许会有缺陷，详情请见:
     * Issue:经过p1000测试,在wifi断开网络连接的时候的确会有问题
     * http://stackoverflow.com/questions/5276032/connectivity-action-intent-recieved-twice-when-wifi-connected?answertab=active#tab-top
     */
    @Override
    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//		NetworkInfo affectedNetworkInfo=
//				intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        // 只获取当前active的NetworkInfo去通知listeners，否则会有问题。比如当2G/3G联网时打开WIFI，这时部分手机（比如Lenovo S820）会收到两个通知
        // 第一个是WIFI连接的通知，第二个是2G/3G断开的通知，此时第二个通知的NetworkInfo会变成listener中active的网络状态，这是不对的。
        NetworkInfo affectedNetworkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (null == affectedNetworkInfo) {
            return;
        }
        boolean disconnect=!affectedNetworkInfo.isConnected();
        if(disconnect){
            for(NetWorkListener listener:mListeners)
                listener.onDisconnected(affectedNetworkInfo);
            LogUtils.d("网络连接断开");
        }else{
            if(affectedNetworkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                for(NetWorkListener listener:mListeners)
                    listener.onWifiConnected(affectedNetworkInfo);
                    LogUtils.d("WIFI网络连接");
            }else if(affectedNetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                for(NetWorkListener listener:mListeners)
                    listener.onMobileConnected(affectedNetworkInfo);
                    LogUtils.d("移动网络连接");
            }
        }
    }

    /**
     * 添加监听
     * @param listener
     */
    public void addNetWorkListener(NetWorkListener listener){
        if (!mListeners.contains(listener)){
            mListeners.add(listener);
        }
    }

    /**
     * 移除监听
     * @param listener
     */
    public void removeNetWorkListener(NetWorkListener listener){
        mListeners.remove(listener);

    }
}
