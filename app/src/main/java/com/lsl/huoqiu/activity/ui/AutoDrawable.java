package com.lsl.huoqiu.activity.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.RoundRectDrawable;

/**
 * Created by Forrest on 16/5/27.
 */
public class AutoDrawable extends AppCompatActivity {
    private TextView textview;
    int strokeWidth = 3; // 3px not dp
    int roundRadius = 8; // 8px not dp
    int strokeColor = Color.parseColor("#DC143C");
    int fillColor = Color.parseColor("#ffffff");
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_drawable);
        textview= (TextView) findViewById(R.id.textview);
        imageView= (ImageView) findViewById(R.id.imageView);
        // 加载动画

        RoundRectDrawable drawable=new RoundRectDrawable();
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        textview.setBackground(gd);
    }
}
