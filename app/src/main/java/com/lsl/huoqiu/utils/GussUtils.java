package com.lsl.huoqiu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Forrest on 16/5/12.
 */
public class GussUtils {
    public static void setGussImage(  Context context ,int imageId, final View view){
        final ImageView image=new ImageView(context);
        image.setImageResource(imageId);
//        Bitmap bmp = image.getDrawingCache();
//        return blur(context,bmp, view);
        Log.d("GussUtils","进入方法");
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();
                Bitmap bmp = image.getDrawingCache();
                blur(bmp, view);
                Log.d("GussUtils", "结束");
                return true;
            }
        });


//        return blur(bmp, view);
    }
    public static BitmapDrawable setGussBitmap(  Context context ,int imageId,  View view){
        ImageView image=new ImageView(context);
        image.setImageResource(imageId);
        Bitmap bmp = ((BitmapDrawable)image.getDrawable()).getBitmap();
        return blur2(context, bmp, view);

    }
    private static void blur( Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 2;
//        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 2;
//        }
        Log.d("GussUtils","准备开始高斯");
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        Log.d("GussUtils", "高斯模糊结束");
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
//        return new BitmapDrawable(overlay);
        view.setBackground(new BitmapDrawable(overlay));
//        return  new BitmapDrawable(context.getResources(), overlay);
//        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    private static BitmapDrawable blur2( Context context,Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 2;
//        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 2;
//        }
        Log.d("GussUtils","准备开始高斯");
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);

        Log.d("GussUtils","准备开始高斯"+view.getMeasuredWidth());
        Log.d("GussUtils","准备开始高斯"+view.getMeasuredHeight());
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        Log.d("GussUtils", "高斯模糊结束");
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
//        return new BitmapDrawable(overlay);
//        view.setBackground(new BitmapDrawable(overlay));
        Log.d("GussUtils",(System.currentTimeMillis() - startMs) + "ms");
        return  new BitmapDrawable(context.getResources(), overlay);

    }
}
