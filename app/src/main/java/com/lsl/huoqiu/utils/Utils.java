package com.lsl.huoqiu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by Forrest on 16/6/21.
 */
public class Utils {
    /**
     * 获取应用版本号
     *
     * @param ctx
     * @return
     */
    public static String getSoftwareVersion(Context ctx) {
        String version = "";
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
    /**
     * 获取IMEI，算法如下：
     * 1。 通过TelephonyManager.getDeviceId()获取唯一标识，若果唯一标示为空或长度小于8（如：#， 0， 1， null, Unknown等）
     * 		，或为一些已知的会重复的号码则继续往下，否则返回唯一号。特殊号码包括：
     * 		00000000
     * 		00000000000000
     *  	000000000000000
     *   	111111111111111
     *     	004999010640000
     *    	352751019523267
     *   	353867052181927
     *   	358673013795895
     *   	353163056681595
     *   	352273017386340
     *   	353627055437761
     *   	351869058577423
     *
     * 2. 通过WifiManager.getConnectionInfo().getMacAddress()取MAC地址作为唯一标识，若取得的是一些已知的会重复地址
     * 		则继续往下，否则返回MAC作为唯一号。特殊地址包括：00:00:00:00:00:00
     * 3. 通过生成随机UUID作为唯一标识
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(Context ctx) {
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (null != telephonyManager) {
            imei = telephonyManager.getDeviceId();
        }
        // get mac adress in case getDeviceId failed or get invalid id
        if (TextUtils.isEmpty(imei)
                || imei.length() < 8
                || isStringWithSameChar(imei)
                || "004999010640000".equals(imei)
                || "352751019523267".equals(imei)
                || "353867052181927".equals(imei)
                || "358673013795895".equals(imei)
                || "353163056681595".equals(imei)
                || "352273017386340".equals(imei)
                || "353627055437761".equals(imei)
                || "351869058577423".equals(imei)
                ) {
            if (null != wifiManager) {
                imei = wifiManager.getConnectionInfo().getMacAddress();
                if (TextUtils.isEmpty(imei)
                        || !isValidMacAddress(imei)
                        || "00:00:00:00:00:00".equals(imei)) {
                    // generate random UUID
                    imei = getUUID(ctx.getSharedPreferences("uuid", Context.MODE_PRIVATE));
                }
            } else {
                // generate random UUID
                imei = getUUID(ctx.getSharedPreferences("uuid", Context.MODE_PRIVATE));
            }
        }

        return imei;
    }

    /**
     * 判断字符串是否由相同的字符组成，比如“000000000000000”
     * @param str
     * @return
     */
    public static boolean isStringWithSameChar(String str) {
        if (null == str || str.length() < 2) {
            return true;
        }
        return str.replace(str.substring(0,1), "").length() == 0;
    }

    /**
     * 判断传入的字符串是否为有效地MAC地址，如00:01:36:D5:E0:D2 或 00-01-36-D5-E0-D2
     * @param str
     * @return
     */
    public static boolean isValidMacAddress(String str) {
        return null!=str && str.matches("([\\da-fA-F]{2}(?:\\:|-|$)){6}");
    }
    /**
     * @gm, retrive or generate a device uuid if IMEI is not available， e.g. 9a6c33a4-96b0-46b3-aead-e6e62b1a043e
     * @param sharedPreferences SharedPreferences which stores the value
     * @return The UUID for this device
     */
    public static String getUUID(SharedPreferences sharedPreferences) {
        String key = "device_uuid";
        String uuid = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(key, uuid).commit();
        }
//		if (uuid == null) return "000000";
        return uuid;
    }

}
