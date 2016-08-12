package com.lsl.huoqiu.utils;

import android.util.Log;

/**
 * 打印LOG工具类
 * Created by Forrest on 16/7/12.
 */
public class LogUtils {
    private static boolean debug=true;
    private static String TAG="FORREST——LOG";

    public static void d(String msg) {
        if (debug) {
            Log.d(TAG, msg);
        }
    }
    public static void e(String msg) {
        if (debug) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (debug) {
            Log.i(TAG, msg);
        }
    }
    public static void w(String msg) {
        if (debug) {
            Log.w(TAG, msg);
        }
    }


}
