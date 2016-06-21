package com.lsl.huoqiu.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.lsl.huoqiu.activity.FirstActivity;
import com.lsl.huoqiu.bean.AlarmBean;
import com.lsl.huoqiu.utils.AlarmUtils;

import java.util.Iterator;


/**
 * Created by Forrest on 16/6/20.
 *  注意：receiver记得在manifest.xml注册
 */
public class PushReceiver extends BroadcastReceiver {
    public static final String PUSH_ACTIVE = "com.lsl.huoqiu.PushActive";
    public static final String PUSH_TIME_KEY = "com.lsl.huoqiu.PushTimeKey";
    private final int ACQUIRE_WAKE_LOCK = 1;
    private final int RELEASE_WAKE_LOCK = 2;
    private PowerManager.WakeLock mWakeLock;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("PushReceiver","得到了消息");
        if(intent.getAction().equals(PUSH_ACTIVE)){
            long time = intent.getLongExtra(PUSH_TIME_KEY, 0);
            //直接调用AlarmUtils的数据列表完成数据的传递
            Iterator<AlarmBean> iterator = AlarmUtils.alarmBeans.iterator();
            while (iterator.hasNext()) {
                AlarmBean bean = iterator.next();
                if (bean.getTime() == time) {
                    showNotificationFunction(context, bean, intent);
                    AlarmUtils.sendMessage(context);
                }else if(bean.getTime() < time){
                    iterator.remove();
                }
            }


//            AlarmBean bean= (AlarmBean) intent.getSerializableExtra("bean");
//            Log.e("AlarmBean",""+bean.getTitle());
//            showNotificationFunction(context, bean,intent);
//            Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "repeating alarm",Toast.LENGTH_LONG).show();
        }
    }

    private void showNotificationFunction(Context ctx,AlarmBean  alarmBean,Intent intent){
        if (!alarmBean.needLight()){//由于没有设数据所以默认是false，还有几个参数，这里就意思意思一下
            // 唤醒屏幕
            PowerManager pm = (PowerManager) ctx
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.FULL_WAKE_LOCK, "唤醒屏幕5秒钟");
            handler.sendEmptyMessage(ACQUIRE_WAKE_LOCK);
        }
        String title = alarmBean.getTitle();
        String content = alarmBean.getContent();
        String type = alarmBean.getType();
        long pushtime = alarmBean.getTime();
        // build通知 这里好像只能找到drawable的文件夹，mipmap好像是找不到的
        int icon = ctx.getApplicationContext().getResources()
                .getIdentifier("study_icon", "drawable", ctx.getPackageName());
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title).bigText(content);

        Intent intentBase=new Intent(ctx, FirstActivity.class);
        //必需要添加FLAG，否则启动不了Activity
        intentBase.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent tempIntent = new Intent(ctx, PushHandleReceiver.class);
        tempIntent.putExtra("baseIntent", intentBase);
//		tempIntent.setAction(PushHandlerReceiver.PUSH_ACTIVE);
        tempIntent.putExtras(intent);
//        tempIntent.putExtra("message", intent.getStringExtra("message"));
        //必需使用PendingIntent.FLAG_UPDATE_CURRENT这个FLag否则不会启动Intent
        PendingIntent pendingIntent=PendingIntent.getBroadcast(ctx,alarmBean.getMsg_id(),tempIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent.getBroadcast(ctx, alarmBean.getMsg_id(), new Intent(ctx, PushHandleReceiver.class).putExtra("id", "0").putExtra("bean", "im"), 0)

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(icon).setContentTitle(title)
                .setContentText(content)
                .setStyle(bigTextStyle)
                .setDeleteIntent(PendingIntent.getBroadcast(ctx, alarmBean.getMsg_id(), new Intent(ctx, PushDeleteReceiver.class).putExtra("id", alarmBean.getMsg_id()), 0))
                .setLights(Color.GREEN, 1, 0).setWhen(pushtime)
                .setContentIntent(pendingIntent)
                ;
        int defaultNotify = 0;
        if(alarmBean.needLight()){
            defaultNotify |= Notification.DEFAULT_LIGHTS;
        }
        if(alarmBean.needSound()){
            defaultNotify |= Notification.DEFAULT_SOUND;
        }
        if(alarmBean.needVibrate()){
            defaultNotify |= Notification.DEFAULT_VIBRATE;
        }
        builder.setDefaults(defaultNotify);
        Notification notification = builder.build();
        notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;
        NotificationManager manager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(0);//alarmBean.getMsg_id()
        // 发送通知
        manager.notify(ctx.getPackageName(), 0, notification);//alarmBean.getMsg_id()

    }



    //处理震动服务等
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ACQUIRE_WAKE_LOCK:
                    mWakeLock.acquire();
                    this.removeMessages(RELEASE_WAKE_LOCK);
                    this.sendMessageDelayed(
                            this.obtainMessage(RELEASE_WAKE_LOCK, 0, 0), 5000);
                    break;

                case RELEASE_WAKE_LOCK:
                    mWakeLock.release();
                    break;

                default:
                    break;
            }
        }
    };
}
