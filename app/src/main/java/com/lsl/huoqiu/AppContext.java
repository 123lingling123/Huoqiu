package com.lsl.huoqiu;

import android.app.Application;
import android.content.SharedPreferences;

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
        //收集错误日志
//        CrashHandler.getInstance().init(getApplicationContext());
        Config.init(this);
    }

    //是否开启手势密码
    public boolean isOpen(){
        SharedPreferences sp = getSharedPreferences("handlepw", MODE_PRIVATE);
        return sp.getBoolean("isopen", false);
    }
    //手势密码开关状态
    public void  setHandlepw(boolean isopen){
        SharedPreferences sp = getSharedPreferences("handlepw", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("isopen",isopen);
        ed.commit();
    }
    //获取手势密码
    public String gethandlepw(){
        SharedPreferences preferences = getSharedPreferences("lock",
                MODE_PRIVATE);
        String patternString = preferences.getString("lock_key",
                null);
        return patternString;
    }
    //清除手势密码和手势开关配置和用户登录信息
    public void deleteHandle(){
        SharedPreferences sp = getSharedPreferences("handlepw", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.commit();
        SharedPreferences preferences = getSharedPreferences("lock",
                MODE_PRIVATE);
        SharedPreferences.Editor hed = preferences.edit();
        hed.clear();
        hed.commit();
    }
}
