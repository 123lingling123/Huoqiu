package com.lsl.huoqiu.net.systerm;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.lsl.huoqiu.utils.LogUtils;

/**
 * Created by Forrest on 16/7/13.
 */
public class PhoneInfo {

    private String imei;
    //获取设备IMEI码
    PhoneInfo(Context context){
        TelephonyManager manager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE   );
        imei=manager.getDeviceId();
        LogUtils.i("该设备的IMEI码是"+imei);
    }

    public String getImei(){
        return  imei;
    }
}
