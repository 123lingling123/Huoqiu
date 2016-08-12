package com.lsl.huoqiu.receiver;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

/**
 * 基础的BroadcastReceiver
 * Created by Forrest on 16/7/13.
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver{
    /**
     * 获取该事件监听器关心的事件
     * @return
     */
    public abstract IntentFilter getIntentFilter();
}
