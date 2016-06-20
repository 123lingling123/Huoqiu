package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.lsl.huoqiu.R;

/**
 * Created by Forrest on 16/5/26.
 */
public class DialogView extends View {
    private int positionX = 0;
    private int positionY = 0;
    private Bitmap bmp;
    private Bitmap bmp2;

    public DialogView(Context context) {
        super(context);
        init(context);
    }

    public DialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.mine_small_meng_copy);
        bmp2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.mine_small_tips);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建背景
        Bitmap bitcircle = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvascicle = new Canvas(bitcircle);
        Paint paintcicle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintcicle.setColor(0xAA000000);
        canvascicle.drawRect(0, 0, getWidth(), getHeight(), paintcicle);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置Mode
        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP);

        //采用saveLayer，让后续canvas的绘制在自动创建的bitmap上
        int cnt = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //先画圆形，圆形是dest
        canvas.drawBitmap(bitcircle, 0, 0, paint);
        paint.setXfermode(xfermode);
        //后画矩形，矩形是src
        canvas.drawBitmap(bmp, positionX, positionY, paint);
        paint.setXfermode(null);
        canvas.drawBitmap(bmp2, positionX+bmp.getWidth()*3/10 , positionY+ bmp.getHeight(), paint);
        canvas.restoreToCount(cnt);

    }

    public void setPositionX(int positionX ,int positionY) {
        this.positionX = positionX;
        this.positionY=positionY;
        postInvalidate();
    }
}
