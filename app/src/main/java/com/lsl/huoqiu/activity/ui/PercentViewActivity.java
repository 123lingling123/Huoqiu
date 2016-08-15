package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.PercentView;

/**
 * Created by Forrest on 16/8/12.
 */
public class PercentViewActivity extends AppCompatActivity{
    private PercentView percentView;
    private int progress= 0;

    private int aimPercent=97;
    private double increaseValue=5;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x23:
                    if(progress>=0&&progress<=aimPercent) {
                        percentView.setAngel(progress,aimPercent);
                        handler.sendEmptyMessageDelayed(0x23, 200);
                        progress+=increaseValue;
                    }else if (progress>=0&&progress>aimPercent){
                        percentView.setAngel(aimPercent, aimPercent);
                        handler.sendEmptyMessageDelayed(0x23, 200);
                        percentView.setRankText("名列前茅","90");
                        progress=-1;
                    }else {

//                        progress=0;
//                        hq_view.isFinish=true;

                    }
//                    percentView.setAngel(progress,60);
//                    handler.sendEmptyMessageDelayed(0x23,200);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percent_view);
        percentView= (PercentView) findViewById(R.id.percent_view);

    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(0x23);
    }
}
