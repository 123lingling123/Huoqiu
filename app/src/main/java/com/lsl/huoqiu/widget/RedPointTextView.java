package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Forrest on 16/7/7.
 */
public class RedPointTextView extends TextView{


    public RedPointTextView(Context context) {
        super(context);
    }

    public RedPointTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RedPointTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x=getMeasuredWidth();
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(x-getPaddingRight(),getPaddingTop(),10,paint);
        super.onDraw(canvas);



    }


}
