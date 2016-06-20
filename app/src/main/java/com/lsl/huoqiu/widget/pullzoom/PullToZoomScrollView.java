package com.lsl.huoqiu.widget.pullzoom;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lsl.huoqiu.R;


public class PullToZoomScrollView extends PullToZoomBase<ScrollView> {
//    private static final String TAG = PullToZoomScrollView.class.getSimpleName();
    private boolean isCustomHeaderHeight = false;
    /**
     * 头部容器Frame
     */
    private FrameLayout mHeaderContainer;
    /**
     * 底部容器Linear
     */
    private LinearLayout mRootContainer;
    /**
     * 内容View
     */
    private View mContentView;
    /**
     * 头部高度
     */
    private int mHeaderHeight;
    private ScalingRunnable mScalingRunnable;
    private OnPullToUpListener onPullToUpListener;
    private  int mDistance;
    public static int mContantDis=100;//内容布局压住图片的距离
    public static int mHeaderDis=200;//头布局缩进去的距离
    public static final float HEAD_DAMP = 2.0f;//头部布局拉伸阻尼
    public static final float CONTENT_DAMP = 4.0f;//内容布局拉伸阻尼4.0f
    private static final float TIME = 4.0f;//回复速度，数值越大回复速度越快
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

    public PullToZoomScrollView(Context context) {
        this(context, null);
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScalingRunnable = new ScalingRunnable();
        ((InternalScrollView) mRootView).setOnScrollViewChangedListener(new OnScrollViewChangedListener() {
            @Override
            public void onInternalScrollChanged(int left, int top, int oldLeft, int oldTop) {
                if (isPullToZoomEnabled() && isParallax()) {
//                    Log.d(TAG, "onScrollChanged --> getScrollY() = " + mRootView.getScrollY());
                    float f = mHeaderHeight - mHeaderContainer.getBottom() + mRootView.getScrollY();
//                    Log.d(TAG, "onScrollChanged --> f = " + f);
                    if (onPullToUpListener!=null){
                        onPullToUpListener.PullToUp(f);
                    }
                    //直上直下控制效果
//                    if ((f > 0.0F) && (f < mHeaderHeight)) {
//                        int i = (int) (0.65D * f);
//                        mHeaderContainer.scrollTo(0, -i);
//                    } else if (mHeaderContainer.getScrollY() != 0) {
//                        mHeaderContainer.scrollTo(0, 0);
//                    }
                }
            }
        });
    }

    @Override
    protected void pullHeaderToZoom(int newScrollValue) {
//        Log.d(TAG, "pullHeaderToZoom --> newScrollValue = " + newScrollValue);
//        Log.d(TAG, "pullHeaderToZoom --> mHeaderHeight = " + mHeaderHeight);
        if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
            mScalingRunnable.abortAnimation();
        }
        if (onPullToDownListener!=null){
            onPullToDownListener.pullToDown((newScrollValue));
        }
        mRootContainer.scrollTo(0, (int) (newScrollValue/HEAD_DAMP));
        mDistance=Math.abs(newScrollValue);
        if (mDistance<mContantDis*CONTENT_DAMP){
            mContentView.scrollTo(0, (int) (newScrollValue / CONTENT_DAMP + mContantDis));
//            mContentView.setPadding(mContentView.getPaddingLeft(),-(200-mDistance),mContentView.getPaddingRight(),mContentView.getPaddingBottom());
        }
//        mDistance=newScrollValue;
//        ViewGroup.LayoutParams localLayoutParams = mHeaderContainer.getLayoutParams();
//        localLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
//        mHeaderContainer.setLayoutParams(localLayoutParams);
//
//        if (isCustomHeaderHeight) {
//            ViewGroup.LayoutParams zoomLayoutParams = mZoomView.getLayoutParams();
//            zoomLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
//            mZoomView.setLayoutParams(zoomLayoutParams);
//        }
    }

    /**
     * 是否显示headerView
     *
     * @param isHideHeader true: show false: hide
     */
    @Override
    public void setHideHeader(boolean isHideHeader) {
        if (isHideHeader != isHideHeader() && mHeaderContainer != null) {
            super.setHideHeader(isHideHeader);
            if (isHideHeader) {
                mHeaderContainer.setVisibility(GONE);
            } else {
                mHeaderContainer.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void setHeaderView(View headerView) {
        if (headerView != null) {
            mHeaderView = headerView;
            updateHeaderView();
        }
    }

    @Override
    public void setZoomView(View zoomView) {
        if (zoomView != null) {
            mZoomView = zoomView;
            mZoomView.setPadding(zoomView.getPaddingLeft(),-1*mHeaderDis,zoomView.getPaddingRight(),zoomView.getPaddingBottom());

            updateHeaderView();
        }
    }

    private void updateHeaderView() {
        if (mHeaderContainer != null) {
            mHeaderContainer.removeAllViews();

            if (mZoomView != null) {
                mHeaderContainer.addView(mZoomView);
            }

            if (mHeaderView != null) {
                mHeaderContainer.addView(mHeaderView);
            }
        }
    }

    public void setScrollContentView(View contentView) {
        if (contentView != null) {
            if (mContentView != null) {
                mRootContainer.removeView(mContentView);
            }
            mContentView = contentView;
            mContentView.scrollTo(0, mContantDis);
//            mContentView.setPadding(contentView.getPaddingLeft(),-1*200,contentView.getPaddingRight(),contentView.getPaddingBottom());
            mRootContainer.addView(mContentView);
        }
    }

    @Override
    protected ScrollView createRootView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new InternalScrollView(context, attrs);
        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected void smoothScrollToTop() {
        //原来的
//        Log.d(TAG, "smoothScrollToTop --> ");
//        mScalingRunnable.startAnimation(200L);
        new CountDownTimer((long) (mDistance/TIME), 1) {
            public void onTick(long millisUntilFinished) {
                long distance= (long) (TIME*millisUntilFinished);
//                System.out.println("finished: " + distance);
                if (onPullToDownListener!=null) {
                    onPullToDownListener.pullToRefresh(distance);
                }
                if (distance < mContantDis * CONTENT_DAMP) {
                    mContentView.scrollTo(0, (int) (mContantDis-distance/CONTENT_DAMP));

                }
                mRootContainer.scrollTo(0, -(int) (distance/HEAD_DAMP));
            }

            public void onFinish() {
//                System.out.println("done!");
                if (onPullToDownListener!=null) {
                    onPullToDownListener.pullToFinish();
                }
                mRootContainer.scrollTo(0, 0);
                mContentView.scrollTo(0,mContantDis);
            }
        }.start();
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRootView.getScrollY() == 0;
    }

    @Override
    public void handleStyledAttributes(TypedArray a) {
        mRootContainer = new LinearLayout(getContext());
        mRootContainer.setOrientation(LinearLayout.VERTICAL);
        mHeaderContainer = new FrameLayout(getContext());

        if (mZoomView != null) {
            mHeaderContainer.addView(mZoomView);
        }
        if (mHeaderView != null) {
            mHeaderContainer.addView(mHeaderView);
        }
        int contentViewResId = a.getResourceId(R.styleable.PullToZoomView_contentView, 0);
        if (contentViewResId > 0) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
            mContentView = mLayoutInflater.inflate(contentViewResId, null, false);
        }

        mRootContainer.addView(mHeaderContainer);
        if (mContentView != null) {
            mRootContainer.addView(mContentView);
        }

        mRootContainer.setClipChildren(false);
        mHeaderContainer.setClipChildren(false);

        mRootView.addView(mRootContainer);
    }

    /**
     * 设置HeaderView高度
     *
     * @param width
     * @param height
     */
    public void setHeaderViewSize(int width, int height) {
        if (mHeaderContainer != null) {
            Object localObject = mHeaderContainer.getLayoutParams();
            if (localObject == null) {
                localObject = new ViewGroup.LayoutParams(width, height);
            }
            ((ViewGroup.LayoutParams) localObject).width = width;
            ((ViewGroup.LayoutParams) localObject).height = height;
            mHeaderContainer.setLayoutParams((ViewGroup.LayoutParams) localObject);
            mHeaderHeight = height;
            isCustomHeaderHeight = true;
        }
    }

    /**
     * 设置HeaderView LayoutParams
     *
     * @param layoutParams LayoutParams
     */
    public void setHeaderLayoutParams(LayoutParams layoutParams) {
        if (mHeaderContainer != null) {
            mHeaderContainer.setLayoutParams(layoutParams);
            mHeaderHeight = layoutParams.height;
            isCustomHeaderHeight = true;
        }
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
                            int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
//        Log.d(TAG, "onLayout --> ");
        if (mHeaderHeight == 0 && mZoomView != null) {
            mHeaderHeight = mHeaderContainer.getHeight();
        }
    }

    class ScalingRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;
        protected int mSpace;
        ScalingRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null) {
                float f2;
                ViewGroup.LayoutParams localLayoutParams;
                if ((!mIsFinished) && (mScale > 1.0D)) {
                    float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
                    f2 = mScale - (mScale - 1.0F) * PullToZoomScrollView.sInterpolator.getInterpolation(f1);
                    localLayoutParams = mHeaderContainer.getLayoutParams();
//                    Log.d(TAG, "ScalingRunnable --> f2 = " + f2);
                    if (f2 > 1.0F) {
                        localLayoutParams.height = ((int) (f2 * mHeaderHeight));
                        mHeaderContainer.setLayoutParams(localLayoutParams);
                        if (isCustomHeaderHeight) {
                            ViewGroup.LayoutParams zoomLayoutParams;
                            zoomLayoutParams = mZoomView.getLayoutParams();
                            zoomLayoutParams.height = ((int) (f2 * mHeaderHeight));
                            mZoomView.setLayoutParams(zoomLayoutParams);
                        }
                        post(this);
                        return;
                    }
                    mIsFinished = true;
                }
            }
        }

        public void startAnimation(long paramLong) {
//            if (mRootContainer!=null){
//                mStartTime=SystemClock.currentThreadTimeMillis();
//                mDuration=paramLong;
//                mSpace= (int) ((mDistance/mDuration)*1000);
//                Log.d(TAG,"滑动距离"+mDistance);
//                Log.d(TAG,"滑动距离de 间距"+mSpace);
//                mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
//                mIsFinished = false;
//                post(this);
                // 这个timer执行scrollY毫秒 每1毫秒回调一次

//            }
            if (mZoomView != null) {

                mStartTime = SystemClock.currentThreadTimeMillis();
                mDuration = paramLong;
                mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
                mIsFinished = false;
                post(this);
            }
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

    public interface OnPullToUpListener{
        public void PullToUp( float  newScrollValues);

    }

    public void setOnPullToUpListener(OnPullToUpListener onPullToUpListener) {
        this.onPullToUpListener = onPullToUpListener;
    }

    public void setOnPullToDownListener(OnPullToDownListener onPullToDownListener) {
        this.onPullToDownListener = onPullToDownListener;
    }

    private  OnPullToDownListener onPullToDownListener;
    public interface OnPullToDownListener{
        public void pullToDown(float newScrollValues);
        public void pullToRefresh(float newScrollValues);
        public void pullToFinish();
    }

}
