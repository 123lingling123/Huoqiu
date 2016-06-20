package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.widget.PieProgress;

/**
 * Created by Forrest on 16/4/20.
 */
public class HuoQiuProgressActivity extends AppCompatActivity{
    private PieProgress progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_huoqiu);
        progress= (PieProgress) findViewById(R.id.progress);

        progress.setPieProgress(20,40,100,200);
    }
}
