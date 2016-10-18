package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.lsl.huoqiu.AppContext;
import com.lsl.huoqiu.R;
import com.lsl.huoqiu.adapter.StructFragmentAdapter;
import com.lsl.huoqiu.bean.StructBean;
import com.lsl.huoqiu.fragment.StructFragment;
import com.lsl.huoqiu.widget.PagerSlidingTabStrip;
import com.lsl.huoqiu.widget.PagerTabSliding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/9/29.
 */
public class PagerSlidingTabActivity extends AppCompatActivity {
    private PagerSlidingTabStrip tab;
    private ViewPager pager;
    private ArrayList<Fragment> mFragments;
    private List<StructBean> beans;
    private int startId=2;//起始选中的ID
    private int currentId;//经过切换之后的当前的ID
    private String startDate="起始的请求数据";//起始的请求数据
//    private PagerTabSliding tabs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_sliding);

        tab= (PagerSlidingTabStrip) findViewById(R.id.tab);
//        tabs= (PagerTabSliding) findViewById(R.id.tabs);
        pager= (ViewPager) findViewById(R.id.pager);

        initData();


        pager.setAdapter(new StructFragmentAdapter(getSupportFragmentManager(),mFragments,beans));

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tab.setViewPager(pager);
//        tabs.setViewPager(pager);

        pager.setCurrentItem(startId);
        tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //先设置当前的ID然后在调用Fragment的OnResume方法
                setCurrentId(beans.get(position).getType());
                mFragments.get(position).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        beans=new ArrayList<>();
        mFragments=new ArrayList<>();
        for (int i=0;i<10;i++){
            if (i%2==0) {
                beans.add(new StructBean("结构化理财" + i, i, "我是结构化理财内容" + i));
            }else {
                beans.add(new StructBean("牛先锋" + i, i, "我是牛先锋" + i));
            }
        }

        //创建Fragment
        for (int i=0;i<beans.size();i++){
            StructBean bean=beans.get(i);
            Bundle args = new Bundle();
            args.putString("text","测试"+bean.getType()+bean.getContent());
            args.putInt("id",bean.getType());
            StructFragment f = StructFragment.newInstance(args);
            mFragments.add(f);
        }
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public String getStartDate() {
        return startDate;
    }
}
