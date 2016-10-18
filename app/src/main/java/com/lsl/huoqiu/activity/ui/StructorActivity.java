package com.lsl.huoqiu.activity.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.adapter.StructFragmentAdapter;
import com.lsl.huoqiu.bean.StructBean;
import com.lsl.huoqiu.fragment.StructFragment;
import com.lsl.huoqiu.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 结构化理财界面
 * 初次进入该界面需要调整到对应的数据之下，比如第三个，就显示第三个
 *
 * 每个Item的Tab的字数并不能确定，可以是三个，也可以是四个，也可以是五个，因此在设计每个Tab的长度时我觉得应该选用Wrap
 * 如果有游标的话，不建议使用动画，直接用drawable文件比较方便，如果必须要用游标的话，就要计算游标的位置和游标每次移动的距离
 *
 * Created by Forrest on 16/9/23.
 */
public class StructorActivity extends AppCompatActivity {
    private LinearLayout mLinear;
    private List<StructBean> beans;
    private ArrayList<Fragment> mFragments;
    private HorizontalScrollView mHorizontal;
    private ViewPager mViewPager;
    private int mCurrentTab =2;

    private LinearLayout mLinearCursor;
    private LinearLayout mStructCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structor);
        mLinear= (LinearLayout) findViewById(R.id.linear_content);
        mHorizontal= (HorizontalScrollView) findViewById(R.id.horizontal);
        mViewPager= (ViewPager) findViewById(R.id.viewpager);
        //添加假数据
        initData();

        //设置ViewPager
        initViewPager();
        //设置TAB
//        installData();
        mLinearCursor= (LinearLayout) findViewById(R.id.linear_cursor);
        mLinearCursor.setWeightSum(beans.size());

        mStructCursor= (LinearLayout) findViewById(R.id.struct_cursor);


    }




    private void initData() {
        beans=new ArrayList<>();
        mFragments=new ArrayList<>();
        for (int i=0;i<10;i++){
            if (i%2==0) {
                beans.add(new StructBean("结构化理财" + i, i, "我是结构化理财内容" + i));
            }else {
                beans.add(new StructBean("牛先锋按时发生的发生" + i, i, "我是牛先锋" + i));
            }
        }

        //创建Fragment
        for (int i=0;i<beans.size();i++){
            StructBean bean=beans.get(i);
            Bundle args = new Bundle();
            args.putString("text","测试"+bean.getType()+bean.getContent());
            StructFragment f = StructFragment.newInstance(args);
            mFragments.add(f);
        }


    }

    private void installData() {
        mLinear.removeAllViews();
        int index=0;
        for (int i=0;i<beans.size();i++) {
            final StructBean bean=beans.get(i);
            //创建按钮
            Button button=new Button(this);
            button.setText(bean.getName());
            LinearLayout.LayoutParams parms=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(parms);
            if (bean.getType()== mCurrentTab){
                index=i;
                button.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2048));
            }else {
                button.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2));
            }

            button.setTextColor(Color.BLACK);
            button.setTag(bean);
            mLinear.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getType()!= mCurrentTab){
                        mCurrentTab =bean.getType();
                        Log.e("StructorActivity","点击按钮"+ mCurrentTab);
                        for (int i=0;i<mLinear.getChildCount();i++){
                            Button button= (Button) mLinear.getChildAt(i);
                            StructBean bean= (StructBean) button.getTag();
                            if (bean.getType()== mCurrentTab){
                                mViewPager.setCurrentItem(i);
                                button.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2048));
                            }else {
                                button.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2));
                            }

                        }
                    }
                }
            });


        }


    }
    private void selectTab(int position){
        mCurrentTab=position;
//        for (int i=0;i<mLinear.getChildCount();i++){
            View checkView=mLinear.getChildAt(position);//得到选中的View
            int k=checkView.getMeasuredWidth();//得到选中View的宽度
            int l=checkView.getLeft();//得到选中VIew的左边距离
            int i2=l+k*3/2- DeviceUtils.getWindowWidth(StructorActivity.this);//将View移到中间位置

            Log.e("StuctorActivity","得到View的宽度"+k);
            Log.e("StuctorActivity","得到选中VIew的左边距离"+l);
            Log.e("StuctorActivity","滑动的距离"+i2);
            mHorizontal.smoothScrollTo(i2,0);//滑动到相关位置
//        }



        //判断是否选中
        for (int i = 0; i <mLinear.getChildCount() ; i++) {
            Button  checkViewButton= (Button) mLinear.getChildAt(i);
            if (i==position){


                checkViewButton.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2048));
            }else {
                checkViewButton.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2));
            }
        }



    }
//    private int cursorLength = 0;// 游标长度
//    private float translationY = 0;// 当前位置
//    private int duration = 200;// 动画时间
//
//    /**
//     * 游标位移动画
//     *
//     * @param duration
//     *            动画时间
//     * @param start
//     *            开始位置
//     * @param end
//     *            结束位置
//     */
//    private void cursorAnimation(int duration, float start, float end) {
//        if (duration < 0) {
//            duration = -duration;
//        }
//        TranslateAnimation animation = new TranslateAnimation(start, end, 0, 0);
//        animation.setInterpolator(new LinearInterpolator()); // 动画以均匀的速度改变
//        animation.setDuration(duration);
//        animation.setFillAfter(true);
//        mStructCursor.startAnimation(animation);
//    }
    /**
     * 构建ViewPager
     */
    private void initViewPager() {
        mViewPager.setAdapter(new StructFragmentAdapter(getSupportFragmentManager(),mFragments));
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(1);
        installData();
        mViewPager.setCurrentItem(mCurrentTab);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            mCurrentTab =position;
            selectTab(mCurrentTab);
            Log.e("StuctorActivity","ViewPagerSelect"+ mCurrentTab);


        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // to refresh frameLayout
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


}
