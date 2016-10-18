package com.lsl.huoqiu.widget.pullanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.utils.DeviceUtils;
import com.lsl.huoqiu.widget.increasenum.RiseNumberTextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Forrest on 16/9/26.
 */
public class MineLayoutView extends LinearLayout{

    private View mHeaderView;//头部布局
    private View mContentView;//内容布局
    protected ScrollView mScrollView;//ScrollView
    private int mTouchSlop;//最小移动距离
    public static int mHeaderDis=200;//头布局缩进去的距离
    private LinearLayout mContainerView;//ScrollView的容器
    private static final float FRICTION = 3.0f;//阻尼

    private boolean isPullEnabled = true;
    private boolean isAnimation = false;//控制文字的动画效果
    private boolean isPulling = false;
    private boolean isHideHeader = false;
    private boolean restControl = false;
    //
    private boolean mIsBeingDragged = false;
    private float mLastMotionY;
    private float mLastMotionX;
    private float mInitialMotionY;
    private float mInitialMotionX;

    //头布局里的控件
    private ImageView coin_left,coin_center,coin_right;
    private ImageView top_image_gif;
    private ImageView top_image;
    private LinearLayout linear_top_content;
    private int walletWidth;
    private int coinWidth;
    private int duration=200;
    private RelativeLayout linear_zoom_header;
    private RiseNumberTextView top_money;
    private AnimationDrawable ad;
    private int currentScrollValue;
    //金币效果
    double right=0.4;//右边金币开始出现的位置
    double left=0.7;//左边金币开始出现的位置
    double center=0.85;//中间金币开始出现的位置

    float aimLocation=70;//金币最终停放的位置
    float floatDis=5;//使3个金币产生错位效果

    private float money;
    private boolean isCancel=false;//用于控制gif动画的效果
    public MineLayoutView(Context context) {
        super(context);
    }

    public MineLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MineLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {

        //获取最小移动距离
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        mScrollView=new InternalScrollView(context,attrs);
        mScrollView.setId(R.id.pull_scrollview);
        mScrollView.setVerticalScrollBarEnabled(false);//设置ScrollBar为null

        addView(mScrollView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        mContainerView = new LinearLayout(context);
        mContainerView.setOrientation(LinearLayout.VERTICAL);

        mScrollView.addView(mContainerView);
        mHeaderView= LayoutInflater.from(context).inflate(R.layout.pull_header_view,null);


        initHeader(context);

        //如果增加属性的话，需要如下代码
        if (mHeaderView!=null){
            mContainerView.addView(mHeaderView);
        }

        if (mContentView!=null){
            mContainerView.addView(mContentView);
        }
        mContainerView.setClipChildren(true);
    }
    private void initHeader(Context context){

        Bitmap bitmapWallet= BitmapFactory.decodeResource(getResources(), R.mipmap.wallet_huoqiu);
        Bitmap bitmapCoin= BitmapFactory.decodeResource(getResources(), R.mipmap.coin_huoqiu);
        walletWidth=bitmapWallet.getWidth();
        coinWidth=bitmapCoin.getWidth();

        linear_zoom_header= (RelativeLayout) mHeaderView.findViewById(R.id.linear_zoom_header);
        linear_zoom_header.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,MineLayoutView.mHeaderDis));


        coin_left= (ImageView) mHeaderView.findViewById(R.id.coin_left);
        coin_center= (ImageView) mHeaderView.findViewById(R.id.coin_center);
        coin_right= (ImageView) mHeaderView.findViewById(R.id.coin_right);
        top_image= (ImageView) mHeaderView.findViewById(R.id.top_image);
        top_image_gif= (ImageView) mHeaderView.findViewById(R.id.top_image_gif);
        top_money= (RiseNumberTextView) mHeaderView.findViewById(R.id.top_money);
        //设置钱包动图宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        top_image_gif.setLayoutParams(new RelativeLayout.LayoutParams(walletWidth,walletWidth));
        top_image_gif.setPadding(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm));
        top_image_gif.setVisibility(View.GONE);
        linear_top_content= (LinearLayout) mHeaderView.findViewById(R.id.linear_top_content);
        ad= (AnimationDrawable) getResources().getDrawable(R.drawable.money_anim);


        int deviceWidth= DeviceUtils.getWindowWidth(context);
        coin_left.setPadding(deviceWidth/2-walletWidth/2-coinWidth,0,0,0);
        coin_center.setPadding(deviceWidth/2-walletWidth/2-coinWidth/2,0,0,0);
        coin_right.setPadding(deviceWidth/2-walletWidth/2,0,0,0);
        //头部隐藏布局
//        mContainerView.scrollTo(0,mHeaderDis);
//        mHeaderView.setPadding(mHeaderView.getPaddingLeft(),-mHeaderDis,mHeaderView.getPaddingRight(),mHeaderView.getPaddingBottom());
        mContainerView.setPadding(mHeaderView.getPaddingLeft(),-mHeaderDis,mHeaderView.getPaddingRight(),mHeaderView.getPaddingBottom());
//        mHeaderView.invalidate();

    }
    /**
     * 设置头部布局
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        if (headerView!=null){
            this.mHeaderView=headerView;
//            mHeaderView.setPadding(headerView.getPaddingLeft(),-1*mHeaderDis,headerView.getPaddingRight(),headerView.getPaddingBottom());
//            mContainerView.scrollTo(0,mHeaderDis);
//            mHeaderView.setPadding(0,-mHeaderDis,0,0);
            mContainerView.setPadding(0,-mHeaderDis,0,0);
            updateView();
        }

    }

    /**
     * 设置内容布局
     * @param mContentView
     */
    public void setContentView(View mContentView) {
        if (mContentView!=null){
            this.mContentView = mContentView;
            updateView();
        }
    }

    /**
     * 获取内容布局
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

    private void updateView(){
        if (mContainerView!=null) {
            mContainerView.removeAllViews();
            if (mHeaderView != null) {
                mContainerView.addView(mHeaderView);
            }

            if (mContentView != null) {
                mContainerView.addView(mContentView);
            }
            mContainerView.invalidate();
        }
    }

    protected class InternalScrollView extends ScrollView {
        private OnScrollViewChangedListener onScrollViewChangedListener;

        public InternalScrollView(Context context) {
            this(context, null);
        }

        public InternalScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setOnScrollViewChangedListener(OnScrollViewChangedListener onScrollViewChangedListener) {
            this.onScrollViewChangedListener = onScrollViewChangedListener;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            if (onScrollViewChangedListener != null) {
                onScrollViewChangedListener.onInternalScrollChanged(l, t, oldl, oldt);
            }
        }
    }

    protected interface OnScrollViewChangedListener {
        public void onInternalScrollChanged(int left, int top, int oldLeft, int oldTop);
    }

    public void setHideHeader(boolean isHideHeader) {//header显示才能Zoom
        this.isHideHeader = isHideHeader;
    }
    public boolean isHideHeader() {
        return isHideHeader;
    }
    public boolean isPullToEnabled() {
        return isPullEnabled;
    }
    private boolean isReadyForPullStart(){
        return mScrollView.getScrollY()==0;
    }

    public boolean isPulling() {
        return isPulling;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isPullToEnabled() || isHideHeader()) {
            return false;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (isReadyForPullStart()) {
                    final float y = event.getY(), x = event.getX();
                    final float diff, oppositeDiff, absDiff;

                    // We need to use the correct values, based on scroll
                    // direction
                    diff = y - mLastMotionY;
                    oppositeDiff = x - mLastMotionX;
                    absDiff = Math.abs(diff);

                    if (absDiff > mTouchSlop && absDiff > Math.abs(oppositeDiff)) {
                        if (diff >= 1f && isReadyForPullStart()) {
                            mLastMotionY = y;
                            mLastMotionX = x;
                            mIsBeingDragged = true;
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    mIsBeingDragged = false;
                }
                break;
            }
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!isPullToEnabled() || isHideHeader()) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {

                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    pullEvent();
                    isPulling = true;
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    // If we're already refreshing, just scroll back to the top
                    if (isPulling()) {
                        smoothScrollToTop();
                        pullEndControl();
                        if (onPullListener != null) {
                            onPullListener.onPullEnd();
                        }
                        isPulling = false;
                        return true;
                    }
                    return true;
                }
                break;
            }
        }
        return false;
    }



    public void setOnPullListener(OnPullListener onPullListener) {
        this.onPullListener = onPullListener;
    }

    private OnPullListener onPullListener;
    public interface OnPullListener {
        public void onPulling(int newScrollValue);

        public void onPullEnd();
    }

    private void pullEvent() {
        final int newScrollValue;
        final float initialMotionValue, lastMotionValue;

        initialMotionValue = mInitialMotionY;
        lastMotionValue = mLastMotionY;
        Log.d("PullToZoomBase","最新的Yshi"+mLastMotionY);
        newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);

        pullHeaderToZoom(newScrollValue);

    }

    /**
     * 下拉移动的距离
     * @param newScrollValue
     */
    private void pullHeaderToZoom(int newScrollValue) {
//        Log.e("MineLayoutView","移动距离"+newScrollValue);
//        if (newScrollValue<mHeaderDis){
//        mContainerView.scrollTo(0,newScrollValue);
//            mHeaderView.scrollTo(0,newScrollValue);
//            mContentView.scrollTo(0,newScrollValue);
//        }
        if (Math.abs(newScrollValue)>mHeaderDis){
            currentScrollValue=mHeaderDis;
//            mContainerView.scrollTo(0,0);
//            mHeaderView.setPadding(0,0,0,0);
            mContainerView.setPadding(0,0,0,0);
            pullDownControl(mHeaderDis);
            if (onPullListener != null) {
                onPullListener.onPulling(mHeaderDis);

            }
        }else {
//            mContainerView.scrollTo(0,mHeaderDis+newScrollValue);
//            mHeaderView.setPadding(0,newScrollValue,0,0);
            mContainerView.setPadding(0,-(mHeaderDis+newScrollValue),0,0);
            currentScrollValue=mHeaderDis+newScrollValue;
            pullDownControl(newScrollValue);
            if (onPullListener != null) {
                onPullListener.onPulling(newScrollValue);
            }
        }


    }

    /**
     * 回复到顶部
     */
    private void smoothScrollToTop() {
//        mContainerView.scrollTo(0,mHeaderDis);
//        mHeaderView.scrollTo(0,0);
//        mContentView.scrollTo(0,0);
//        scrollTo(0,mHeaderDis);
        Log.e("MineLayoutView","返回");
    }

    public void restScroll(){
//        mContentView.scrollTo(0,0);
//        mContainerView.scrollTo(0,mHeaderDis);
//        mHeaderView.setPadding(0,-mHeaderDis,0,0);
        mContainerView.setPadding(0,-mHeaderDis,0,0);
    }

    public void setIsBeingDragged(boolean mIsBeingDragged) {
        this.mIsBeingDragged = mIsBeingDragged;
    }

    public void firstCome(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mHeaderDis);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.e("valueAnimator","第一次进来的动画值"+valueAnimator.getAnimatedValue());
//                mContainerView.scrollTo(0,mHeaderDis-Integer.parseInt(valueAnimator.getAnimatedValue().toString()));
//                mHeaderView.setPadding(0,-(mHeaderDis-Integer.parseInt(valueAnimator.getAnimatedValue().toString())),0,0);
                mContainerView.setPadding(0,-(mHeaderDis-Integer.parseInt(valueAnimator.getAnimatedValue().toString())),0,0);
                pullDownControl(Integer.parseInt(valueAnimator.getAnimatedValue().toString()));
            }



        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                currentScrollValue=mHeaderDis;
                pullEndControl();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();


    }

    private void pullDownControl(int newScrollValue){
        isCancel=false;
            if (newScrollValue==MineLayoutView.mHeaderDis){

                top_image_gif.setVisibility(View.VISIBLE);
                top_image.setVisibility(View.GONE);
                coin_right.setVisibility(View.GONE);
                coin_center.setVisibility(View.GONE);
                coin_left.setVisibility(View.GONE);
                if (!top_money.isRunning()&&!isAnimation) {

                    top_money.start();
                    isAnimation=true;
                }
                ad.start();
//                    mine_scroll.setIsBeingDragged(false);
            }
            else {
                if (ad.isRunning()){
                    ad.stop();
                }
                if (top_money.isRunning()){
                    top_money.cancel();

                }
                isAnimation=false;
                restControl=false;
//                    top_image.setImageResource(R.drawable.wallet_huoqiu);//getResources().getDrawable(R.drawable.wallet_huoqiu)
                top_image_gif.setVisibility(View.GONE);
                top_image.setVisibility(View.VISIBLE);
                coin_right.setVisibility(View.VISIBLE);
                coin_center.setVisibility(View.VISIBLE);
                coin_left.setVisibility(View.VISIBLE);
                top_image_gif.setImageDrawable(ad);
            }
            float scrollValue=Math.abs(newScrollValue);
            float dis=MineLayoutView.mHeaderDis;
            float rate=scrollValue/dis;
//                Log.e("NewMineActivity","得到的值为"+rate);
            //对钱包和文字进行缩放和位移
            ViewHelper.setScaleX(linear_top_content,(float) (rate));
            ViewHelper.setScaleY(linear_top_content,(float) (rate));
            ViewHelper.setTranslationY(linear_top_content,MineLayoutView.mHeaderDis/2-MineLayoutView.mHeaderDis*rate/2);
            //对三个金币进行控制
            //做X偏移处理，视觉效果上达到钱包刚好接住金币
            translateX(rate);
            //透明度处理，和Y方向偏移处理
            if (rate<right){
                ViewHelper.setAlpha(coin_right,rate);
                ViewHelper.setTranslationY(coin_right, (float) (MineLayoutView.mHeaderDis*(1-right)));
                ViewHelper.setTranslationY(coin_left, (float) (MineLayoutView.mHeaderDis*(1-left)));
                ViewHelper.setTranslationY(coin_left, (float) (MineLayoutView.mHeaderDis*(1-center)));
            }else if (rate>right&&rate<left){
                translateY(rate);
                ViewHelper.setAlpha(coin_right,rate);
                ViewHelper.setAlpha(coin_left, (float) (rate*0.5));
                ViewHelper.setAlpha(coin_center, (float) (rate*0.3));
            }else if (rate>left&&rate<center){
                translateY(rate);
                ViewHelper.setAlpha(coin_right,rate);
                ViewHelper.setAlpha(coin_left, (float) (rate*0.8));
                ViewHelper.setAlpha(coin_center, (float) (rate*0.6));
            }else if (rate>center&&rate<0.9){
                translateY(rate);
                ViewHelper.setAlpha(coin_right,rate);
                ViewHelper.setAlpha(coin_left,rate);
                ViewHelper.setAlpha(coin_center, (float) (rate*0.8));
            }else {
                translateY(rate);
                ViewHelper.setAlpha(coin_right,rate);
                ViewHelper.setAlpha(coin_left,rate);
                ViewHelper.setAlpha(coin_center, rate);
            }
    }

    private void translateY(float rate){
        ViewHelper.setTranslationY(coin_right, (float) (MineLayoutView.mHeaderDis*(1-right)+(aimLocation-MineLayoutView.mHeaderDis*(1-right))*((rate-right)/(1-right))));
        ViewHelper.setTranslationY(coin_left, (float) (MineLayoutView.mHeaderDis*(1-left)+(aimLocation-floatDis-MineLayoutView.mHeaderDis*(1-left))*((rate-left)/(1-left))));
        ViewHelper.setTranslationY(coin_center, (float) (MineLayoutView.mHeaderDis*(1-center)+(aimLocation-floatDis*2-MineLayoutView.mHeaderDis*(1-center))*((rate-center)/(1-center))));

    }

    /**
     *  金币做X方向上的偏移
     * @param rate
     */
    private void translateX(float rate){
        ViewHelper.setTranslationX(coin_right,(1-rate)*coinWidth);
        ViewHelper.setTranslationX(coin_left,(1-rate)*coinWidth);
        ViewHelper.setTranslationX(coin_center,(1-rate)*coinWidth);
    }

    /**
     * 滑动底部时的控制
     */
    private void pullEndControl(){
        isCancel=true;
        Log.e("PullEndControl","currentScrollValue"+currentScrollValue);
        if (currentScrollValue==mHeaderDis&&!restControl) {
            Log.e("PullEndControl","滑动底部时的控制"+"开始动画");
            if (!ad.isRunning()) {
                ad.start();
            }

            isAnimation=false;
            top_money.start();

        }else if (currentScrollValue!=mHeaderDis&&!restControl){
            Log.e("PullEndControl","滑动底部时的控制"+"回复动画");
            restAnimation();
        }else {
            waitAnimation();
        }


    }

    /**
     * 返回初始状态的动画
     */
    private void restAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt( currentScrollValue,0);

        valueAnimator.setDuration(duration*currentScrollValue/mHeaderDis);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                mContainerView.scrollTo(0,mHeaderDis-Integer.parseInt(valueAnimator.getAnimatedValue().toString()));
//                mHeaderView.setPadding(0,-(mHeaderDis-Integer.parseInt(valueAnimator.getAnimatedValue().toString())),0,0);
                mContainerView.setPadding(0,-(mHeaderDis-Integer.parseInt(valueAnimator.getAnimatedValue().toString())),0,0);

//                pullDownControl(Integer.parseInt(valueAnimator.getAnimatedValue().toString()));
            }



        });

        valueAnimator.start();
    }

    public void setMoney(float money) {
        this.money = money;
        // 设置数据
        top_money.withNumber(money);
        // 设置动画播放时间
        top_money.setDuration(800);
        top_money.setOnEnd(new RiseNumberTextView.EndListener() {
            @Override
            public void onEndFinish() {
//                    restScroll();
                Log.e("PullEndControl","动画结束");
                restControl=true;
                if (isCancel){
                    waitAnimation();
                }


            }
        });
    }

    /**
     * 暂留动画，数字增长的动画要比数字停留的时间少1倍
     */
    private void waitAnimation(){
        ValueAnimator valueAnimatorFloat = ValueAnimator.ofFloat(0, 0);
        valueAnimatorFloat.setDuration(1200);

        valueAnimatorFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedFraction()>=1){
                    Log.e("PullEndControl","执行回复动作");

                    if (isCancel) {
                        ad.stop();
                        restAnimation();
//                        Log.e("MienLayoutView", "得到监听");
                    }
                }
            }


        });
        valueAnimatorFloat.start();
    }

}
