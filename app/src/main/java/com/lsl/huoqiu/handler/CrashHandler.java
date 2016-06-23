package com.lsl.huoqiu.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.huoqiu.AppContext;
import com.lsl.huoqiu.R;
import com.lsl.huoqiu.activity.ui.MainActivity;

/**
 * Created by Forrest on 16/6/23.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    private String TAG="CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    //单例
    private static  CrashHandler mCrashHandler;
    private CrashHandler(){
    }
    public static  CrashHandler getInstance(){
        if (mCrashHandler==null){
            mCrashHandler=new CrashHandler();
        }
        return mCrashHandler;
    }
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    //初始化
    public void init(Context context){
        mContext=context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
    * 当UncaughtException发生时会转入该重写的方法来处理
    */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }else {
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);

        //使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
////                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }.start();
        //保存日志文件
        saveCatchInfo2File(ex);
        return true;
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        //将崩溃报告写入sb中
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        //将崩溃的详细信息写入到sb中
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        try {
            long timestamp = System.currentTimeMillis();//写入时 的当前时间
            String time = formatter.format(new Date());//format日期
            String fileName = "crash-" + time + "-" + timestamp + ".log";//Log名称
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //崩溃报告存放的地方
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"HuoQiuCrash"+File.separator;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                //发送给开发人员
                sendCrashLog2PM(path+fileName);
                fos.close();
            }
//            ShowDialog(sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员
     *
     * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     */
    private void sendCrashLog2PM(String fileName){
        if(!new File(fileName).exists()){
//            Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            while(true){
                s = reader.readLine();
                if(s == null) break;
                //由于目前尚未确定以何种方式发送，所以先打出log日志。
                Log.e(TAG, s.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{   // 关闭流
            try {
                reader.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    private void ShowDialog(String text){
        final Dialog dialog = new Dialog(mContext,R.style.MyDialog_Theme);
        dialog.setContentView(R.layout.crash_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        TextView crash_content = (TextView)dialog.findViewById(R.id.crash_content);
        TextView crash_cancel = (TextView)dialog.findViewById(R.id.crash_cancel);
        TextView crash_send = (TextView)dialog.findViewById(R.id.crash_send);
        crash_content.setText(text);
        crash_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        crash_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                LoginActivity.startLoginActivity(context, paramMap);
                dialog.dismiss();
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
}
