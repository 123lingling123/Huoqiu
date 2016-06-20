package com.lsl.huoqiu.activity.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.DialogView;

/**
 * Created by Forrest on 16/5/26.
 */
public class HqDialogActivity extends AppCompatActivity {
    private DialogView dialog_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        dialog_view= (DialogView) findViewById(R.id.dialog_view);
        dialog_view.setPositionX(222,444);
        Dialog dialog=getMineSmallDialog(HqDialogActivity.this);
//        dialog.show();
    }
    //返回第一次进入应用时弹出小火球的复投换地方的提示
    public  Dialog getMineSmallDialog(final Context context){
        final Dialog dialog = new Dialog(context,R.style.MyDialog_Theme);//MyDialog_Theme
        dialog.setContentView(R.layout.mine_small_dialog);
        dialog_view= (DialogView)dialog. findViewById(R.id.dialog_view);
//        dialog_view.setPosition(200,400);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = getScreenHW(context)[0];
        params.height = getScreenHW(context)[1];
        dialog.getWindow().setAttributes(params);
        return dialog;
    }
    /**
     * 获取屏幕宽和高
     * @param context
     * @return
     */
    public static int[] getScreenHW(Context context){
        int[] hw = new int[3];
        try {
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            hw[0] = dm.widthPixels;//屏幕宽带(像素)
            hw[1] = dm.heightPixels;//屏幕高度(像素)
            hw[2] = dm.densityDpi;//屏幕密度(120/160/240)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hw;
    }

}
