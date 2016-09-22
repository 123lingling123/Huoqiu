package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.RecentlyIncomeBean;
import com.lsl.huoqiu.utils.DeviceUtils;
import com.lsl.huoqiu.widget.LineChartView;
import com.lsl.huoqiu.widget.PercentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/8/12.
 */
public class PercentViewActivity extends AppCompatActivity{
    private PercentView percentView;
    private int progress= 0;
    private List<RecentlyIncomeBean> bean;
    private LineChartView mChartView;
    private int aimPercent=90;
    private Button button;
//    private double increaseValue=5;
//    private Handler handler=new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 0x23:
//                    if(progress>=0&&progress<=aimPercent) {
//                        handler.sendEmptyMessageDelayed(0x23, 200);
//                        progress+=increaseValue;
//                    }else if (progress>=0&&progress>aimPercent){
//                        handler.sendEmptyMessageDelayed(0x23, 200);
//                        percentView.setRankText("名列前茅","90");
//                        progress=-1;
//                    }else {
//
////                        progress=0;
////                        hq_view.isFinish=true;
//
//                    }
////                    percentView.setAngel(progress,60);
////                    handler.sendEmptyMessageDelayed(0x23,200);
//                    break;
//            }
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percent_view);
        percentView= (PercentView) findViewById(R.id.percent_view);
        percentView.setAngel(aimPercent);
        percentView.setRankText("名列前茅", "90");
        //折线图
        mChartView= (LineChartView) findViewById(R.id.linear_chart);
        getData();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DeviceUtils.getWindowWidth(PercentViewActivity.this)*10*(bean.size()+1)/75,600);
        Log.e("宽度", "" + DeviceUtils.getWindowWidth(PercentViewActivity.this) * 10 * (bean.size() + 1) / 75);
        mChartView.setLayoutParams(params);
//        mChartView.setBean(bean);




        button= (Button) findViewById(R.id.button);

    }
    @Override
    protected void onResume() {
        super.onResume();
        aimPercent=0;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               aimPercent+=2;
                percentView.setAngel(aimPercent);
                percentView.setRankText("测试",""+aimPercent);
            }
        });
    }

    private void getData() {

        bean=new ArrayList<RecentlyIncomeBean>();
        bean.add(new RecentlyIncomeBean("07-01","10.0"));
        bean.add(new RecentlyIncomeBean("07-02","0.0"));
        bean.add(new RecentlyIncomeBean("07-03","13.0"));
        bean.add(new RecentlyIncomeBean("07-04","4.0"));
        bean.add(new RecentlyIncomeBean("07-05","5.0"));
        bean.add(new RecentlyIncomeBean("07-06","0.02"));
        bean.add(new RecentlyIncomeBean("07-07","0.4"));
        bean.add(new RecentlyIncomeBean("07-08","1.4"));
        bean.add(new RecentlyIncomeBean("07-09","3.4"));
        bean.add(new RecentlyIncomeBean("07-10","7.0"));
        bean.add(new RecentlyIncomeBean("07-11","4"));
        bean.add(new RecentlyIncomeBean("07-12","11.4"));
    }
}
