package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.LabelRadioButton;

/**
 * Created by Forrest on 16/4/22.
 */
public class HuoQiuMainActivity extends AppCompatActivity{
    private LabelRadioButton button;
    private boolean isChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        button= (LabelRadioButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked){
                    button.setLabelVisible();
                    isChecked=!isChecked;
                }else{
                    button.setLabelGone();
                    isChecked=!isChecked;
                }
            }
        });
    }
}
