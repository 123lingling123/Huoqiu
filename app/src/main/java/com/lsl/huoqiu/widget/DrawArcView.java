package com.lsl.huoqiu.widget;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.utils.DeviceUtils;


/**
 * 主页环形动画
 * @ClassName: DrawArcView
 * @author qulq
 * @date 2014年8月20日下午4:54:59
 * @version V1.0
 *
 */
@SuppressLint("NewApi")
public class DrawArcView extends View{

	private final  Paint paint;
	private final  Paint paint1;
	private final  Paint paint2;
	private final  Paint textPaint;
	private final  Paint bkLinePaint;
	private final  Paint hqLinePaint;
	private final  Paint mulTextPaint;
	private final  Paint textPaint2;
	private final  Paint textPaint3;
	
	private double mulNum = 0.0;	//倍数
	private double mulNum2;
	
	private Bitmap mBitmap; 
	
	private boolean flag = false;
	public int angleA = 0;
	public int angleB = 10;	//银行所在角度
	public int angleC = 0;
	
	private int backColor;
	private int huoqiuColor;
	private int huoqiuFontColor;
	
	private int width = 0;
	private int height = 0;
	private int densityDpi = 0;
	private int defaultSize =10;
	private float dp15;
	private float dp5;
	private float dp5_old;
	private boolean isDraw = false;
	private int angleH;//火球所占角度
	private double mulNum3;//倍数增长量
	
	public DrawArcView(Context context) {
		this(context,null);
		
	}
	
	public void setData(double num){
		mulNum = num;
//		if(mulNum==0.0)
//			mulNum = 43.3;
		//算出火球和银行各自所占圆环角度(总共260度)
		angleH = (int) (260/(mulNum+1)*mulNum);
		angleB = 260 - angleH;
//		mulNum3 = mulNum/angleB;
	}
	
	public DrawArcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setData(0.0);
		
		int[] hw = DeviceUtils.getScreenHW(context);
		this.width = hw[0];
		this.height = hw[1];
		this.densityDpi = hw[2];
		
		this.paint = new Paint();
		this.paint1 = new Paint();
		this.paint2 = new Paint();
		this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.bkLinePaint = new Paint();
		this.hqLinePaint = new Paint();
		this.mulTextPaint = new Paint();
		this.textPaint2 = new Paint();
		this.textPaint3 = new Paint();
		
		float strokeWidth = context.getResources().getDimension(R.dimen.dp5);
		dp15 = context.getResources().getDimension(R.dimen.dp15);
		dp5 = context.getResources().getDimension(R.dimen.dp5);
		dp5_old = context.getResources().getDimension(R.dimen.dp5);
		
		backColor = context.getResources().getColor(R.color.backcolor);
		huoqiuColor = context.getResources().getColor(R.color.hq_cicle_color);
		huoqiuFontColor = context.getResources().getColor(R.color.huoqiuRedColor);
		
		int bgColor = context.getResources().getColor(R.color.lightgray);
		
		paint.setAntiAlias(true);
		paint.setColor(bgColor);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Style.STROKE);
		
		paint1.setAntiAlias(true);
		paint1.setColor(backColor);
		paint1.setStrokeWidth(strokeWidth);
		paint1.setStyle(Style.STROKE);
		
		paint2.setAntiAlias(true);
		paint2.setColor(huoqiuColor);
		paint2.setStrokeWidth(strokeWidth);
		paint2.setStyle(Style.STROKE);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		if(!isDraw){
			int center_w = (int)(width*2/3-15);
			int left = 80;
			int top = 100;
			int right = left + center_w;
			int down = top + center_w;
			
			RectF oval = new RectF(left, top, right, down);
			
			canvas.drawArc(oval, 140, 260, flag, paint);
			
				angleA+=1;//银行增长速度
				
				if(angleA<=angleB){
					canvas.drawArc(oval, 140, angleA, flag, paint1);
				}else{
					if(angleC<=260-angleB){
						angleC+=1;
						canvas.drawArc(oval, 140 + angleB, angleC, flag, paint2);
						invalidate();
					}else{
						isDraw = true;
						cli();
					}
				}
			
			super.onDraw(canvas);
		}else{
			myDraw(canvas);
		}
		myDraw(canvas);
	}
	
	private void myDraw(Canvas canvas){
		
		int center_w = (int)(width*2/3-15);
		int left = 8;
		int top = 10;
		int right = left + center_w;
		int down = top + center_w;
		
		RectF oval = new RectF(left, top, right, down);
		
		canvas.drawArc(oval, 140, 260, flag, paint);
		canvas.drawArc(oval, 140, angleB, flag, paint1);
		canvas.drawArc(oval, 140+angleB, angleH-angleB, flag, paint2);
		
	}
	
	/**
	 * 重绘画面
	 */
	public void cli(){
		angleA = 0;
		angleC = 0;
		mulNum2 = 0.0f;
	}
	
	/**
	 * float精确到小数点1位
	 * @param f
	 * @return
	 */
	public double getFloat(Double f){
		BigDecimal b = new BigDecimal(f);
		double ft = b.setScale(1,  BigDecimal.ROUND_HALF_UP).doubleValue();
		return ft;
	}
	
	//重绘界面
	public void myinvalidate(){
		isDraw = false;
		invalidate();
	}
	//清空画布
	public void clear(Canvas canvas)  
	{  
		canvas.drawColor(0xFFFFFFFF);
	    Paint paint = new Paint();  
	    paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));  
	    canvas.drawPaint(paint);  
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC));  
	      
	    invalidate();  
	}
	
	//度到弧度的转换  
    public float DegToRad(float deg)  
    {  
        return (float) (3.14159265358979323846 * deg / 180.0);  
    }

	

}