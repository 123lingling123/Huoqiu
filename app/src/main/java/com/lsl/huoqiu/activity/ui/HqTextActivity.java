package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.widget.TextView;

import com.lsl.huoqiu.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Forrest on 16/5/27.
 */
public class HqTextActivity extends AppCompatActivity {
    private TextView textview;
//    private String str = "最长133天";
    private String str = "活期133";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hqtext);
        textview = (TextView) findViewById(R.id.textview);
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        Log.e("HqTextActivity", "得到的字符串" + m.toString());
        System.out.println(m.replaceAll("").trim());
        textview.setText(getSpanByNum(str));

    }

    private Spannable getSpanByNum(String percent) {
        Spannable span = new SpannableString(percent);
        int index=0;
        for (int i = 0; i < percent.length(); i++) {
//            System.out.println(((int) percent.charAt(i)));
            if (percent.charAt(i) >= 48 && percent.charAt(i) <= 57) {
                index++;
                span.setSpan(new AbsoluteSizeSpan(getResources()
                                .getDimensionPixelSize(R.dimen.sp15)), i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (index>0){
            return span;
        }else {
            span.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.sp18)),0,percent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }

    }
}
