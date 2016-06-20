package com.lsl.huoqiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.activity.function.AlarmActivity;
import com.lsl.huoqiu.activity.ui.HuoQiuProgressActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/6/20.
 */
public class FunctionActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayAdapter<String> adapter;
    List<String> data = new ArrayList<String>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_base);
        listview= (ListView) findViewById(R.id.listview);
        intent=new Intent();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, getData());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    intent.setClass(FunctionActivity.this,AlarmActivity.class);
                }else if (position==1){
                    intent.setClass(FunctionActivity.this,HuoQiuProgressActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    private List<String> getData(){

        data = new ArrayList<String>();
        data.add("时钟提醒");
        data.add("暂无");


        return data;
    }
}
