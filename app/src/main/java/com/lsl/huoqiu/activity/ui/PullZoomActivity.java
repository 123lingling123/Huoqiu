package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lsl.huoqiu.R;

import com.lsl.huoqiu.widget.pullzoom.PullToZoomScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/5/13.
 */
public class PullZoomActivity extends AppCompatActivity {
    private PullToZoomScrollView pull_zoom;
    private int width;
    private ArrayAdapter<String> adapter;
    private LinearLayout listView;
    List<String> data = new ArrayList<String>();
    private RelativeLayout relative;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_zoom_activity);
        pull_zoom= (PullToZoomScrollView) findViewById(R.id.pull_zoom);

        relative= (RelativeLayout) findViewById(R.id.relative);
        relative.setAlpha(0);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, getData());
        initPull();
    }

    private void initPull() {

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
                width, (int) (11.0F * (width / 16.0F)));
        pull_zoom.setHeaderLayoutParams(localObject);


        listView = (LinearLayout) pull_zoom.getRootView()
                .findViewById(R.id.linear_contain);
        for (int i=0;i<20;i++){
            Button button=new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setText("THE Number"+i);
            listView.addView(button);
        }

       pull_zoom.setOnPullToUpListener(new PullToZoomScrollView.OnPullToUpListener() {
           @Override
           public void PullToUp(float newScrollValues) {
               Log.d("PullZoomActivity", "onScrollChanged --> f = " + newScrollValues);
               float alpha=newScrollValues /( 11.0F * (width / 16.0F));
               relative.setAlpha(alpha);

           }


       });

//        listView.setAdapter(adapter);
//
//        setListViewHeight(listView);
    }
    private List<String> getData(){

        data = new ArrayList<String>();
        data.add("HuoQiuProgress");
        data.add("HuoQiuProgressActivity");
        data.add("HuoQiuMainActivity");
        data.add("FengActivity");
        data.add("VerticalViewPagerActivity");
        data.add("GussBitmap");
        data.add("GussLayout");
        data.add("DialogActivity");
        data.add("PullZoomActivity");

        return data;
    }
    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
