package com.lsl.huoqiu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Forrest on 16/5/4.
 */
public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Service","onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Service","onCreate");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Service","onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("Service", "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service", "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
