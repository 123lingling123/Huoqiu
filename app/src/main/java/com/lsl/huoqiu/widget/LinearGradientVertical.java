package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Forrest on 16/8/16.
 */
public class LinearGradientVertical extends View {
    public LinearGradientVertical(Context context) {
        super(context);
        init(context);
    }

    public LinearGradientVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinearGradientVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        mPaint=new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPath=new Path();

    }

    private Paint mPaint;
    private Path mPath;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
//        canvas.rotate(-90, 150, 125);
//        mPath.addRect(100, 100, 200, 150, Path.Direction.CCW);
//        int colorSweep[] = {Color.RED,Color.BLUE};
////        float position[]={0.3f,0.5f};
////        LinearGradient lg=new LinearGradient(start,mTopLineHeight+mShowHeight,start + (mPositionHeight.size() - 1) * mSpaceWidth,height,colorSweep,position, Shader.TileMode.MIRROR);
//        LinearGradient lg=new LinearGradient(100, 100, 100, 150,colorSweep,null, Shader.TileMode.MIRROR);
////        mShadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//
//
//
//
//        mPaint.setShader(lg);

//        int colorSweep[] = { Color.WHITE,Color.RED,Color.BLUE,Color.DKGRAY};
//        float position[]={0.3f,0.5f,0.7f,0.8f};
//        int colorSweep[] = { Color.RED,Color.RED};
//        float position[]={0.2f,0.7f};
        int colorSweep[] = { Color.BLUE,Color.RED};
        float position[]={0.5f,0.7f};
        SweepGradient sweepGradient=new SweepGradient(getWidth()/2, getHeight()/2, colorSweep, position);
        mPaint.setShader(sweepGradient);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2,getHeight()/2,100,mPaint);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();
        canvas.save();
        mPaint.reset();

        mPaint.setTextSize(30);
        mPaint.setColor(Color.BLACK);
        canvas.drawText("我是一个文本",100,200,mPaint);

    }
}
