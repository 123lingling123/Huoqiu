package com.lsl.huoqiu.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.WindowManager;
/**
 * Created by Forrest on 16/6/23.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
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
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     *            异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null || mContext == null)
            return false;
        final String crashReport = getCrashReport(mContext, ex);
        new Thread() {
            public void run() {
                Looper.prepare();
                File file = save2File(crashReport);
//                sendAppCrashReport(mContext, crashReport, file);
                Looper.loop();
            }

        }.start();
        return true;
    }

    //将错误日志存储到本地
    private File save2File(String crashReport){
        DateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        String time=dateFormat.format(new Date());//将当前毫秒数格式化固定格式
        String fileName="crash-"+time+"-"+System.currentTimeMillis()+".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"HuoQiuCrash");
                if (!dir.exists()){
                    dir.mkdir();
                    File file=new File(dir,fileName);
                    FileOutputStream fos=new FileOutputStream(file);
                    fos.write(crashReport.toString().getBytes());
                    fos.close();
                    return file;
                }
            }catch (Exception e){
                Log.e("CrashHanlder:save2File+error:",e.getMessage());
            }
        }
        return null;
    }



//    private void sendAppCrashReport(final Context context,
//                                    final String crashReport, final File file) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setTitle(R.string.app_error)
//                .setMessage(R.string.app_error_message)
//                .setPositiveButton(R.string.submit_report,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                try {
//
//                                    //这以下的内容，只做参考，因为没有服务器
//                                    Intent intent = new Intent(Intent.ACTION_SEND);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    String[] tos = { way.ping.li@gmail.com };
//                                    intent.putExtra(Intent.EXTRA_EMAIL, tos);
//
//                                    intent.putExtra(Intent.EXTRA_SUBJECT,
//                                           " Android客户端 - 错误报告");
//                                    if (file != null) {
//                                        intent.putExtra(Intent.EXTRA_STREAM,
//                                                Uri.fromFile(file));
//                                        intent.putExtra(Intent.EXTRA_TEXT,
//                                                "请将此错误报告发送给我，以便我尽快修复此问题，谢谢合作！"
//                                        );
//                                    } else {
//                                        intent.putExtra(Intent.EXTRA_TEXT,
//                                                "请将此错误报告发送给我，以便我尽快修复此问题，谢谢合作！"
//
//                                                + crashReport);
//                                    }
//                                    intent.setType(text/plain);
//                                    intent.setType(message/rfc882);
//                                    Intent.createChooser(intent, Choose Email Client);
//                                    context.startActivity(intent);
//
//                                } catch (Exception e) {
//                                    Log.i(Show,error: + e.getMessage());
//                                } finally {
//                                    dialog.dismiss();
//                                    // 退出
//                                    android.os.Process.killProcess(android.os.Process.myPid());
//                                    System.exit(1);
//                                }
//                            }
//                        })
//                .setNegativeButton(android.R.string.cancel,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                // 退出
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(1);
//                            }
//                        });
//
//        AlertDialog dialog = builder.create();
//        //需要的窗口句柄方式，没有这句会报错的
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
//    }

    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo packageInfo=getPackageInfo(context);
        StringBuilder sb=new StringBuilder();
        sb.append("Version:"+packageInfo.versionName+packageInfo.versionCode);
        sb.append("Android"+ Build.VERSION.RELEASE+Build.MODEL);
        sb.append("Exception:"+ex.getMessage());
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            sb.append(elements[i].toString());
        }
        return sb.toString();
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


}
