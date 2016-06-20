package com.lsl.huoqiu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.lsl.huoqiu.R;

/**
 * Created by Forrest on 16/4/22.
 */
public class LabelRadioButton extends RadioButton {
    private int textColor;
    private int backColor;
    private float textSize;
    private float backSize;
    private Paint paint;
    private boolean isVisible;

    public LabelRadioButton(Context context) {
        super(context);
    }

    public LabelRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LabelRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LabelRadioButton);
        textColor = array.getColor(R.styleable.LabelRadioButton_label_textColor, Color.WHITE);
        backColor = array.getColor(R.styleable.LabelRadioButton_label_backColor, Color.RED);
        textSize = array.getInt(R.styleable.LabelRadioButton_label_textSize, 20);
        backSize = array.getInt(R.styleable.LabelRadioButton_label_backSize, 25);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isVisible) {
            int y = 0;
            int x = 0;
            Drawable[] drawables = getCompoundDrawables();// 得到Drawable图片
            if (drawables != null) {
                Log.d("LabelRadioButton", "宽" + getWidth() + "++++++高" + getHeight());
                Drawable drawableRight = drawables[1];// 上边的图片
                if (drawableRight != null) {
                    Rect rectF = new Rect();
                    getPaint().getTextBounds(getText().toString(), 0, getText().length(), rectF);
                    Log.d("LabelRadioButton", "textHeight" + rectF.height());
                    float textHeight = rectF.height();
                    float textWidth = getPaint().measureText(getText().toString());// 测量文本的宽度
                    Log.d("LabelRadioButton", "textWidth" + textWidth);
                    int drawablePadding = getCompoundDrawablePadding();// Drawable的padding的值（图片的Padding）
                    int drawableWidth = 0;
                    int drawableHeight = 0;
                    drawableWidth = drawableRight.getIntrinsicWidth();// Drawable的宽度（图片）
                    drawableHeight = drawableRight.getIntrinsicHeight();// Drawable的高度（图片）
                    Log.d("LabelRadioButton", "drawableWidth" + drawableWidth);
                    Log.d("LabelRadioButton", "drawableHeight" + drawableHeight);
                    //原点圆心坐标
                    y = (int) ((getHeight() - drawableHeight - textHeight) / 2 + backSize / 4);
                    x = (int) ((getWidth() - (textWidth > drawableWidth ? textWidth : drawableWidth)) / 2 + backSize / 5 + (textWidth > drawableWidth ? textWidth : drawableWidth));
                    Log.d("LabelRadioButton", "y" + y);
                    Log.d("LabelRadioButton", "x" + x);
                    Log.d("LabelRadioButton", "backSize/2" + backSize / 2);
                    paint.setColor(backColor);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE); //设置实心
                    paint.setAntiAlias(true);


                    canvas.drawCircle(x, y, backSize / 2, paint);


//                float bodyWidth = textWidth + drawableWidth + drawablePadding;// 设置总的宽度
//                setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);// 将内容移到容器的最左边
//                canvas.translate((getWidth() - bodyWidth) / 2, 0);// 画布平移
                }
            }
        }
    }

    public synchronized void setLabelVisible() {
        isVisible = true;
        postInvalidate();
    }

    public synchronized void setLabelGone() {
        isVisible = false;
        postInvalidate();
    }

}
