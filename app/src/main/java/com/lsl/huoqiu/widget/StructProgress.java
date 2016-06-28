package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.StructBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/6/28.
 */
public class StructProgress extends View {
    private List<StructBean> beans;//传入的数据
    private Paint mPaint;//普通画笔
    private int bgColor;//进度条背景颜色
    private int rightColor;//渐变条右侧颜色
    private int leftColor;//渐变条左侧颜色
    private int redTextColor;//红色文字颜色
    private int grayTextColor;//灰色文字颜色
    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private Canvas mCanvas1;//新建画布，目的是为了画出椭圆形
    private int progressHeight;//进度条高度
    private Bitmap Bitmap1;//承载画布的bitmap
    private Path mPathArc;//圆圈的路径
    private Path mPahtRecf;//长方形的路径
    private Paint mPaintRed;//进度条画笔
    private float progress;//进度值
    private float redTextWidth;//红色文字的大小
    private float grayTextWidth;//灰色文字的大小
    private float grayLineHeigth;//灰色线条颜色
    private float location;//各个文字所在的位置
    private float space;//文字和线条和进度条之间的距离
    private float textWidth;
    public StructProgress(Context context) {
        super(context);
        initView(context);
    }

    public StructProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StructProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        mPaint = new Paint();
        bgColor = context.getResources().getColor(R.color.struct_backgroud);
        rightColor = context.getResources().getColor(R.color.struct_right_red);
        leftColor = context.getResources().getColor(R.color.struct_left_red);
        redTextColor = context.getResources().getColor(R.color.struct_text_red);
        grayTextColor = context.getResources().getColor(R.color.struct_text_gray);
        mPaintRed=new Paint();
        beans=new ArrayList<StructBean>();
        redTextWidth = context.getResources().getDimension(R.dimen.sp15);
        grayTextWidth = context.getResources().getDimension(R.dimen.sp10);
        grayLineHeigth = context.getResources().getDimension(R.dimen.dp10);
        space = context.getResources().getDimension(R.dimen.dp3);
        textWidth = context.getResources().getDimension(R.dimen.dp1);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到控件的宽度，-40目的是为了让右边的文字显示出来
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec)-40;
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        //进度条的高度
        progressHeight=mHeight/6;
        Bitmap1= Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas1 =new Canvas(Bitmap1);
        //进度条的背景
        mPathArc =new Path();
        mPathArc.addArc(new RectF(0, mHeight-progressHeight, progressHeight, mHeight), 90, 270);//左半圆，其实是一个180°的扇形
        mPathArc.addRect(progressHeight / 2, mHeight-progressHeight, mWidth - progressHeight / 2, mHeight, Path.Direction.CW);
        mPathArc.addArc(new RectF(mWidth - progressHeight, mHeight-progressHeight, mWidth, mHeight), 180, 360);//右半圆，其实是一个180°的扇形



    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(bgColor);
        mCanvas1.drawPath(mPathArc, mPaint);
        mPahtRecf=new Path();
        float max=getMax(beans);
        //绘制文字并且找到要凸显的进度
        if (beans.size()>0){
            for (StructBean bean:beans) {
                if (bean.isCurrent()){
                    progress = bean.getRate()/max;
                    mPaint.setTextSize(redTextWidth);
                    mPaint.setColor(redTextColor);
                }else {
                    mPaint.setTextSize(grayTextWidth);
                    mPaint.setColor(grayTextColor);
                }
                location=bean.getRate()/max;
                mPaint.setTextAlign(Paint.Align.CENTER);//居中显示
                mPaint.setStrokeWidth(textWidth);//设置线条的宽度
                //为了防止文字太靠左边出现的显示问题
                if (location*100>6){
                    canvas.drawLine(location*mWidth,mHeight-progressHeight-space,location*mWidth,mHeight-progressHeight-space-grayLineHeigth,mPaint);
                    canvas.drawText(bean.getRate()+"%",location * mWidth,mHeight-progressHeight-2*space-grayLineHeigth,mPaint);
                }else {
                    canvas.drawLine(location*mWidth,mHeight-progressHeight-space,location*mWidth,mHeight-progressHeight-space-grayLineHeigth,mPaint);
                    canvas.drawText(bean.getRate() + "%", progressHeight, mHeight-progressHeight-2*space-grayLineHeigth, mPaint);
                }
            }
        }else {
            //将进度设为0
            progress=0;
        }
        //画覆盖在上面的进度条
        mPahtRecf.addRect(0, mHeight-progressHeight, progress * mWidth -progressHeight/2, mHeight, Path.Direction.CW);
        mPahtRecf.addArc(new RectF(progress * mWidth -progressHeight,mHeight-progressHeight,progress * mWidth,mHeight),180,360);
        LinearGradient lg=new LinearGradient(0,mHeight-progressHeight,  progress * mWidth,mHeight,leftColor,rightColor, Shader.TileMode.CLAMP);
        mPaintRed.setShader(lg);//设置渐变色
        mPaintRed.setColor(rightColor);
        mPaintRed.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));//
        mCanvas1.drawPath(mPahtRecf, mPaintRed);
        //将此Bitmap绘制到canvas上
        canvas.drawBitmap(Bitmap1, 0, 0, null);


    }

    public void setData(List<StructBean> beans){
        this.beans=beans;
        invalidate();
    }
    private float getMax(List<StructBean> beans){
        float max=0;
        for (StructBean bean: beans) {
            if (bean.getRate()>max){
                max=bean.getRate();
            }
        }
        return max;
    }
}
