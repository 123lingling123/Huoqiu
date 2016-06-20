package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.utils.DeviceUtils;
import com.lsl.huoqiu.widget.PullToRefreshListView;
import com.lsl.huoqiu.widget.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/5/17.
 */
public class MultiViewPagerActivity extends AppCompatActivity implements  PullToRefreshListView.OnRefreshListener {
    private static int     TOTAL_COUNT = 10;
    private PullToRefreshListView listView;
    private RelativeLayout viewPagerContainer;
    private ViewPager viewPager;
    List<String> data = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_viewpager);
        viewPagerContainer= (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_viewpager, null);
        viewPager= (ViewPager) viewPagerContainer.findViewById(R.id.viewpager);
        listView= (PullToRefreshListView) findViewById(R.id.ptfListView);

//        viewPager=new ViewPager(MultiViewPagerActivity.this);
        viewPager.setClipChildren(false);

//        viewPager= (ViewPager) findViewById(R.id.viewpager);
        AbsListView.LayoutParams params1=new AbsListView.LayoutParams(
                DeviceUtils.getWindowWidth(this)*10/10,
                DeviceUtils.getWindowHeight(this)*8/10);
        viewPagerContainer.setLayoutParams(params1);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                DeviceUtils.getWindowWidth(this)*5/10,
                DeviceUtils.getWindowHeight(this)*6/10);
        viewPager.setLayoutParams(params);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        // to cache all page, or we will see the right item delayed
        viewPager.setOffscreenPageLimit(TOTAL_COUNT);
        viewPager.setPageMargin(100);
        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
        viewPager.setOnPageChangeListener(myOnPageChangeListener);

//        viewPager.setCurrentItem(1);
        listView.addHeaderView(viewPagerContainer);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, getData());
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
//        viewPagerContainer.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
//                return viewPager.dispatchTouchEvent(event);
//            }
//        });

    }

    @Override
    public void onRefresh() {
        new CountDownTimer(2000,1){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                listView.onRefreshComplete();

        }
        }.start();

        Toast.makeText(this,"shuaxian",Toast.LENGTH_SHORT).show();

    }

    /**
     * this is a example fragment, just a imageview, u can replace it with your needs
     *
     * @author Trinea 2013-04-03
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return TOTAL_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MultiViewPagerActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.mipmap.test);
            ((ViewPager)container).addView(imageView, position);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((ImageView)object);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // to refresh frameLayout
            if (viewPagerContainer != null) {
                viewPagerContainer.invalidate();
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    private List<String> getData(){

        data = new ArrayList<String>();
//        data.add("HuoQiuProgress");
//        data.add("HuoQiuProgressActivity");
//        data.add("HuoQiuMainActivity");
//        data.add("FengActivity");
//        data.add("VerticalViewPagerActivity");
//        data.add("GussBitmap");
//        data.add("GussLayout");
//        data.add("PullZoomActivity");
//        data.add("MultiViewPagerActivity");

        return data;
    }
}
