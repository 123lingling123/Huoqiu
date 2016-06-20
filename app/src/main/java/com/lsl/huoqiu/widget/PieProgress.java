package com.lsl.huoqiu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lsl.huoqiu.R;

/**
 * Created by Forrest on 16/4/20.
 */
public class PieProgress extends View{
    private Paint paint;
    /** 文字颜色*/
    private int textColor;
    /** 背景颜色*/
    private int backGround;
    /** 进度颜色*/
    private int progressOne;
    /** 进度颜色*/
    private int progressTwo;
    /** 进度颜色*/
    private int progressThree;
    /** 进度颜色*/
    private int progressFour;
    /** 文字大小*/
    private float textSize;
    /** 最大进度*/
    private int max;
    /** 进度条宽度*/
    private float progressWidth;
    /** 进度的风格，实心或者空心*/
    private int style;
    /** 第一个进度*/
    private float oneProgress;
    /** 第二个进度*/
    private float twoProgress;
    /** 第三个进度*/
    private float threeProgress;
    /** 第四个进度*/
    private float fourProgress;
    private float startProgress=90;
    public PieProgress(Context context) {
        super(context);
    }

    public PieProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initArray(context, attrs);
    }

    public PieProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArray(context, attrs);
    }
    private void initArray(Context context, AttributeSet attrs){
        paint=new Paint();

        TypedArray mArray=context.obtainStyledAttributes(attrs, R.styleable.PieProgress);
        textColor=mArray.getColor(R.styleable.PieProgress_pie_textColor, Color.BLACK);
        backGround=mArray.getColor(R.styleable.PieProgress_pie_backGround,Color.GRAY);
        progressOne=mArray.getColor(R.styleable.PieProgress_pie_progressOne,Color.BLUE);
        progressTwo=mArray.getColor(R.styleable.PieProgress_pie_progressTwo,Color.BLUE);
        progressThree=mArray.getColor(R.styleable.PieProgress_pie_progressThree,Color.BLUE);
        progressFour=mArray.getColor(R.styleable.PieProgress_pie_progressFour,Color.BLUE);
        textSize=mArray.getDimension(R.styleable.PieProgress_pie_textSize, 5);
        max=mArray.getDimensionPixelSize(R.styleable.PieProgress_pie_max, 100);
        progressWidth=mArray.getDimension(R.styleable.PieProgress_pie_progressWidth, 20);
        style=mArray.getInt(R.styleable.PieProgress_pie_style, 0);

        mArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - progressWidth/2); //圆环的半径
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(progressWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
//        canvas.drawCircle(centre, centre, radius, paint); //画出圆环
        RectF oval=new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);
        /**
         * 画第一个进度
         */
        paint.setColor(progressOne); //设置圆环的颜色

        canvas.drawArc(oval, startProgress, oneProgress, true, paint);
        /**
         * 画第二个进度
         */
        paint.setColor(progressTwo); //设置圆环的颜色
        canvas.drawArc(oval, startProgress+oneProgress, twoProgress, true, paint);
        /**
         * 画第三个进度
         */
        paint.setColor(progressThree); //设置圆环的颜色
        canvas.drawArc(oval, startProgress + oneProgress + twoProgress, threeProgress, true, paint);
        /**
         * 画第四个进度
         */
        paint.setColor(progressFour); //设置圆环的颜色
        canvas.drawArc(oval, startProgress + oneProgress + twoProgress + threeProgress, fourProgress, true, paint);

        /**
         * 画里面的实心
         */
        paint.setColor(backGround);
        paint.setStyle(Paint.Style.FILL_AND_STROKE); //设置空心
        paint.setAntiAlias(true);  //消除锯齿
        Log.d("PieProgress","centre"+centre);
        canvas.drawCircle(centre, centre, radius - progressWidth, paint);
    }
    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }
    //设置起始角度
    public  void setPieProgress(float degree,float one,float two ,float three, float four){
        this.oneProgress=one;
        this.twoProgress=two;
        this.threeProgress=three;
        this.fourProgress=four;
        this.startProgress=degree;
        postInvalidate();
    }
    //设计进度条
    public  void setPieProgress(float one,float two ,float three, float four){
        this.oneProgress=one;
        this.twoProgress=two;
        this.threeProgress=three;
        this.fourProgress=four;
        postInvalidate();
    }
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackGround() {
        return backGround;
    }

    public void setBackGround(int backGround) {
        this.backGround = backGround;
    }

    public int getProgressOne() {
        return progressOne;
    }

    public void setProgressOne(int progressOne) {
        this.progressOne = progressOne;
    }

    public int getProgressTwo() {
        return progressTwo;
    }

    public void setProgressTwo(int progressTwo) {
        this.progressTwo = progressTwo;
    }

    public int getProgressThree() {
        return progressThree;
    }

    public void setProgressThree(int progressThree) {
        this.progressThree = progressThree;
    }

    public int getProgressFour() {
        return progressFour;
    }

    public void setProgressFour(int progressFour) {
        this.progressFour = progressFour;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        this.progressWidth = progressWidth;
    }

    public float getOneProgress() {
        return oneProgress;
    }

    public void setOneProgress(float oneProgress) {
        this.oneProgress = oneProgress;
    }

    public float getTwoProgress() {
        return twoProgress;
    }

    public void setTwoProgress(float twoProgress) {
        this.twoProgress = twoProgress;
    }

    public float getThreeProgress() {
        return threeProgress;
    }

    public void setThreeProgress(float threeProgress) {
        this.threeProgress = threeProgress;
    }

    public float getFourProgress() {
        return fourProgress;
    }

    public void setFourProgress(float fourProgress) {
        this.fourProgress = fourProgress;
    }
}
