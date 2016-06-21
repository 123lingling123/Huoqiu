package com.lsl.huoqiu.activity.function;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.AlarmBean;
import com.lsl.huoqiu.receiver.PushReceiver;
import com.lsl.huoqiu.utils.AlarmUtils;

import java.util.Calendar;

/**
 * Created by Forrest on 16/6/20.
 * 时钟提醒类Activity
 */
public class AlarmActivity extends AppCompatActivity{
    private Button mBtnAlarm;
    private int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        mBtnAlarm= (Button) findViewById(R.id.buttonAlarm);
        mBtnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmBean bean=new AlarmBean(id,"测试"+id,"http://www.baidu.com","测试内容"+id,""+id,System.currentTimeMillis()+5*1000,id);
                AlarmUtils.addAlarm(AlarmActivity.this,bean);
//                createAlarm(id);
                id++;
//                Log.e("AlarmActivity","AlarmActivityID"+id);
            }
        });

    }
//    AlarmManager.RTC，硬件闹钟，不唤醒手机（也可能是其它设备）休眠；当手机休眠时不发射闹钟。
//
//    AlarmManager.RTC_WAKEUP，硬件闹钟，当闹钟发射时唤醒手机休眠；
//
//    AlarmManager.ELAPSED_REALTIME，真实时间流逝闹钟，不唤醒手机休眠；当手机休眠时不发射闹钟。
//
//    AlarmManager.ELAPSED_REALTIME_WAKEUP，真实时间流逝闹钟，当闹钟发射时唤醒手机休眠
    private void createAlarm(int id) {
        Log.e("AlarmActivity","createAlarm"+id);
//        int msg_id, String title, String url, String content, String type,
//        long time,int cls
        AlarmBean bean=new AlarmBean(id,"测试"+id,"http://www.baidu.com","测试内容"+id,""+id,System.currentTimeMillis()+5*1000,id);
        Log.i("AlarmDetail",bean.getTitle());
        Log.i("AlarmDetail",bean.getUrl());
        Intent intent=new Intent();
        intent.setClass(AlarmActivity.this, PushReceiver.class);
        intent.setAction(PushReceiver.PUSH_ACTIVE);
        intent.putExtra("bean", bean);


        //设定一个五秒后的时间
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);


        PendingIntent pendingIntent=PendingIntent.getBroadcast(AlarmActivity.this,0,intent,0);

        AlarmManager alarm= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


}
