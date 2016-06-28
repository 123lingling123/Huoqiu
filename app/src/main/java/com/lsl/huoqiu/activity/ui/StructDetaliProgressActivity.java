package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.bean.StructBean;
import com.lsl.huoqiu.widget.StructProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/6/28.
 */
public class StructDetaliProgressActivity extends AppCompatActivity {
    private StructProgress mStructProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sturct_progress_activity);
        mStructProgress= (StructProgress) findViewById(R.id.struct);
        mStructProgress.setData(getData());
    }
    private List<StructBean> getData(){
        List<StructBean> beans=new ArrayList<StructBean>();
        beans.add(new StructBean(7,false));
        beans.add(new StructBean(12,true));
        beans.add(new StructBean(16,false));
        beans.add(new StructBean(20,false));
        return  beans;
    }
}
