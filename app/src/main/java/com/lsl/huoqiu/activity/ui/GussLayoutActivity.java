package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.GussLayout;

/**
 * Created by Forrest on 16/5/12.
 */
public class GussLayoutActivity extends AppCompatActivity {
    private LinearLayout layout;
    private GussLayout gusslayout;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guss_layout);
        layout= (LinearLayout) findViewById(R.id.layout);
        text= (TextView) findViewById(R.id.text);
        gusslayout= (GussLayout) findViewById(R.id.gusslayout);
        gusslayout.setImageBack(gusslayout);

        gusslayout.setGussLayout(gusslayout,text);


    }
}
