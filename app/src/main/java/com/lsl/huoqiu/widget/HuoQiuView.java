package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.lsl.huoqiu.R;

/**
 * Created by Forrest on 16/5/20.
 */
public class HuoQiuView extends View {
    private Paint paint;//画笔
    private int width;//控件宽度
    private int height;//控件高度
    private float strokeWidth;//最外层透明度条的宽度
    private float smallCircleRadius;//最外层白色小球半径
    private float transpanceWidth;//透明度外圈的宽度
    private float angele = 150;
    private float startAngele = 180;
    private float floatAngele = 9;

    private int transpacneColor;
    private int redColor;
    private int transparent;
    private int transparent_white;
    private int transparent_white_deep;
    private Paint paintSweep;

    private Bitmap bmp;
    public void setHeight(int height) {
        this.height = height;
        invalidate();
    }

    public HuoQiuView(Context context) {
        super(context);
        initView(context);
    }

    public HuoQiuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }


    public HuoQiuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        paint = new Paint();
        paintSweep=new Paint();
        //圆环宽度
        strokeWidth = context.getResources().getDimension(R.dimen.dp3);
//        smallCircleRadius = context.getResources().getDimension(R.dimen.dp3);
        transpanceWidth = context.getResources().getDimension(R.dimen.dp20);
        transpacneColor = context.getResources().getColor(R.color.backcolor);
        redColor = context.getResources().getColor(R.color.huoqiuSmall);
        transparent = context.getResources().getColor(R.color.transparent);
        transparent_white = context.getResources().getColor(R.color.transparent_white);
        transparent_white_deep = context.getResources().getColor(R.color.transparent_white_deep);
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(strokeWidth);

        bmp= BitmapFactory.decodeResource(context.getResources(),R.mipmap.white_round);
        smallCircleRadius=bmp.getHeight()/2;
    }

    public boolean isFinish = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth(); //获取宽度
//        height=getHeight();//获取高度
        //画中心白色的圆
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        float backRadius = (height - smallCircleRadius * 2 - transpanceWidth * 2 - strokeWidth * 2) / 2;//中心白园半径
        canvas.drawCircle(width / 2, height / 2, backRadius, paint);
        //画透明色的圆环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(transpacneColor);
//        paint.setARGB(0x11,0x00,0x00,0x00);
        paint.setAlpha(100);
        paint.setStrokeWidth(transpanceWidth);
//        float transpanecRadius=height/2-smallCircleRadius-strokeWidth-transpanceWidth/2;//透明圆环半径
        float transpanecRadius = backRadius + transpanceWidth / 2;//透明圆环半径
        canvas.drawCircle(width / 2, height / 2, transpanecRadius, paint);

        //画最外面的白条
        float left = width / 2 - height / 2 + strokeWidth+smallCircleRadius;//透明度变化白条矩形框左边;;+smallCircleRadius/2调节左边距离
        float top = smallCircleRadius + strokeWidth;//透明度变化白条矩形框上边
        float right = width / 2 + height / 2 - strokeWidth-smallCircleRadius;//透明度变化白条矩形框右边
        float down = height - strokeWidth-smallCircleRadius;//透明度变化白条矩形框下边
        RectF oval = new RectF(left, top, right, down);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(strokeWidth);
//        paint.setColor(Color.WHITE);
        paintSweep.setStyle(Paint.Style.STROKE);
        paintSweep.setStrokeWidth(strokeWidth);
        paintSweep.setColor(Color.WHITE);

        int colorSweep[] = { transparent, transparent_white,transparent_white_deep,Color.WHITE};//Color.GREEN, Color.RED, Color.BLUE,
        float position[]={0.3f,0.6f,0.7f,0.8f };//0.0f,0.2f,0.4f,
        SweepGradient sweepGradient=new SweepGradient(width / 2, height / 2, colorSweep, position);
//        paintSweep.setShader(new LinearGradient(0, 0, 400, 400, Color.RED, Color.BLUE, Shader.TileMode.MIRROR));
        paintSweep.setShader(sweepGradient);
        canvas.drawArc(oval, startAngele-floatAngele , floatAngele+angele, false, paintSweep);

//        for (int i = 0; i < angele; i++) {
//            paint.setAlpha((int) (255 * i / angele));
//
//            canvas.drawArc(oval, startAngele + i, 1, false, paint);
//        }

        //画跟随运动的白点
//        double formWidth=width/2-height/2+smallCircleRadius+strokeWidth/2;
        double formHeight = height / 2 - smallCircleRadius - strokeWidth / 2;//白点所在圆环半径
        paint.setColor(Color.WHITE);
        paint.setAlpha((int) (angele));
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(255);

        //跟着角度变换找圆心
        if (angele <= 90) {
            double smallCenterX = width / 2 - Math.cos(Math.toRadians(angele)) * formHeight;
            double smallCenterY = height / 2 - Math.sin(Math.toRadians(angele)) * formHeight;
            canvas.drawBitmap(bmp,(int)smallCenterX-bmp.getWidth()/2,(int)smallCenterY-bmp.getHeight()/2,paint);
//            paint.setAlpha(100);
//            canvas.drawCircle((float) smallCenterX, (float) smallCenterY, smallCircleRadius, paint);
//            paint.setAlpha(255);
//            canvas.drawCircle((float) smallCenterX, (float) smallCenterY, smallCircleRadius/2, paint);
        } else {
            double smallCenterBigX = width / 2 + Math.sin(Math.toRadians(angele - 90)) * formHeight;
            double smallCenterBigY = height / 2 - Math.cos(Math.toRadians(angele - 90)) * formHeight;
            canvas.drawBitmap(bmp,(int)smallCenterBigX-bmp.getWidth()/2,(int)smallCenterBigY-bmp.getHeight()/2,paint);
//            paint.setAlpha(100);
//            canvas.drawCircle((float) smallCenterBigX, (float) smallCenterBigY, smallCircleRadius, paint);
//            paint.setAlpha(255);
//            canvas.drawCircle((float) smallCenterBigX, (float) smallCenterBigY, smallCircleRadius / 2, paint);
        }

        //画红色的小圆
        paint.setColor(redColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        float redRadius = backRadius * 3 / 10 - 5;//红色圆圈半径
        float redCenterY = height / 2 + backRadius - redRadius;//红色圆圈圆心Y
//        13/10*13/10-7/10*7/10;
        float redCenterX = (float) (width / 2 + Math.sqrt(1.2) * backRadius);//红色圆圈圆心X
        canvas.drawCircle(redCenterX, redCenterY, redRadius, paint);
        canvas.save();
        paint.setStrokeWidth(1);
        //画本月分红
        if (!TextUtils.isEmpty(textDes)) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(35);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(textDes, redCenterX, redCenterY-20, paint);
        }

        if (!TextUtils.isEmpty(money)) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(money, redCenterX, redCenterY+40, paint);
        }
    }

    public void setAngle(int angele) {
        this.angele = angele;
        invalidate();
    }

    private String textDes;
    private String money;

    public void setTextDes(String text) {
        this.textDes = text;
        invalidate();
    }
    public void setMoney(String money){
        this.money=money;
        invalidate();
    }
}
