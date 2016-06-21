package com.lsl.huoqiu.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
