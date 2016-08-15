package com.lsl.huoqiu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.lsl.huoqiu.R;


/**
 * 用户击败百分比图
 * Created by Forrest on 16/8/12.
 */
public class PercentView extends View{
    private Paint paint;//画笔
    private Paint shaderPaint;//彩色画笔
    private Paint bitmapPaint;//图片画笔
    private Paint textPaint;//文字画笔
    /**控件宽度*/
    private int width;
    /**控件高度*/
    private int height;
    /**半径*/
    private int radius;
    /**外圆弧的宽度*/
    private float outerArcWidth;
    /**内部大圆弧的宽度*/
    private float insideArcWidth;
    /**两圆弧中间间隔距离*/
    private float spaceWidth;
    /**两圆弧中间间隔距离*/
    private float percentTextSize;
    /**最外层滑动小球的半径*/
    private float scrollCircleRadius;
    /**粉红底色*/
    private int pinkColor;
    /**黄色*/
    private int yellowColor;
    /**粉色红*/
    private int pinkRedColor;
    /**浅红*/
    private int redColor;
    /**深红*/
    private int deepRedColor;
    /**灰色*/
    private int grayColor;
    /**间隔的角度*/
    private double spaceAngle=22.5;
    /**两条圆弧的起始角度*/
    private double floatAngel=20;
    /**自定义的Bitmap*/
    private Bitmap mBitmap;
    /**自定义的画布，目的是为了能画出重叠的效果*/
    private Canvas mCanvas;
    /**时刻变化的Angel*/
    private double mAngel;
    /**内弧半径*/
    private float insideArcRadius;
    private double aimPercent=0;
    private float outerArcRadius;
    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmapBackDeepRed;  // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    private RectF outerArea;            //外圈的矩形
    private String tag;
    private String aim;
    private int textSizeTag;//名列前茅字体大小
    private int textSizeAim;//击败百分比字体大小
    public PercentView(Context context) {
        super(context);
        initView(context);

    }

    public PercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public PercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context){
        shaderPaint=new Paint();
        textPaint=new Paint();

        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        bitmapPaint=new Paint();
        bitmapPaint.setStyle(Paint.Style.FILL);
        bitmapPaint.setAntiAlias(true);

        outerArcWidth = context.getResources().getDimensionPixelOffset(R.dimen.dp2);
        insideArcWidth = context.getResources().getDimensionPixelOffset(R.dimen.dp12);
        spaceWidth = context.getResources().getDimensionPixelOffset(R.dimen.dp12);
        scrollCircleRadius = context.getResources().getDimensionPixelOffset(R.dimen.dp4);
        percentTextSize = context.getResources().getDimensionPixelOffset(R.dimen.dp8);
        textSizeAim = context.getResources().getDimensionPixelOffset(R.dimen.sp15);
        textSizeTag = context.getResources().getDimensionPixelOffset(R.dimen.sp30);
        pinkColor = context.getResources().getColor(R.color.percent_pink);
        yellowColor = context.getResources().getColor(R.color.percent_yellow);
        pinkRedColor = context.getResources().getColor(R.color.percent_pink_red);
        redColor = context.getResources().getColor(R.color.percent_red);
        deepRedColor = context.getResources().getColor(R.color.percent_deep_red);
        grayColor = context.getResources().getColor(R.color.percent_gray);


        pos = new float[2];
        tan = new float[2];
        mBitmapBackDeepRed= BitmapFactory.decodeResource(context.getResources(), R.mipmap.blur_back_deep_red);
        mMatrix=new Matrix();

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth(); //获取宽度
        height = getHeight();//获取高度
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas =new Canvas(mBitmap);

        radius= (int) (height/(1+Math.sin(Math.toRadians(spaceAngle))));//获取最外园的半径
        insideArcRadius= radius-scrollCircleRadius-spaceWidth;//内弧半径
//        Log.i(TAG,"最外园半径"+radius+"\n高度为"+height);
//        Log.i(TAG,"最外园半径"+Math.sin(Math.toRadians(spaceAngle)));
        paintPercentText(mCanvas);
        paintPercentBack(mCanvas);
        paintPercent(mAngel, aimPercent, mCanvas);
//        calculateItemPositions(aimPercent,increaseValue,mCanvas,mBitmapBackDeepRed);
        //将Bitmap画到Canvas
        paintText(mCanvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);


    }

    /**
     * 旋转画布画刻度
     * @param canvas 画布
     */
    private void paintPercentText(Canvas canvas){
        paint.setTextSize(percentTextSize);
        paint.setColor(pinkColor);
        paint.setStrokeWidth(1);
        paint.setTextAlign(Paint.Align.CENTER);
        for (int i=0;i<=10;i++){
            canvas.save();
            //旋转角度
            canvas.rotate((float) (spaceAngle * i + -135 + spaceAngle), width / 2, radius);
            //画文字
            canvas.drawText(i * 10 + "", width / 2,  outerArcWidth + insideArcWidth + spaceWidth * 2, paint);
            canvas.restore();
        }
    }
    /**画两条线的底色*/
    private void paintPercentBack(Canvas canvas){
        paint.setColor(grayColor);
        paint.setStrokeWidth(outerArcWidth);//outerArcWidth
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        //绘制最外层圆条底色
        outerArcRadius=radius-outerArcWidth;
        outerArea= new RectF(width/2 - outerArcRadius, radius - outerArcRadius, width/2  + outerArcRadius, radius + outerArcRadius);
        canvas.drawArc(outerArea,
                (float) (180 - floatAngel),
                (float) (180 + 2 * floatAngel), false, paint);
        //绘制里层大宽度弧形
        paint.setColor(pinkColor);
        paint.setStrokeWidth(insideArcWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(new RectF(width / 2 - insideArcRadius, radius - insideArcRadius, width / 2 + insideArcRadius, radius + insideArcRadius),
                (float) (180 - floatAngel),
                (float) (180 + 2 * floatAngel), false, paint);

    }

    /***
     * 4个色值由浅到深分别是 ffd200 ff5656 fa4040 f60157
     * 等级划分：0-20% 再接再厉   21-60% 技高一筹   61-90% 名列前茅   90以上 理财达人
     */
    private void paintPercent(double percent,double aimPercent,Canvas canvas){
        double roateAngel=percent*0.01*225;
        shaderPaint.setColor(yellowColor);
        shaderPaint.setStrokeCap(Paint.Cap.ROUND);
        shaderPaint.setAntiAlias(true);
        shaderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));//shaderPaint.setColor(yellowColor);
        if (aimPercent>=0&&aimPercent<=20){
        }else if (aimPercent>20&&aimPercent<=60){
            int colorSweep[] = { yellowColor,pinkRedColor };
            float position[]={0.5f,0.7f};
            SweepGradient sweepGradient=new SweepGradient(width / 2, radius, colorSweep, position);
            shaderPaint.setShader(sweepGradient);
        }else if (aimPercent>60&&aimPercent<=90){
            int colorSweep[] = { yellowColor,pinkRedColor,redColor };
            float position[]={0.5f,0.7f,0.8f};
            SweepGradient sweepGradient=new SweepGradient(width / 2, radius, colorSweep, position);
            shaderPaint.setShader(sweepGradient);
        }else if (aimPercent>90){
            int colorSweep[] = {deepRedColor, yellowColor,yellowColor,pinkRedColor,redColor, deepRedColor};
            float position[]={0.2f,0.4f,0.5f,0.7f,0.9f,1.0f};
            SweepGradient sweepGradient=new SweepGradient(width / 2, radius, colorSweep, position);
            shaderPaint.setShader(sweepGradient);
        }
        if (aimPercent<=10){//目的是为了
            drawInsideArc((float) (180 - floatAngel), (float) roateAngel, canvas);
            drawOuterAcr((float) (180 - floatAngel), (float) roateAngel, canvas,mBitmapBackDeepRed,yellowColor);
        }else if (aimPercent>10&&aimPercent<=20){
            drawInsideArc((float) (180 - floatAngel), (float) roateAngel, canvas);
            drawOuterAcr((float) (180 - floatAngel), (float) roateAngel, canvas,mBitmapBackDeepRed,yellowColor);
        }else if (aimPercent>20&&aimPercent<=60){
            drawInsideArc((float) (180 - floatAngel), (float) (roateAngel-(spaceAngle-floatAngel)), canvas);
            drawOuterAcr((float) (180 - floatAngel), (float) (roateAngel - (spaceAngle - floatAngel)), canvas,mBitmapBackDeepRed,pinkRedColor);
        }else if (aimPercent>60&&aimPercent<=90){
            drawInsideArc((float) (180 - floatAngel), (float) (roateAngel-(spaceAngle-floatAngel)), canvas);
            drawOuterAcr((float) (180 - floatAngel), (float) (roateAngel - (spaceAngle - floatAngel)),canvas,mBitmapBackDeepRed,redColor);
        }else {
            drawInsideArc((float) (180 - floatAngel), (float) (roateAngel-2*(spaceAngle-floatAngel)), canvas);
            drawOuterAcr((float) (180 - floatAngel), (float) (roateAngel-2*(spaceAngle-floatAngel)), canvas,mBitmapBackDeepRed, deepRedColor);
        }


    }

    /***
     * 画内部圆环渐变
     * @param formDegree 起始角度
     * @param toDegree 旋转角度
     * @param canvas 画布
     */
    private void drawInsideArc(float formDegree ,float toDegree,Canvas canvas){
        shaderPaint.setStrokeWidth(insideArcWidth);
        shaderPaint.setStyle(Paint.Style.STROKE);
        //内弧半径
        canvas.drawArc(new RectF(width/2 - insideArcRadius, radius - insideArcRadius, width/2  + insideArcRadius, radius + insideArcRadius),
                        formDegree,
                        toDegree, false, shaderPaint);

    }

    /***
     * 绘制外部彩色线条和小红圈
     * @param formDegree 起始角度
     * @param toDegree 旋转角度
     * @param canvas 画布
     * @param bitmap 四种状态的模糊Bitmap
     * @param color 四种状态的实心颜色
     */
    private void drawOuterAcr(float formDegree ,float toDegree,Canvas canvas,Bitmap bitmap,int color){
        shaderPaint.setStrokeWidth(outerArcWidth);
        shaderPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawArc( new RectF(width/2 - outerArcRadius, radius - outerArcRadius, width/2  + outerArcRadius, radius + outerArcRadius),
//                formDegree,
//                toDegree, false, shaderPaint);
        Path orbit = new Path();
        //通过Path类画一个90度（180—270）的内切圆弧路径
        orbit.addArc(outerArea, formDegree, toDegree);
        // 创建 PathMeasure
        PathMeasure measure = new PathMeasure(orbit, false);
        measure.getPosTan(measure.getLength() * 1, pos, tan);
        mMatrix.reset();
        mMatrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合
        canvas.drawPath(orbit, shaderPaint);//绘制外层的线条
        canvas.drawBitmap(bitmap, mMatrix, bitmapPaint);//绘制
        bitmapPaint.setColor(color);
        //绘制实心小圆圈
        canvas.drawCircle(pos[0], pos[1], 5, bitmapPaint);
    }
    /***
     * 4个色值由浅到深分别是 ffd200 ff5656 fa4040 f60157
     * 等级划分：0-20% 再接再厉   21-60% 技高一筹   61-90% 名列前茅   90以上 理财达人
     */
    private void paintText(Canvas canvas){
        if (!TextUtils.isEmpty(tag)&&!TextUtils.isEmpty(aim)){
            if (aimPercent>=0&&aimPercent<=20){
                textPaint.setColor(yellowColor);
            }else if (aimPercent>20&&aimPercent<=60){
                textPaint.setColor(pinkRedColor);
            }else if (aimPercent>60&&aimPercent<=90){
                textPaint.setColor(redColor);
            }else {
                textPaint.setColor(deepRedColor);
            }
            textPaint.setTextSize(textSizeTag);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setStrokeWidth(2);
            canvas.drawText(tag, width / 2, radius - textSizeTag / 2, textPaint);
            textPaint.setColor(grayColor);
            textPaint.setTextSize(textSizeAim);
            textPaint.setStrokeWidth(1);
            float leftLength=textPaint.measureText("你击败了");
            float rightLength=textPaint.measureText("的用户");
            float centerLength=textPaint.measureText(aim+"%");
            float rightOffset=textSizeAim/2;//像右边的偏移量
            canvas.drawText("你击败了",width/2-leftLength/2-centerLength/2+rightOffset,radius + textSizeAim, textPaint);
            canvas.drawText("的用户",width/2+rightLength/2+centerLength/2+rightOffset,radius + textSizeAim, textPaint);
            textPaint.setColor(Color.parseColor("#fa4040"));
            canvas.drawText(aim+"%",width/2+rightOffset,radius + textSizeAim, textPaint);

        }


    }

//
//    private void calculateItemPositions(double aimPercent,double increaseValue,Canvas canvas,Bitmap bitmap) {
//        //内切弧形路径
//        //以圆点坐标（x，y）为中心画一个矩形RectF
//        RectF area = new RectF(width/2  - outerArcRadius, radius - outerArcRadius, width/2  + outerArcRadius, radius + outerArcRadius);
//        Path orbit = new Path();
//        //通过Path类画一个90度（180—270）的内切圆弧路径
//        orbit.addArc(area,  (float) (180 - floatAngel), (float) ((180 +2*floatAngel)*aimPercent*0.01));
//        // 创建 PathMeasure
//        PathMeasure measure = new PathMeasure(orbit, false);
//
//
//        // 计算当前的位置在总长度上的比例[0,1]
//        currentValue += increaseValue/aimPercent;
//        if (currentValue >= 1) {
//            currentValue = 1;
//        }
////        LogUtils.i("增长角度increaseValue"+increaseValue);
////               LogUtils.i("增长的比例currentValue"+currentValue);
//
//        // 获取当前位置的坐标以及趋势
//        measure.getPosTan(measure.getLength() * currentValue, pos, tan);
//        mMatrix.reset();                                                        // 重置Matrix
////        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度
////        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);   // 旋转图片
//        mMatrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合
//
//        canvas.drawBitmap(bitmap, mMatrix, bitmapPaint);
//
//    }

    /**
     * 设置角度变化，刷新界面
     * @param angel 旋转进度
     * @param aimPercent 目标百分比
     */
    public void setAngel(double angel,double aimPercent){
        if (angel<0){
            throw new IllegalArgumentException("Angel must more than 0");
        }
        this.mAngel=angel;
        this.aimPercent=aimPercent;
        postInvalidate();
    }

    /**
     * 设置文字
     * @param tag 名列前茅文案
     * @param aim 击败的百分比
     */
    public void setRankText(String tag,String aim){
        this.tag=tag;
        this.aim=aim;
        postInvalidate();

    }

}
