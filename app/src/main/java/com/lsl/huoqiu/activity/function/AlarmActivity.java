package com.lsl.huoqiu.activity.function;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lsl.huoqiu.R;

import java.util.Calendar;

/**
 * Created by Forrest on 16/6/20.
 * 时钟提醒类Activity
 */
public class AlarmActivity extends AppCompatActivity{
    public static String ALARM_ACTION="com.lsl.huoqiu.alarm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        createAlarm();
    }
//    AlarmManager.RTC，硬件闹钟，不唤醒手机（也可能是其它设备）休眠；当手机休眠时不发射闹钟。
//
//    AlarmManager.RTC_WAKEUP，硬件闹钟，当闹钟发射时唤醒手机休眠；
//
//    AlarmManager.ELAPSED_REALTIME，真实时间流逝闹钟，不唤醒手机休眠；当手机休眠时不发射闹钟。
//
//    AlarmManager.ELAPSED_REALTIME_WAKEUP，真实时间流逝闹钟，当闹钟发射时唤醒手机休眠
    private void createAlarm() {

        Intent intent=new Intent();
        intent.setClass(AlarmActivity.this, AlarmActivity.alarmreceiver.class);
        intent.setAction(ALARM_ACTION);


        //设定一个五秒后的时间
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);


        PendingIntent pendingIntent=PendingIntent.getBroadcast(AlarmActivity.this,0,intent,0);

        AlarmManager alarm= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    //注意：receiver记得在manifest.xml注册
    public static class alarmreceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ALARM_ACTION)){
                Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "repeating alarm",Toast.LENGTH_LONG).show();
            }
        }
    }
}
