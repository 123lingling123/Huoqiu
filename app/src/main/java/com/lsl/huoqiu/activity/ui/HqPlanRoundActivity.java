package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.HuoQiuView;
import com.lsl.huoqiu.widget.pullzoom.PullToZoomScrollView;

/**
 * Created by Forrest on 16/5/13.
 */
public class HqPlanRoundActivity extends AppCompatActivity {
    private int progress= 0;
    private HuoQiuView hq_view;
    private PullToZoomScrollView pull_zoom;
    private LinearLayout listView;
    private int width;
    private RelativeLayout view_contain;
    private RelativeLayout text_contain;
    private TextView text_rate;
    private View right;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x23:
                    if(progress<=360) {
                        progress+=3;
                        hq_view.setAngle(progress);
                        handler.sendEmptyMessageDelayed(0x23,200);
                    }else{
//                        progress=0;
//                        hq_view.isFinish=true;

                    }
//                    hq_view.setAngle(progress);
//                    handler.sendEmptyMessageDelayed(0x23,100);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(0x23);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huoqiu_srcoll_activity);

        view_contain= (RelativeLayout) findViewById(R.id.view_contain);
        initPull();

        hq_view= (HuoQiuView) findViewById(R.id.hq_view);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, (int) (4.0F * (width / 16.0F)) - PullToZoomScrollView.mContantDis, 0, 0);
        view_contain.setLayoutParams(params);
//        hq_view.setLayoutParams(params);
        hq_view.setHeight((int) (12.0F * (width / 16.0F)));
        hq_view.setMoney("150万");
        hq_view.setTextDes("本月分红");


        text_contain= (RelativeLayout) findViewById(R.id.text_contain);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                width, (int) (12.0F * (width / 16.0F)));
        text_contain.setLayoutParams(params1);

        text_rate= (TextView) findViewById(R.id.text_rate);
        text_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HqPlanRoundActivity.this,"点击了复合年化收益率",Toast.LENGTH_SHORT).show();
            }
        });

        right=findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HqPlanRoundActivity.this,"点击本月分红",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initPull() {
        pull_zoom= (PullToZoomScrollView) findViewById(R.id.hq_scroll);
        View heardView = LayoutInflater.from(this).inflate(
                R.layout.pull_zoom_headview, null, false);
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pull_zoom_content, null, false);
        ImageView zoomView = new ImageView(this);
        zoomView.setImageResource(R.mipmap.test);
        zoomView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // 得到屏幕的宽度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        // height = metric.heightPixels; // 屏幕高度（像素）
        pull_zoom.setHeaderView(heardView);
        pull_zoom.setZoomView(zoomView);
        pull_zoom.setScrollContentView(contentView);
        // 设置照片的高度
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(
                width, (int) (10.0F * (width / 16.0F)));
        pull_zoom.setHeaderLayoutParams(localObject);


        listView = (LinearLayout) pull_zoom.getRootView()
                .findViewById(R.id.linear_contain);
        for (int i = 0; i < 20; i++) {
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setText("THE Number" + i);
            listView.addView(button);
        }

        pull_zoom.setOnPullToUpListener(new PullToZoomScrollView.OnPullToUpListener() {
            @Override
            public void PullToUp(float newScrollValues) {
                Log.d("PullZoomActivity", "onScrollChanged --> f = " + newScrollValues);
                float alpha = newScrollValues / (11.0F * (width / 16.0F));
//                relative.setAlpha(alpha);

            }

        });
        pull_zoom.setOnPullToDownListener(new PullToZoomScrollView.OnPullToDownListener() {
            @Override
            public void pullToDown(float newScrollValue) {
                view_contain.scrollTo(0, (int) (newScrollValue / PullToZoomScrollView.HEAD_DAMP));
                mDistance= (int) Math.abs(newScrollValue);
                if (mDistance<PullToZoomScrollView.mContantDis*PullToZoomScrollView.CONTENT_DAMP){
                    //+ PullToZoomScrollView.mContantDis
                    hq_view.scrollTo(0, (int) (newScrollValue / PullToZoomScrollView.CONTENT_DAMP ));
                    text_contain.scrollTo(0, (int) (newScrollValue / PullToZoomScrollView.CONTENT_DAMP ));

                }
            }

            @Override
            public void pullToRefresh(float newScrollValues) {
                if (newScrollValues <PullToZoomScrollView. mContantDis * PullToZoomScrollView.CONTENT_DAMP) {
                    hq_view.scrollTo(0, (int) (-newScrollValues/PullToZoomScrollView.CONTENT_DAMP));
                    text_contain.scrollTo(0, (int) (-newScrollValues/PullToZoomScrollView.CONTENT_DAMP));
                }
                view_contain.scrollTo(0, -(int) (newScrollValues/PullToZoomScrollView.HEAD_DAMP));
            }

            @Override
            public void pullToFinish() {
                hq_view.scrollTo(0,0);
                text_contain.scrollTo(0,0);
                view_contain.scrollTo(0,0);
            }

        });
    }
    private  int mDistance;
}
