package com.lsl.huoqiu;

import android.app.Application;

import com.lsl.huoqiu.handler.CrashHandler;

/**
 * Created by Forrest on 16/5/4.
 */
public class AppContext extends Application {
    private static AppContext context;
    public static AppContext getInstance(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        CrashHandler.getInstance().init(getApplicationContext());
    }
}
