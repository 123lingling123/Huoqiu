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
 * Created by ZhongHang on 2015/12/2.
 */
public class MyProgress extends View {
    private String[] texts={"普通用户","铜牌用户","银牌用户","金牌用户","钻石用户"};
    private int mWidth;
    private int mHeight;
    /**普通用户图片*/
    private Bitmap mBitmap1;
    //普通用户图片画布
    private Canvas mCanvas1;
    private Path mPathArc;
    /**普通用户图片*/
    private Bitmap mBitmap2;
    //普通用户图片画布
    private Canvas mCanvas2;
    private Path mPath2;
    private Paint mPaintGray;
    private Paint mPaintWhite;
    private int progress= 15;
    private int textSize= 25;
    private Paint mPaintRed;
    private Paint mPaintGreen;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    private Paint mPaintBlue;
    public MyProgress(Context context) {
        super(context);
    }

    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintGray=new Paint();
        mPaintGray.setColor(Color.GRAY);
        mPaintWhite=new Paint();
        mPaintWhite.setColor(Color.WHITE);
        mPaintWhite.setTextSize(textSize);
        mPaintWhite.setTextAlign(Paint.Align.CENTER);
        mPaintRed=new Paint();
        mPaintRed.setColor(Color.RED);
        mPaintRed.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mPaintGreen=new Paint();
        mPaintGreen.setColor(Color.GREEN);
        mPaintBlue =new Paint();
        mPaintBlue.setColor(Color.BLUE);
    }

    public MyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        mBitmap1 = Bitmap.createBitmap(mWidth /   5, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas1 =new Canvas(mBitmap1);
        mPathArc =new Path();
        mPathArc.addArc(new RectF(  0,   0, mHeight, mHeight),  90, 270);//左半圆，其实是一个180°的扇形
        mPathArc.addRect(mHeight /   2,   0, mWidth /   5, mHeight, Path.Direction.CW);

        mBitmap2 = Bitmap.createBitmap(mWidth /   5, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas2 =new Canvas(mBitmap2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画普通用户灰色背景 带有半圆的，此处使用的一个Bitmap，同钻石用户一样
        mCanvas1.drawPath(mPathArc, mPaintGray);
        //画上层颜色
        mCanvas1.drawRect(  0,   0, progress * mWidth / 100, mHeight, mPaintRed);
        //将此Bitmap绘制到canvas上
        canvas.drawBitmap(mBitmap1,   0,   0, null);

        //画铜牌用户灰色背景
        canvas.drawRect(mWidth /   5 +   2,   0, mWidth /   5 *   2, mHeight, mPaintGray);

        //画银牌用户灰色背景
        canvas.drawRect(mWidth/  5*  2+  2,  0,mWidth/  5*  3,mHeight,mPaintGray);

        //画金牌用户灰色背景
        canvas.drawRect(mWidth /   5 *   3 +   2,   0, mWidth /   5 *   4, mHeight, mPaintGray);
        if(progress> 20&&progress<= 40){
            //绘制铜牌用户变色区域
            canvas.drawRect(mWidth /   5 +   2,   0, mWidth /   5+mWidth*(progress- 20)/100+  2, mHeight, mPaintGreen);
        }
        if(progress> 40&&progress<= 60){
            //铜牌区域全部变色
            canvas.drawRect(mWidth /   5 +   2,   0, mWidth /   5 *   2, mHeight, mPaintGreen);

            //绘制银牌用户变色区域
            canvas.drawRect(mWidth /   5 *  2+   2,   0, mWidth /   5*  2+mWidth*(progress- 40)/100+  2, mHeight, mPaintBlue);
        }
        if(progress> 60&&progress<= 80){
            //铜牌区域全部变色
            canvas.drawRect(mWidth /   5 +   2,   0, mWidth /   5 *   2, mHeight, mPaintGreen);
            //银牌区域全部变色
            canvas.drawRect(mWidth/  5*  2+  2,  0,mWidth/  5*  3,mHeight,mPaintBlue);
            //绘制金牌用户变色区域
            canvas.drawRect(mWidth /   5 *   3 +   2,   0, mWidth /   5 *   3+mWidth*(progress- 60)/100+  2, mHeight, mPaintGreen);
        }
        //画钻石用户灰色背景，这里还是使用了那个半圆的path，普通用户是左边半圆，钻石用户旋转180变成右边半圆
        mCanvas2.save();
        mCanvas2.rotate(180, mWidth /  10, mHeight /   2);
        mCanvas2.drawPath(mPathArc, mPaintGray);//绘制钻石用户背景
        mCanvas2.restore();
        if(progress> 80&&progress<=100){
            //铜牌区域全部变色
            canvas.drawRect(mWidth /   5 +   2,   0, mWidth /   5 *   2, mHeight, mPaintGreen);
            //银牌区域全部变色
            canvas.drawRect(mWidth/  5*  2+  2,  0,mWidth/  5*  3,mHeight, mPaintBlue);
            //绘制金牌全部变色
            canvas.drawRect(mWidth /   5 *   3 +   2,   0, mWidth /   5 *   4, mHeight, mPaintGreen);

            //绘制钻石用户上层颜色
            mCanvas2.drawRect(  0,   0, (progress -  80) * mWidth / 100, mHeight, mPaintRed);
        }
        //将此Bitmap绘制到canvas上
        canvas.drawBitmap(mBitmap2, mWidth /   5 *   4 +   2,   0, null);
//        canvas.drawLine(mWidth/2,0,mWidth/2,mHeight,mPaintRed);
        for(int i=  0;i<  5;i++) {
            canvas.drawText(texts[i],mWidth/  5*i+mWidth/ 10,mHeight/  2+textSize/  2,mPaintWhite);
        }
    }
}
