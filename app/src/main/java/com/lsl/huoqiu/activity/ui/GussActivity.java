package com.lsl.huoqiu.activity.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.utils.FastBlur;

/**
 * Created by Forrest on 16/5/12.
 */
public class GussActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guss_bitmap);

        image = (ImageView) findViewById(R.id.picture);
        text = (TextView) findViewById(R.id.text);
        image.setImageResource(R.mipmap.test);
        statusText = addStatusText((ViewGroup) findViewById(R.id.controls));
        addCheckBoxes((ViewGroup) findViewById(R.id.controls));

        if (savedInstanceState != null) {
            downScale.setChecked(savedInstanceState.getBoolean(DOWNSCALE_FILTER));
        }
        applyBlur();
    }

    private final String DOWNSCALE_FILTER = "downscale_filter";

    private ImageView image;
    private TextView text;
    private CheckBox downScale;
    private TextView statusText;



    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, text);
                return true;
            }
        });
    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//缩放比例
        float radius = 20;//模糊程度
//        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 2;
//        }

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    @Override
    public String toString() {
        return "Fast blur";
    }

    private void addCheckBoxes(ViewGroup container) {

        downScale = new CheckBox(GussActivity.this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        downScale.setLayoutParams(lp);
        downScale.setText("Downscale before blur");
        downScale.setVisibility(View.VISIBLE);
        downScale.setTextColor(0xFFFFFFFF);
        downScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                applyBlur();
            }
        });
        container.addView(downScale);
    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(GussActivity.this);
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DOWNSCALE_FILTER, downScale.isChecked());
        super.onSaveInstanceState(outState);
    }
}
