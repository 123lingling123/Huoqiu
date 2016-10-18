package com.lsl.huoqiu.activity.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.utils.DeviceUtils;
import com.lsl.huoqiu.widget.FlingTextView;
import com.lsl.huoqiu.widget.increasenum.RiseNumberTextView;
import com.lsl.huoqiu.widget.pullanimation.MineLayoutView;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Forrest on 16/9/26.
 */
public class NewMineActivity extends AppCompatActivity {

    private MineLayoutView mine_scroll;
    private LinearLayout linear_top_content;
    private LinearLayout top_text_content;
//    private LinearLayout linear_text_content;
    private LinearLayout linear_text;
    private RelativeLayout linear_zoom_header;
    private ImageView top_image;
    private TextView top_text;
    private RiseNumberTextView top_money;
    private ImageView coin_left,coin_center,coin_right;
    private int walletWidth;
    private int coinWidth;
    RiseNumberTextView rnTextView;
    private TextView content_money;
    private AnimationDrawable ad;
    private ImageView img_content_money;
    private ImageView top_image_gif;
    private FlingTextView fling_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mine_activity);
        mine_scroll= (MineLayoutView) findViewById(R.id.mine_scroll);


        View contentView = LayoutInflater.from(this).inflate(R.layout.pull_zoom_content, null, false);
        linear_text= (LinearLayout) contentView.findViewById(R.id.linear_text);
        content_money= (TextView) contentView.findViewById(R.id.content_money);
        img_content_money= (ImageView) contentView.findViewById(R.id.img_content_money);
//        textViewAnimator();
        fling_text= (FlingTextView) contentView.findViewById(R.id.fling_text);



        //获取到RiseNumberTextView对象
         rnTextView = (RiseNumberTextView)contentView. findViewById(R.id.risenumber_textview);
        // 设置数据
        rnTextView.withNumber(2.50f);
        // 设置动画播放时间
        rnTextView.setDuration(2000);
        // 开始播放动画

        // 监听动画播放结束
        rnTextView.setOnEnd(new RiseNumberTextView.EndListener() {

            @Override
            public void onEndFinish() {
                Toast.makeText(NewMineActivity.this, "数据增长完毕...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        linear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rnTextView.start();
                fling_text.transferY();
            }
        });

        ad= (AnimationDrawable) getResources().getDrawable(R.drawable.money_anim);

//        img_content_money.setImageDrawable(ad);
//        ad.start();


//        View header = LayoutInflater.from(this).inflate(R.layout.pull_header_view, null, false);
//        linear_top_content= (LinearLayout) header.findViewById(R.id.linear_top_content);
//
//        top_text_content= (LinearLayout) header.findViewById(R.id.top_text_content);
////        linear_text_content= (LinearLayout) header.findViewById(R.id.linear_text_content);
//        linear_zoom_header= (RelativeLayout) header.findViewById(R.id.linear_zoom_header);
//        top_image= (ImageView) header.findViewById(R.id.top_image);
//        coin_left= (ImageView) header.findViewById(R.id.coin_left);
//        coin_center= (ImageView) header.findViewById(R.id.coin_center);
//        coin_right= (ImageView) header.findViewById(R.id.coin_right);
//        top_image_gif= (ImageView) header.findViewById(R.id.top_image_gif);
//        top_image_gif.setVisibility(View.GONE);
//
//        top_text= (TextView) header.findViewById(R.id.top_text);
//        top_money= (RiseNumberTextView) header.findViewById(R.id.top_money);
//        top_image_gif.setImageDrawable(ad);
//        linear_zoom_header.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,MineLayoutView.mHeaderDis));

//        Log.e("NewMineActivity","钱包左边的距离"+top_image.getLeft());
//        Log.e("NewMineActivity","钱包顶部的距离"+top_image.getTop());
//        Log.e("NewMineActivity","初始化布局的高度"+height);

//        Bitmap bitmapWallet= BitmapFactory.decodeResource(getResources(), R.mipmap.wallet_huoqiu);
//        Bitmap bitmapCoin= BitmapFactory.decodeResource(getResources(), R.mipmap.coin_huoqiu);
//        walletWidth=bitmapWallet.getWidth();
//        coinWidth=bitmapCoin.getWidth();
//
//        //设置钱包动图宽高
//        top_image_gif.setLayoutParams(new RelativeLayout.LayoutParams(walletWidth,walletWidth));
//        Log.e("NewMineActivity","初始化布局的高度walletWidth"+walletWidth);
//        Log.e("NewMineActivity","coinWidth"+coinWidth);


//        resetCoin();



//        mine_scroll.setHeaderView(header);
        mine_scroll.setContentView(contentView);

        initListener();


//        firstCome();
        mine_scroll.firstCome();

        mine_scroll.setMoney(23.4f);

    }
    private void firstCome(){
        top_image_gif.setVisibility(View.VISIBLE);
        top_image.setVisibility(View.GONE);
        coin_right.setVisibility(View.GONE);
        coin_center.setVisibility(View.GONE);
        coin_left.setVisibility(View.GONE);
        mine_scroll.scrollTo(0,0);
        ad.start();
        // 设置数据
        top_money.withNumber(0.34f);
        // 设置动画播放时间
        top_money.setDuration(2000);
        top_money.setOnEnd(new RiseNumberTextView.EndListener() {
            @Override
            public void onEndFinish() {
                mine_scroll.restScroll();
                ad.start();
            }
        });
        top_money.start();



    }


    private void resetCoin() {
        int deviceWidth= DeviceUtils.getWindowWidth(this);
        coin_left.setPadding(deviceWidth/2-walletWidth/2-coinWidth,0,0,0);
        coin_center.setPadding(deviceWidth/2-walletWidth/2-coinWidth/2,0,0,0);
        coin_right.setPadding(deviceWidth/2-walletWidth/2,0,0,0);

    }
    double right=0.4;//右边金币开始出现的位置
    double left=0.7;//左边金币开始出现的位置
    double center=0.85;//中间金币开始出现的位置

    float aimLocation=70;//金币最终停放的位置
    float floatDis=5;//使3个金币产生错位效果
    private void initListener() {
        mine_scroll.setOnPullListener(new MineLayoutView.OnPullListener() {


            @Override
            public void onPulling(int newScrollValue) {
//                if (newScrollValue==MineLayoutView.mHeaderDis){
//                    top_image_gif.setVisibility(View.VISIBLE);
//                    top_image.setVisibility(View.GONE);
//                    coin_right.setVisibility(View.GONE);
//                    coin_center.setVisibility(View.GONE);
//                    coin_left.setVisibility(View.GONE);
//
//                    ad.start();
////                    mine_scroll.setIsBeingDragged(false);
//                }
//                else {
//                    if (ad.isRunning()){
//                        ad.stop();
//                    }
////                    top_image.setImageResource(R.drawable.wallet_huoqiu);//getResources().getDrawable(R.drawable.wallet_huoqiu)
//                    top_image_gif.setVisibility(View.GONE);
//                    top_image.setVisibility(View.VISIBLE);
//                    coin_right.setVisibility(View.VISIBLE);
//                    coin_center.setVisibility(View.VISIBLE);
//                    coin_left.setVisibility(View.VISIBLE);
//                    top_image_gif.setImageDrawable(ad);
//                }
//                float scrollValue=Math.abs(newScrollValue);
//                float dis=MineLayoutView.mHeaderDis;
//                float rate=scrollValue/dis;
////                Log.e("NewMineActivity","得到的值为"+rate);
//                //对钱包和文字进行缩放和位移
//                ViewHelper.setScaleX(linear_top_content,(float) (rate));
//                ViewHelper.setScaleY(linear_top_content,(float) (rate));
//                ViewHelper.setTranslationY(linear_top_content,MineLayoutView.mHeaderDis/2-MineLayoutView.mHeaderDis*rate/2);
//                //对三个金币进行控制
//                //做X偏移处理，视觉效果上达到钱包刚好接住金币
//                translateX(rate);
//                //透明度处理，和Y方向偏移处理
//                if (rate<right){
//                    ViewHelper.setAlpha(coin_right,rate);
//                    ViewHelper.setTranslationY(coin_right, (float) (MineLayoutView.mHeaderDis*(1-right)));
//                    ViewHelper.setTranslationY(coin_left, (float) (MineLayoutView.mHeaderDis*(1-left)));
//                    ViewHelper.setTranslationY(coin_left, (float) (MineLayoutView.mHeaderDis*(1-center)));
//                }else if (rate>right&&rate<left){
//                    translateY(rate);
//                    ViewHelper.setAlpha(coin_right,rate);
//                    ViewHelper.setAlpha(coin_left, (float) (rate*0.5));
//                    ViewHelper.setAlpha(coin_center, (float) (rate*0.3));
//                }else if (rate>left&&rate<center){
//                    translateY(rate);
//                    ViewHelper.setAlpha(coin_right,rate);
//                    ViewHelper.setAlpha(coin_left, (float) (rate*0.8));
//                    ViewHelper.setAlpha(coin_center, (float) (rate*0.6));
//                }else if (rate>center&&rate<0.9){
//                    translateY(rate);
//                    ViewHelper.setAlpha(coin_right,rate);
//                    ViewHelper.setAlpha(coin_left,rate);
//                    ViewHelper.setAlpha(coin_center, (float) (rate*0.8));
//                }else {
//                    translateY(rate);
//                    ViewHelper.setAlpha(coin_right,rate);
//                    ViewHelper.setAlpha(coin_left,rate);
//                    ViewHelper.setAlpha(coin_center, rate);
//                }
            }

            @Override
            public void onPullEnd() {
//                if (!ad.isRunning()){
//                    ad.start();
//                }
//
//                // 设置数据
//                top_money.withNumber(0.34f);
//                // 设置动画播放时间
//                top_money.setDuration(2000);
//                top_money.setOnEnd(new RiseNumberTextView.EndListener() {
//                    @Override
//                    public void onEndFinish() {
//                        mine_scroll.restScroll();
//                        ad.start();
//                    }
//                });
//                top_money.start();

            }
        });
    }
//    private void translateY(float rate){
//        ViewHelper.setTranslationY(coin_right, (float) (MineLayoutView.mHeaderDis*(1-right)+(aimLocation-MineLayoutView.mHeaderDis*(1-right))*((rate-right)/(1-right))));
//        ViewHelper.setTranslationY(coin_left, (float) (MineLayoutView.mHeaderDis*(1-left)+(aimLocation-floatDis-MineLayoutView.mHeaderDis*(1-left))*((rate-left)/(1-left))));
//        ViewHelper.setTranslationY(coin_center, (float) (MineLayoutView.mHeaderDis*(1-center)+(aimLocation-floatDis*2-MineLayoutView.mHeaderDis*(1-center))*((rate-center)/(1-center))));
//
//    }
//    private void translateX(float rate){
//        ViewHelper.setTranslationX(coin_right,(1-rate)*coinWidth);
//        ViewHelper.setTranslationX(coin_left,(1-rate)*coinWidth);
//        ViewHelper.setTranslationX(coin_center,(1-rate)*coinWidth);
//    }
//
//    private void textViewAnimator() {
//
//        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tips);
//
//        LinearInterpolator lin = new LinearInterpolator();
//
//        operatingAnim.setInterpolator(lin);
//
//        content_money.startAnimation(operatingAnim);
//    }

}
