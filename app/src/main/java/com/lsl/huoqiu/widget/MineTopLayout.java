package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Forrest on 16/9/27.
 */
public class MineTopLayout extends LinearLayout {
    public MineTopLayout(Context context) {
        super(context);
    }

    public MineTopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e("MineTopLayout","onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.e("MineTopLayout","onLayout");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        Log.e("MineTopLayout","draw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.e("MineTopLayout","onDraw");
    }

    public void requeset(){
        requestLayout();
    }
    public void dddreaw(){
        invalidate();
    }
}
