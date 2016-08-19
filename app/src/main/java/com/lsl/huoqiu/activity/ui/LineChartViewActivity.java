package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.RecentlyIncomeBean;
import com.lsl.huoqiu.utils.DeviceUtils;
import com.lsl.huoqiu.widget.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/8/15.
 */
public class LineChartViewActivity extends AppCompatActivity {
    private List<RecentlyIncomeBean> bean;
    private LineChartView mChartView;
    private float progress;
    private float aimPercent=1;
    private double increaseValue=0.1;
//    private Handler handler=new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 0x23:
//                    if(progress>=0&&progress<=aimPercent) {
////                        mChartView.setmAnimatorValue(progress);
//                        handler.sendEmptyMessageDelayed(0x23, 200);
//                        progress+=increaseValue;
//                    }else if (progress>=0&&progress>aimPercent){
////                        mChartView.setmAnimatorValue(aimPercent);
//                        handler.sendEmptyMessageDelayed(0x23, 200);
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
        setContentView(R.layout.activity_line_chart_view);
        mChartView= (LineChartView) findViewById(R.id.linear_chart);
        getData();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DeviceUtils.getWindowWidth(LineChartViewActivity.this)*10*(bean.size()+1)/75,600);
        Log.e("宽度",""+DeviceUtils.getWindowWidth(LineChartViewActivity.this)*10*(bean.size()+1)/75);
        mChartView.setLayoutParams(params);
        mChartView.setBean(bean);

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

    @Override
    protected void onResume() {
        super.onResume();
//        handler.sendEmptyMessage(0x23);
    }
}
