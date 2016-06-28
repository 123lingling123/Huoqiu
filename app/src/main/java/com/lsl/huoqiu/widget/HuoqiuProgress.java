package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by macbook on 16/3/25.
 */
public class HuoqiuProgress extends View {
    private int mWidth;
    private int mHeight;
    private Bitmap Bitmap1;
    //普通用户图片画布
    private Canvas mCanvas1;
    private Path mPathArc;
    private Paint mPaintGray;
    private Paint mPaintRed;
    private int progress= 0;
    private Path mPahtRecf;
    private Paint mPaintText;
    private int progressHeight;
    public HuoqiuProgress(Context context) {
        super(context);
    }

    public HuoqiuProgress(Context context, AttributeSet attrs) {
        super(context, attrs);



        mPaintGray=new Paint();
        mPaintGray.setColor(Color.GRAY);
        mPaintRed=new Paint();
        mPaintRed.setColor(Color.RED);
        mPaintRed.setStrokeWidth(5);
        mPaintRed.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mPaintText=new Paint();
        mPaintText.setColor(Color.GRAY);
        mPaintText.setTextSize(20);
        mPaintText.setTextAlign(Paint.Align.RIGHT);

    }

    public HuoqiuProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        progressHeight=mHeight/3;
        Bitmap1=Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas1 =new Canvas(Bitmap1);

        mPathArc =new Path();
        mPathArc.addArc(new RectF(0, mHeight-progressHeight, progressHeight, mHeight), 90, 270);//左半圆，其实是一个180°的扇形
        mPathArc.addRect(progressHeight / 2, mHeight-progressHeight, mWidth - progressHeight / 2, mHeight, Path.Direction.CW);
        mPathArc.addArc(new RectF(mWidth - progressHeight, mHeight-progressHeight, mWidth, mHeight), 180, 360);//右半圆，其实是一个180°的扇形

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas1.drawPath(mPathArc, mPaintGray);
        mPahtRecf=new Path();
        if (progress>0){
            mPahtRecf.addRect(0, mHeight-progressHeight, progress * mWidth / 100-progressHeight/2, mHeight, Path.Direction.CW);
            mPahtRecf.addArc(new RectF(progress * mWidth / 100-progressHeight,mHeight-progressHeight,progress * mWidth / 100,mHeight),180,360);
        }else {
            mPahtRecf.addArc(new RectF(progress * mWidth / 100-progressHeight,mHeight-progressHeight,progress * mWidth / 100,mHeight),180,360);
        }

        mCanvas1.drawPath(mPahtRecf, mPaintRed);
        //将此Bitmap绘制到canvas上
        canvas.drawBitmap(Bitmap1, 0, 0, null);
//        canvas.drawPoint(mWidth / 2, mHeight / 2, mPaintRed);
        if (progress>6){
            canvas.drawText(progress+"%",progress * mWidth / 100,mHeight-progressHeight-10,mPaintText);
        }else {
            canvas.drawText(progress+"%",progressHeight,mHeight-progressHeight-10,mPaintText);
        }


//        canvas.drawPath(mPahtRecf, mPaintRed);
    }
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
