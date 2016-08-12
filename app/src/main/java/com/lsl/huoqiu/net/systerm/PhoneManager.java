package com.lsl.huoqiu.net.systerm;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.lsl.huoqiu.net.listener.NetWorkListener;
import com.lsl.huoqiu.receiver.BaseBroadcastReceiver;
import com.lsl.huoqiu.receiver.NetWorkReceiver;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * 获取手机状态信息
 * Created by Forrest on 16/7/13.
 */
public class PhoneManager {
    private NetWorkAdapter mNetWorkAdapter;//连接网络的状态信息
    private NetWorkReceiver mNetWorkReceiver;//网络连接变化的Receiver
    private WeakReference<Context> mWeakReference;//弱引用
//    private PhoneInfo mInfo;//获取
    private LinkedList<BaseBroadcastReceiver> mManagedReceivers = new LinkedList<BaseBroadcastReceiver>();

    //单例
    private static  PhoneManager mPhoneManager;
    public static synchronized  PhoneManager getInstance(Context context){
        if (mPhoneManager==null){
            mPhoneManager=new PhoneManager(context.getApplicationContext());
        }
        return  mPhoneManager;
    }

    /**
     * 创建PhoneManager
     * @param context
     * @return
     */
    public static PhoneManager createInstance(Context context){
        return new PhoneManager(context);
    }

    /**
     * 初始化
     * @param context
     */
    private PhoneManager(Context context) {
        mWeakReference = new WeakReference<Context>(context);
//        mInfo = new PhoneInfo(context);
        mNetWorkReceiver = new NetWorkReceiver();
        mNetWorkAdapter = new NetWorkAdapter(context);
        mNetWorkReceiver.addNetWorkListener(mNetWorkAdapter);
    }

    /**
     * 注册广播
     */
    public void registerSystemReceiver(){
        Context context = mWeakReference.get();
        if(context!=null){
            registerManagedReceiver(mNetWorkReceiver, context);
        }
    }

    /**
     * 注册广播
     * @param receiver
     * @param context
     */
    private void registerManagedReceiver(BaseBroadcastReceiver receiver,Context context) {
        if (receiver == null || mManagedReceivers.contains(receiver))
            return;
        mManagedReceivers.add(receiver);
        context.registerReceiver(receiver, receiver.getIntentFilter());
    }

    /**
     * 注销Receiver
     * @param receiver
     * @param context
     */
    private void unRegisterReceiver(BroadcastReceiver receiver,Context context){
            int index=mManagedReceivers.indexOf(receiver);
        if (index!=-1){
            context.unregisterReceiver(mManagedReceivers.remove(index));
        }
    }

    /**
     * 是否联网
     * @return
     */
    public boolean isConnectedNetwork() {
        return mNetWorkAdapter.isConnectedNetwork();
    }

    /**
     * 是否是WIFI连接
     * @return
     */
    public boolean isConnectedWifi() {
        return mNetWorkAdapter.isConnectedWiFi();
    }

    /**
     * 是否是移动网络连接
     * @return
     */
    public boolean isConnectedMobileNet() {
        return mNetWorkAdapter.isConnectedMobileNet();
    }
    public void addOnNetWorkChangeListener(NetWorkListener listioner) {
        mNetWorkReceiver.addNetWorkListener(listioner);
    }

    public void removeNetworkChangeListioner(NetWorkListener listioner) {
        mNetWorkReceiver.removeNetWorkListener(listioner);
    }

    /**
     * 释放与该Context注册的Receiver资源
     */
    public void destroy() {
        Context context = mWeakReference.get();
        if (context != null) {
            unRegisterReceiver(mNetWorkReceiver,context);
        }
    }

//    public String getImei() {
//        return mInfo.getImei();
//    }

    public boolean isConnectionFast() {
        return mNetWorkAdapter.isConnectionFast();
    }
}
