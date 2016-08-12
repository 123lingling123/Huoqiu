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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.AlarmBean;
import com.lsl.huoqiu.bean.BaseBean;
import com.lsl.huoqiu.bean.Person;
import com.lsl.huoqiu.parser.BaseBeanParser;
import com.lsl.huoqiu.receiver.PushReceiver;
import com.lsl.huoqiu.utils.AlarmUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Forrest on 16/6/20.
 * 时钟提醒类Activity
 */
public class AlarmActivity extends AppCompatActivity{
    private Button mBtnAlarm;
    private int id=0;
    private TextView textView;
    private TextView textviewparser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        mBtnAlarm= (Button) findViewById(R.id.buttonAlarm);
        textView= (TextView) findViewById(R.id.textview);
        textviewparser= (TextView) findViewById(R.id.textviewparser);
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
        gsonParse();
    }

    private void gsonParse(){
        JSONObject jsonObject=new JSONObject();
        //创建一个Json串
        try {
            jsonObject.put("message","成功返回");
            jsonObject.put("success",true);
            JSONArray jsonArray=new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject jsonObjectItem=new JSONObject();
                jsonObjectItem.put("name","我是"+i);
                jsonObjectItem.put("age",i);
                jsonArray.put(i,jsonObjectItem);
            }
            jsonObject.put("data",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //打印JsonObject的内容
        Log.e("AlarmActivity++++JsonObejct",jsonObject.toString());
        textView.setText(jsonObject.toString());
        //开始解析Json串，并将其转化为JavaBean
        try {
            BaseBean<List<Person>> personBaseBean=new  BaseBeanParser<BaseBean<List<Person>>>(
                    new TypeToken<BaseBean<List<Person>>>() {
                    }).parse(jsonObject.toString().getBytes());
            //打印解析出来的message字段
            Log.e("AlarmActivity++++JsonObejct", personBaseBean.getMessage());
            //将解析出来的JavaBean拆开进行显示：
            StringBuilder sb=new StringBuilder();
            sb.append(personBaseBean.getMessage()+"\n");
            sb.append(personBaseBean.isSuccess() + "\n");
            for (int i=0;i<10;i++){
                sb.append("名字是"+personBaseBean.getData().get(i).getName()
                        +"  年龄是："+personBaseBean.getData().get(i).getAge()+"\n");
            }
            //打印JavaBean 的内容
            Log.e("AlarmActivity++++JsonObejct",sb.toString());
            textviewparser.setText(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
