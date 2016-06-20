package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lsl.huoqiu.feng.CaloriesUtils;

/**
 * Created by Forrest on 16/5/3.
 * 返回卡路里的数据
 */
public class FengActivity extends AppCompatActivity {
    private int calories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CaloriesUtils.getData();
        calories=CaloriesUtils.getFoodCalories("大米");
        Toast.makeText(this,"返回的卡路里是"+calories,Toast.LENGTH_SHORT).show();
    }
}
