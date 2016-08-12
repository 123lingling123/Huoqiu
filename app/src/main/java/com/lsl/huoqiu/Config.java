package com.lsl.huoqiu;

import android.content.Context;

import com.lsl.huoqiu.net.ApnManager;
import com.lsl.huoqiu.net.systerm.PhoneManager;
import com.lsl.huoqiu.utils.LogUtils;
import com.lsl.huoqiu.utils.Utils;

/**
 * Created by Forrest on 16/7/13.
 */
public class Config {
    public static String IMEI;
    public static void init(Context context){
        IMEI= Utils.getIMEI(context);
        LogUtils.i("设备的IMEI码是"+IMEI);
        PhoneManager.getInstance(context).registerSystemReceiver();
        PhoneManager.getInstance(context).addOnNetWorkChangeListener(
                ApnManager.getInstance(context));
//        initImageLoader(context);
    }


    public static void destroy(Context context){
        PhoneManager.getInstance(context).destroy();
    }

}
