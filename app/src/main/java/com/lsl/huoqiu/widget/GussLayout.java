package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.huoqiu.utils.FastBlur;

/**
 * Created by Forrest on 16/5/12.
 */
public class GussLayout extends RelativeLayout {
    public   float scaleFactor = 8;//缩放程度
    public  float radius = 10;//模糊程度

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public GussLayout (Context context){
        super(context);
    }

    public GussLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GussLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setImageBack( final View view){
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                view.buildDrawingCache();
                Bitmap bmp = view.getDrawingCache();
                blur(bmp, view);
                return true;
            }
        });

    }

    public void setGussLayout( final View view, final View layout){
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                view.buildDrawingCache();
                Bitmap bmp = view.getDrawingCache();
                blurLayout(bmp, layout);
                return true;
            }
        });

    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Log.d("GussUtils", "准备开始高斯" + view.getMeasuredWidth());
        Log.d("GussUtils", "准备开始高斯" + view.getMeasuredHeight());
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        Log.d("GussUtils", (System.currentTimeMillis() - startMs) + "ms");
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }

    private void blurLayout(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//缩放程度
        float radius = 30;//模糊程度
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Log.d("GussUtils", "准备开始高斯" + view.getMeasuredWidth());
        Log.d("GussUtils", "准备开始高斯" + view.getMeasuredHeight());
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        Log.d("GussUtils", (System.currentTimeMillis() - startMs) + "ms");
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }
}
