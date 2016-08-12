package com.lsl.huoqiu.utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lsl.huoqiu.bean.AlarmBean;
import com.lsl.huoqiu.receiver.PushReceiver;


/**
 * Created by Forrest on 16/6/20.
 */
public class AlarmUtils {

    private static boolean isChanged = false;
    public static int ALARM_CODE_HUOQIU = 101;
    public static int ALARM_CODE_SHUOQIU = 102;
    public static ArrayList<AlarmBean> alarmBeans = new ArrayList<AlarmBean>();


    public static boolean addAlarm(Context context, AlarmBean bean){
        synchronized (alarmBeans) {
            if(!alarmBeans.contains(bean)){
                alarmBeans.add(bean);
                //升序处理将第一个将要显示的Alarm放到第一个
                Collections.sort(alarmBeans, new Comparator<AlarmBean>() {

                    @Override
                    public int compare(AlarmBean lhs,
                                       AlarmBean rhs) {
                        return ((Long)lhs.getTime()).compareTo(rhs.getTime());
                    }

                });
                saveAlarm(context);
                AlarmUtils.sendMessage(context);
                return true;
            }
        }
        return false;
    }
    //取消提醒
    public static boolean removeAlarm(Context context, AlarmBean bean){
        synchronized (alarmBeans) {
            if(alarmBeans.contains(bean)){
                alarmBeans.remove(bean);
                saveAlarm(context);
                AlarmUtils.sendMessage(context);
                return true;
            }
        }
        return false;
    }

    public static boolean isRecode(String title, String content, long time,int cls){
        synchronized (alarmBeans) {
            if(alarmBeans.contains(new AlarmBean(ALARM_CODE_HUOQIU, title, null, content, null, time,cls))){
                return true;
            }
            if(alarmBeans.contains(new AlarmBean(ALARM_CODE_SHUOQIU, title, null, content, null, time,cls))){
                return true;
            }
        }
        return false;
    }
    //发送提醒
    public static void sendMessage(Context context){
        for(AlarmBean bean : alarmBeans){
            if(bean.getTime()>System.currentTimeMillis()){
                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, PushReceiver.class);
				intent.setAction(PushReceiver.PUSH_ACTIVE);
                intent.putExtra(PushReceiver.PUSH_TIME_KEY, bean.getTime());
                intent.putExtra("isPush", false);
                PendingIntent pendIntent = PendingIntent.getBroadcast(context,
                        bean.getMsg_id(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                //setImpl(type, triggerAtMillis, WINDOW_EXACT, 0, 0, operation, null, null);
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, bean.getTime(), pendIntent);
                //setImpl(type, triggerAtMillis, legacyExactLength(), 0, 0, operation, null, null);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, bean.getTime(), pendIntent);
                break;
            }
        }
    }
    //存储一个闹钟
    public static void saveAlarm(Context context){
        if(alarmBeans!=null){
            try {
                Files.serializeObject(context.getFileStreamPath("HQAlarm+"+Utils.getSoftwareVersion(context)+".dat"), (Serializable) alarmBeans);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //读取闹钟
    @SuppressWarnings("unchecked")
    public static ArrayList<AlarmBean> loadAlarm(Context context){
        try {
            File file = context.getFileStreamPath("HQAlarm+"+Utils.getSoftwareVersion(context)+".dat");
            if(file.exists()&&file.lastModified()<(System.currentTimeMillis()-60*24*60*60*1000)){
                file.delete();
                return null;
            }else if(file.exists()){
                Log.d("HQdebug", "config load "+context.getFileStreamPath("HQAlarm+"+Utils.getSoftwareVersion(context)+".dat"));
                return (ArrayList<AlarmBean>) Files.deserializeObject(context.getFileStreamPath("HQAlarm+"+Utils.getSoftwareVersion(context)+".dat"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
