package com.lsl.huoqiu.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lsl.huoqiu.AppContext;

/**
 * Created by Forrest on 16/6/20.
 */
public class PushDeleteReceiver extends BroadcastReceiver {
    public static final String PUSH_ACTIVE="com.lsl.huoqiu.receiver.PushDeleteActive";
    @Override
    public void onReceive(Context context, Intent intent) {
        int id=intent.getIntExtra("id",-1);
        if (intent==null||context==null){
            Log.e("PushDeleteReceiver", "取消了提醒:传入值为空");
            return;
        }else {
            Log.e("PushDeleteReceiver", "取消了提醒");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);//Integer.parseInt(id)
            Log.e("PushDeleteReceiver", "取消了提醒:对应的ID是"+id);

        }
    }
    //告诉Umeng这个消息被处理掉了
//    public static final String PUSH_ACTIVE = "com.huoqiu.app.push.PushDeleteActive";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String message = intent.getStringExtra("message");
//        if(intent.getBooleanExtra("isPush", false)){
//            try {
//                UTrack.getInstance(context).trackMsgDismissed(new UMessage(new JSONObject(message)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        boolean isPush = intent.getBooleanExtra("isPush", true);
//        if(isPush){
//            if (intent == null || context == null) {
//                return;
//            }
//            int id = intent.getIntExtra("id", -1);
//            if(id != -1){
//                NotificationManager manager = (NotificationManager) context
//                        .getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.cancel(id);
//            }
//
//        }
//    }
}
