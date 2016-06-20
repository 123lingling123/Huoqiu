package com.lsl.huoqiu.widget;


import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer
{

	private static final float MIN_ALPHA = 0.5f;
	private static final float SPACE = 0.2f;
	private static final float MAX_SCALE = 1.2f;
	private static final float MIN_SCALE = 1.0f;//0.85f

	@SuppressLint("NewApi")
	public void transformPage(View view, float position)
	{
//		int pageWidth = view.getWidth();
//		int pageHeight = view.getHeight();

//		Log.e("TAG", view + " , " + position + "");

		if (position < -1)
		{ // [-Infinity,-1)
			// This page is way off-screen to the left.
//			view.setAlpha(0);
//			view.setScaleX(MIN_ALPHA);
//			view.setScaleY(MIN_ALPHA);
			view.setScaleX(MIN_SCALE);
			view.setScaleY(MIN_SCALE);
		} else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
		{ // [-1,1]
			// Modify the default slide transition to shrink the page as well
			Log.e("TAG", view + " , " + position + "");
//			float scaleFactor = Math.min(MAX_SCALE, 1 - Math.abs(position));
//			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//			if (position < 0)
//			{
//				view.setTranslationX(horzMargin - vertMargin / 2);
//
//			} else
//			{
//				view.setTranslationX(-horzMargin + vertMargin / 2);
//			}
//			view.setScaleX(scaleFactor);
//			view.setScaleY(scaleFactor);
			float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(MAX_SCALE-MIN_SCALE);
			view.setScaleX(scaleFactor);
			//少量的偏移，目的是为了适配三星手机在没有绘制
			if(position>0){
				view.setTranslationX(-scaleFactor*2);
			}else if(position<0){
				view.setTranslationX(scaleFactor*2);
			}
			view.setScaleY(scaleFactor);
//			if (position<0) {
//
////				Log.i("TAG",view+""+scaleFactor+"");
//				view.setScaleX(scaleFactor);
//			    view.setScaleY(scaleFactor);
//			}else if (position==0) {
//				view.setScaleX(MAX_SCALE);
//				view.setScaleY(MAX_SCALE);
//			}else if (position>0) {
//				view.setScaleX(scaleFactor);
//				view.setScaleY(scaleFactor);
//			}
			// Scale the page down (between MIN_SCALE and 1)
			

			// Fade the page relative to its size.
//			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
//					/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));

		} else
		{ // (1,+Infinity]
			// This page is way off-screen to the right.
//			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//			view.setTranslationX(horzMargin - vertMargin / 2);
//			view.setScaleX(scaleFactor);
//			view.setScaleY(scaleFactor);
			
			view.setScaleX(MIN_SCALE);
			view.setScaleY(MIN_SCALE);
			
//			ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);  
//            ViewHelper.setPivotY(view, view.getMeasuredHeight()* 0.5f);  
//            ViewHelper.setScaleX(view, MIN_ALPHA);  
//            ViewHelper.setScaleY(view, MIN_ALPHA);
//			view.setAlpha(0);
//			view.setScaleX(MIN_SCALE);
//			view.setScaleY(MIN_SCALE);
		}
	}
}