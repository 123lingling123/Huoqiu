package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;


/**
 * Created by Forrest on 16/9/30.
 */
public class FlingTextView extends LinearLayout {
    private String TAG="FlingTextView";

    private TextView first;
    private TextView second;
    private int mWidth;
    private int mHeight;
//    private int mTotalHeight = 0;
    private int num=30;
    private int offset=0;
    private ValueAnimator valueAnimatorFloat;
    public FlingTextView(Context context) {
        super(context);
        init(context);
    }

    public FlingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        first=new TextView(context);
        second=new TextView(context);
        setOrientation(VERTICAL);
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        first.setText("第一");
//        first.setTextColor(Color.RED);
//        first.setLayoutParams(lp);
//        first.setBackgroundColor(Color.CYAN);
//        second.setText("第一");
//        second.setLayoutParams(lp);
//        second.setBackgroundColor(Color.GRAY);
        for (int i=0;i<num;i++){
            TextView textView=new TextView(context);
            textView.setText(i+".34");
            textView.setTextColor(Color.RED);
            textView.setLayoutParams(lp);
            addView(textView);
        }

//        addView(first);
//        addView(second);
        Log.e(TAG,"加载View");
        Log.e(TAG,"加载View"+first.getLeft());
        Log.e(TAG,"加载View"+first.getTop());
        Log.e(TAG,"加载View"+first.getRight());
        Log.e(TAG,"加载View"+first.getBottom());
//        scrollTo(0,30);
    }
    /**
     * 计算控件的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth= measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);

        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(mWidth, mHeight);
    }


    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }
    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    /**
     * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
     * 才能确定怎么摆放
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG,"onLayoutLeft"+l);
        Log.e(TAG,"onLayoutTop"+t);
        Log.e(TAG,"onLayoutRight"+r);
        Log.e(TAG,"onLayoutBottom"+b);
        // 记录总高度
        int mTotalHeight = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            childView.layout(l, mTotalHeight+offset, measuredWidth, mTotalHeight+ measureHeight+offset);
            mTotalHeight += measureHeight;

        }
    }


    private void setOffset(int offset){
        this.offset=offset;
        requestLayout();
    }
    public void transferY(){
        valueAnimatorFloat = ValueAnimator.ofFloat(0, (num-1)*mHeight);
        valueAnimatorFloat.setDuration(2000);
        valueAnimatorFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                setOffset(mHeight-(int) Float.parseFloat(valueAnimator.getAnimatedValue().toString()));
//                Log.e(TAG,"增长的值"+(mHeight-(int) Float.parseFloat(valueAnimator.getAnimatedValue().toString())));

                scrollTo(0, (int) Float.parseFloat(valueAnimator.getAnimatedValue().toString()));
            }


        });
        valueAnimatorFloat.start();
    }


}
