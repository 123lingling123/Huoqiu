package com.lsl.huoqiu.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;
import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.RecentlyIncomeBean;
import com.lsl.huoqiu.utils.DeviceUtils;
import com.lsl.huoqiu.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *近七日收益折线图，可点击切换到当前日期并显示当前日期的收益
 *设计思路：首先绘制日期和最底下的横线，然后根据数据绘制一个闭合的路径用来绘制渐变色区域
 *接着绘制一个红色折线的路径，最后画原点
 *点击事件的分配是利用x坐标来计算的，每个item的宽度width一定就可以利用x/width取整来确定点击的是哪个item
 * Created by Forrest on 16/8/15.
 */
public class LineChartView extends View{
    //全局Context
    private Context mContext;
    // View 宽高
    private int mViewWidth;
    private int mViewHeight;
    //折线图各部分高度
    private int mTopLineHeight;//顶部头部高度
    private int mShowHeight;//顶部留下显示详情的高度
    private int mLineHeight;//折线图最高显示高度
    private int mSpaceHeight;//日期与折线图底部的间距
    private int mDateHeight;//日期所占的高度
    private int mSpaceWidth;//每条数据对应的宽度
    //颜色
    private int mTextGray;
    private int mRed;
    private int mPink;
    private int mLineGray;
    //文字大小
    private int mTextDateSize;
    private int mTextShowSize;

    //画笔
    private Paint mPaintDate;
    private Paint mPaint;
    private Paint mShadePaint;
    private Bitmap mShowBit;
    //数据
    private List<RecentlyIncomeBean> bean;
    private List<Float> mPositionHeight;
    //当前选择项
    private int mCurrent=0;
    private float mAnimatorValue=0;
    //Path
    private Path mLinePath;//渐变色路径
    private Path mRedPath;//红色折线路径
    //惯性滑动
    private Scroller mScroll;

    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    //过程动画
    private ValueAnimator mValueAnimator;
    // 用于控制动画状态转换
    private Handler mAnimatorHandler;
    // 默认的动效周期 2s
    private int defaultDuration = 2000;

    public LineChartView(Context context) {
        super(context);
        this.mContext=context;
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init(context);
    }
    private void init(Context context){
        mScroll=new Scroller(context);
        mTextGray = context.getResources().getColor(R.color.struct_text_gray);
        mRed = context.getResources().getColor(R.color.struct_text_red);
        mPink = context.getResources().getColor(R.color.percent_pink);
        mLineGray = context.getResources().getColor(R.color.percent_gray);
        mTextDateSize = context.getResources().getDimensionPixelOffset(R.dimen.sp12);
        mTextShowSize = context.getResources().getDimensionPixelOffset(R.dimen.sp12);
        mShowBit= BitmapFactory.decodeResource(context.getResources(), R.mipmap.line_chart_tips);

        mLinePath=new Path();
        mRedPath=new Path();
        bean=new ArrayList<RecentlyIncomeBean>();
        mPositionHeight=new ArrayList<Float>();
        mPaintDate=new Paint();
        mPaint=new Paint();

        mShadePaint=new Paint();

//        mPaint.setColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mViewHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        Log.i("TAG", "控件的宽度" + mViewWidth + "控件的高度" + mViewHeight);
    }
    private int count=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("LineChartView", "开始绘制"+count);
        count++;
        initHeight();
        Log.i("TAG", "数据的大小"+bean.size());
        if (bean!=null&&bean.size()>0){
            long startTime=System.currentTimeMillis();
            Log.i("TAG", "进行绘制");
            getPosition(bean);
            drawDate(canvas);
            //绘制点击区域测试使用
//            drawClickArea(canvas);
            //绘制折线区域
            drawLineArea(canvas);
//            mPaint.setColor(mLineGray);
//            mPaint.setStrokeWidth(2);
//            canvas.drawLine(mSpaceWidth / 2, mViewHeight - mDateHeight - mSpaceHeight, mSpaceWidth * 3 / 4 + (mPositionHeight.size() - 1) * mSpaceWidth + mSpaceWidth / 2, mViewHeight - mDateHeight - mSpaceHeight, mPaint);
            long endTime=System.currentTimeMillis();
            Log.i("LineChartView", "绘制结束" + (endTime-startTime));
        }
    }



    /**
     * 设置各部分的高度所占控件的比例
     */
    private void initHeight() {
        mTopLineHeight= (int) (mViewHeight*0.07);
        mShowHeight= (int) (mViewHeight*0.10);
        mLineHeight= (int) (mViewHeight*0.65);
        mSpaceHeight= (int) (mViewHeight*0.06);
        mDateHeight= (int) (mViewHeight*0.12);
        mSpaceWidth= (int) (DeviceUtils.getWindowWidth(mContext)/7.5);

    }

    /**
     * 绘制日期，如果当前为选中状态就绘制选中时的边框
     * @param canvas 画布
     */
    private void drawDate(Canvas canvas) {

        mPaintDate.setTextAlign(Paint.Align.CENTER);
        mPaintDate.setStrokeWidth(1);
        mPaintDate.setColor(mTextGray);
        mPaintDate.setTextSize(mTextDateSize);
        mPaintDate.setAntiAlias(true);
        //得到日期文本的宽高
        Rect bounds = new Rect();
        mPaintDate.getTextBounds(bean.get(0).getDate(),0,bean.get(0).getDate().length(),bounds);
        float textWidth=bounds.width();
        float textHeight=bounds.height();
        float start=mSpaceWidth*3/4;
        float height= mViewHeight - mDateHeight/2;
        for (int i=0;i<bean.size();i++){
//            Log.i("TAG","当前的View"+i+"\n"+"横坐标"+(start+i*mSpaceWidth));
            //当前是选中的日期就绘制成选中状态
            if (i==mCurrent){
                mPaintDate.setColor(mRed);
                mPaintDate.setStyle(Paint.Style.STROKE);
                mPaintDate.setStrokeWidth(2);
                RectF rectf=new RectF(start+i*mSpaceWidth-textWidth/2-textHeight/2,height-3*textHeight/2,start+i*mSpaceWidth+textWidth/2+textHeight/2,height+textHeight/2);
                //绘制空心圆角矩形，第二个和第三个参数决定了圆角的大小
                canvas.drawRoundRect(rectf,textHeight,textHeight,mPaintDate);
                mPaintDate.setStyle(Paint.Style.FILL);
                mPaintDate.setStrokeWidth(1);
                canvas.drawText(bean.get(i).getDate(),start+i*mSpaceWidth,height,mPaintDate);
            }else {
                mPaintDate.setColor(mTextGray);
                mPaintDate.setStyle(Paint.Style.FILL);
                mPaintDate.setStrokeWidth(1);
                canvas.drawText(bean.get(i).getDate(),start+i*mSpaceWidth,height,mPaintDate);
            }

        }
        mPaint.setColor(mLineGray);
        mPaint.setStrokeWidth(2);
        canvas.drawLine(mSpaceWidth / 2, mViewHeight - mDateHeight - mSpaceHeight, mSpaceWidth * 3 / 4 + (mPositionHeight.size() - 1) * mSpaceWidth + mSpaceWidth / 2, mViewHeight - mDateHeight - mSpaceHeight, mPaint);


    }

    /**
     * 绘制折线区域
     * @param canvas 画布
     */
    private void drawLineArea(Canvas canvas) {
        mPaint.setColor(mLineGray);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        float start=mSpaceWidth*3/4;
        float height=mViewHeight-mDateHeight-mSpaceHeight;
        //重置路径
        mLinePath.reset();
        mRedPath.reset();
        //计算路径
        mLinePath.moveTo(start, height);
        for (int i=0;i<mPositionHeight.size();i++){
            mLinePath.lineTo(start+i*mSpaceWidth,height-mAnimatorValue*mPositionHeight.get(i));
            if (i==0){
                mRedPath.moveTo(start,height-mAnimatorValue*mPositionHeight.get(i));
            }else {
                mRedPath.lineTo(start+i*mSpaceWidth,height-mAnimatorValue*mPositionHeight.get(i));
            }
        }
        mLinePath.lineTo(start + (mPositionHeight.size() - 1) * mSpaceWidth, height);
        mLinePath.lineTo(start, height);
        //绘制阴影
        mShadePaint.setStyle(Paint.Style.FILL);
        int colorSweep[] = {Color.WHITE,mPink};
        float position[]={0.3f,0.5f};
        //x,y的起始和终点坐标决定了绘制的方向
        //(x0,y0)(x1,y1)两个坐标表示绘制区域的左上角和右下角
        //LinearGradient(x0,y0,x1,y1 color,position,paint) //从左往右绘制
        //LinearGradient(x0,y1,x1,y0 color,position,paint) //从右往左绘制
        //LinearGradient(x0,y0,x0,y1 color,position,paint) //从上往下绘制
        //LinearGradient(x1,y0,x1,y1 color,position,paint) //从下往上绘制
        LinearGradient lg=new LinearGradient(start,height,start,mTopLineHeight+mShowHeight,colorSweep,position, Shader.TileMode.MIRROR);
        mShadePaint.setShader(lg);
        canvas.drawPath(mLinePath, mShadePaint);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLineGray);
        canvas.drawPath(mLinePath, mPaint);
        //绘制折线
        mPaint.setColor(mRed);
        mPaint.setStrokeWidth(4);
        canvas.drawPath(mRedPath, mPaint);
        //绘制点
        for (int i=0;i<mPositionHeight.size();i++){
            mPaint.setStrokeWidth(2);
            mPaint.setColor(mLineGray);
            canvas.drawLine(start + i * mSpaceWidth, height - mAnimatorValue * mPositionHeight.get(i), start + i * mSpaceWidth, height, mPaint);
            mPaint.setStrokeWidth(4);
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(start + i * mSpaceWidth, height - mAnimatorValue * mPositionHeight.get(i), 6, mPaint);
            mPaint.setColor(mRed);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(start + i * mSpaceWidth, height - mAnimatorValue * mPositionHeight.get(i), 6, mPaint);
            //绘制显示文字
            if(i==mCurrent) {
                mPaint.setColor(mRed);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(start + i * mSpaceWidth, height - mAnimatorValue * mPositionHeight.get(i), 8, mPaint);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(mTextShowSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setStrokeWidth(1);
                mPaint.setStyle(Paint.Style.FILL);
//                Canvas bitmapCanvas=new Canvas(mShowBit);
////                bitmapCanvas.drawText();
//                bitmapCanvas.drawText(bean.get(i).getAmount(),start + i * mSpaceWidth, height - mAnimatorValue * mPositionHeight.get(i)-mShowBit.getHeight()/2-12,mPaint);
//                bitmapCanvas.drawCircle(mShowBit.getWidth()/2,mShowBit.getHeight(),20,mPaint);
                canvas.drawBitmap(mShowBit,start + i * mSpaceWidth-mShowBit.getWidth()/2,height - mAnimatorValue * mPositionHeight.get(i) - mShowBit.getHeight() - 15, mPaint);
                canvas.drawText(bean.get(i).getAmount(), start + i * mSpaceWidth, height - mAnimatorValue * mPositionHeight.get(i)-mShowBit.getHeight()/2-12,mPaint);
            }
        }

    }

    /**
     * 得到各个点的最终高度
     * @param bean 数据
     */
    private void getPosition(List<RecentlyIncomeBean> bean) {
        mPositionHeight.clear();
        float max = 0;
        for (RecentlyIncomeBean item : bean) {
            if (Float.parseFloat(item.getAmount()) > max) {
                max = Float.parseFloat(item.getAmount());
            }
        }
        LogUtils.e("max" + max);
        for (RecentlyIncomeBean item : bean) {
            //添加每个Item的垂直高度
            mPositionHeight.add((Float.parseFloat(item.getAmount()) / max * mLineHeight));
        }
    }

    /***
     * 绘制点击区域
     * 用于测试点击区域是否正确
     * @param canvas 画布
     */
    private void drawClickArea(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, mSpaceWidth * 5 / 4, mViewHeight - mDateHeight, paint);
        for (int i=1;i<bean.size();i++){
            if (i%2==0){
                paint.setColor(Color.GRAY);
            }else {
                paint.setColor(Color.LTGRAY);
            }
            canvas.drawRect(mSpaceWidth*5/4+(i-1)*mSpaceWidth,0,mSpaceWidth*5/4+i*mSpaceWidth,mViewHeight-mDateHeight,paint);
        }
    }

    /**
     * 设置数据
     * @param bean 数据
     */
    public void setBean(List<RecentlyIncomeBean> bean ) {
        if (bean.size()<=0){
//            throw  new IllegalArgumentException("Bean's size can’t be zero");
        }else {
            initListener();

            initHandler();

            initAnimator();
            mValueAnimator.start();
            this.bean = bean;
            mCurrent=bean.size()-1;
        }

//        postInvalidate();
        Log.i("TAG", "设置数据");
    }




    //View当前的位置
    private int rawX = 0;
    private int rawY = 0;
    //View之前的位置
    private int lastX = 0;
    private int lastY = 0;
    //View按下的初始位置
    private int startX=0;
    private int endX=0;
    //这里应该加上Scroller惯性滑动的处理，使得滑动变的平滑
    //注意，触摸事件的响应范围仅限于该View的区域
    public boolean onTouchEvent(MotionEvent event){
        //Log.e("onTouchEvent执行","true");
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.e("ACTION","down");
                //获取手指落下的坐标并保存
                rawX = (int)(event.getX());
                startX = (int)(event.getX());
                rawY = (int)(event.getY());
                lastX = rawX;
                lastY = rawY;
//                Log.e("TAG","ACTION_DOWN"+rawX);
//                Log.e("TAG","ACTION_DOWNstartX"+startX);
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("ACTION","move");
                //手指拖动时，获得当前位置
                rawX = (int)event.getX();
                rawY = (int)event.getY();
                //限定移动范围
                //手指移动的x轴和y轴偏移量分别为当前坐标-上次坐标
                int offsetX = rawX - lastX;
                if (offsetX>mSpaceWidth/2&&getLeft()<=0&&bean.size()>7){//向右移动
                    Log.e("TAG", "向右移动getLeft" + getLeft());
                    if (offsetX>Math.abs(getLeft())){
                        offsetLeftAndRight(Math.abs(getLeft()));
                    }else {
                        offsetLeftAndRight(offsetX);
                    }

                    lastX = rawX;
                    lastY = rawY;
//                    Log.e("TAG", "Right+++++rawX=====" + rawX);
//                    Log.e("TAG", "Right+++++offsetX=====" + offsetX);
//                    Log.e("TAG","Right+++++getLeft"+getLeft());
//                    Log.e("TAG","Right+++++getRight" +getRight());
                }else if (bean.size()>7&&offsetX<-mSpaceWidth/2&&(getRight()>(DeviceUtils.getWindowWidth(mContext)))) {//向左移动
                    Log.e("TAG", "向左移动getRight" + getRight());
                    Log.e("TAG", "向左移动getLeft" + getLeft());
                    offsetLeftAndRight(offsetX);
                    lastX = rawX;
                    lastY = rawY;

                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "离开屏幕" + getLeft());
                endX = (int)event.getX();
                if (-mSpaceWidth/2<(endX-startX)&&(endX-startX)<mSpaceWidth/2&&bean.size()>0){
                    int centerX=(endX+startX)/2;
                    if (centerX<(mSpaceWidth*5/4)){
//                        Toast.makeText(mContext,""+bean.get(0).getDate(),Toast.LENGTH_SHORT).show();
                        mCurrent=0;
                    }else if (centerX>(bean.size()*mSpaceWidth+mSpaceWidth*1/4)){
//                        Toast.makeText(mContext,""+bean.get(bean.size()-1).getDate(),Toast.LENGTH_SHORT).show();
                        mCurrent=bean.size()-1;
                    }else {
//                        Toast.makeText(mContext,""+bean.get((centerX-mSpaceWidth*1/4)/mSpaceWidth).getDate(),Toast.LENGTH_SHORT).show();
                        mCurrent=(centerX-mSpaceWidth*1/4)/mSpaceWidth;
                    }
                    postInvalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 设置动画进度
     * @param mAnimatorValue 进度值【0~1】
     */
    public void setmAnimatorValue(float mAnimatorValue) {
        this.mAnimatorValue = mAnimatorValue;
        postInvalidate();
    }
    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
//                Log.i("TAG", "mAnimatorValue="+mAnimatorValue);
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // getHandle发消息通知动画状态更新
                mAnimatorHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initHandler() {
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mValueAnimator.removeAllUpdateListeners();
                mValueAnimator.removeAllListeners();
            }
        };
    }

    private void initAnimator() {
        mValueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);

        mValueAnimator.addUpdateListener(mUpdateListener);

        mValueAnimator.addListener(mAnimatorListener);
    }

}
