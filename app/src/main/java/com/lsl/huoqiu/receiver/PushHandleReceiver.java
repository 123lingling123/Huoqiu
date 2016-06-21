package com.lsl.huoqiu.receiver;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.os.Process;
import java.util.List;

/**
 * Created by Forrest on 16/6/20.
 */
public class PushHandleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("PushDeleteReceiver", "点击了提醒:处理了消息");
        ActivityManager actvityManager = (ActivityManager)context.getSystemService( Context.ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
        int id = Process.myPid();
        for(ActivityManager.RunningAppProcessInfo procInfo : procInfos) {
            if (id == procInfo.pid)
            {
                if (!TextUtils.isEmpty(procInfo.processName) &&
                        (procInfo.processName.equals("com.lsl.huoqiu"))) {
                    Intent base = intent.getParcelableExtra("baseIntent");
                    if(base != null){
                        context.startActivity(base);
                    }
                }
            }
        }

//        }
    }

//告诉Umeng说这个消息被处理掉了同时自定义处理消息
//    public static final String PUSH_ACTIVE = "com.huoqiu.app.push.PushHandlerActive";
//    public static final String PUSH_TIME_KEY = "com.huoqiu.app.push.PushTimeKey";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String message = intent.getStringExtra("message");
//        if(intent.getBooleanExtra("isPush", false)){
//            try {
//                UTrack.getInstance(context).trackMsgClick(new UMessage(new JSONObject(message)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        ActivityManager actvityManager = (ActivityManager)context.getSystemService( Context.ACTIVITY_SERVICE );
//        List<RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
//        int id = Process.myPid();
//        for(RunningAppProcessInfo procInfo : procInfos) {
//            if (id == procInfo.pid)
//            {
//                if (!TextUtils.isEmpty(procInfo.processName) &&
//                        (procInfo.processName.equals("com.huoqiu.app"))) {
//                    Intent base = intent.getParcelableExtra("baseIntent");
//                    if(base != null){
//                        context.startActivity(base);
//                    }
//                }
//            }
//        }
//    }
}
