package com.lsl.huoqiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.activity.ui.AutoDrawable;
import com.lsl.huoqiu.activity.ui.FengActivity;
import com.lsl.huoqiu.activity.ui.GussActivity;
import com.lsl.huoqiu.activity.ui.GussLayoutActivity;
import com.lsl.huoqiu.activity.ui.HqDialogActivity;
import com.lsl.huoqiu.activity.ui.HqPlanRoundActivity;
import com.lsl.huoqiu.activity.ui.HqTextActivity;
import com.lsl.huoqiu.activity.ui.HuoQiuMainActivity;
import com.lsl.huoqiu.activity.ui.HuoQiuProgressActivity;
import com.lsl.huoqiu.activity.ui.MainActivity;
import com.lsl.huoqiu.activity.ui.MultiViewPagerActivity;
import com.lsl.huoqiu.activity.ui.PercentViewActivity;
import com.lsl.huoqiu.activity.ui.PullZoomActivity;
import com.lsl.huoqiu.activity.ui.StructDetaliProgressActivity;
import com.lsl.huoqiu.activity.ui.VerticalViewPagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/20.
 */
public class UIActivity extends AppCompatActivity{
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
                    intent.setClass(UIActivity.this,MainActivity.class);
                }else if (position==1){
                    intent.setClass(UIActivity.this,HuoQiuProgressActivity.class);
                }else if (position==2){
                    intent.setClass(UIActivity.this,HuoQiuMainActivity.class);
                }else if (position==3){
                    intent.setClass(UIActivity.this,FengActivity.class);
                }else if (position==4){
                    intent.setClass(UIActivity.this,VerticalViewPagerActivity.class);
                }else if (position==5){
                    intent.setClass(UIActivity.this,GussActivity.class);
                }else if (position==6){
                    intent.setClass(UIActivity.this,GussLayoutActivity.class);
                }else if (position==7){
                    intent.setClass(UIActivity.this,PullZoomActivity.class);
                }else if (position==8){
                    intent.setClass(UIActivity.this,HqPlanRoundActivity.class);
                }else if (position==9){
                    intent.setClass(UIActivity.this,MultiViewPagerActivity.class);
                }else if (position==10){
                    intent.setClass(UIActivity.this,HqDialogActivity.class);
                }else if (position==11){
                    intent.setClass(UIActivity.this,HqTextActivity.class);
                }else if (position==12){
                    intent.setClass(UIActivity.this,AutoDrawable.class);
                }else if (position==13){
                    intent.setClass(UIActivity.this,StructDetaliProgressActivity.class);
                }else if (position==14){
                    intent.setClass(UIActivity.this,PercentViewActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    private List<String> getData(){

        data = new ArrayList<String>();
        data.add("火球结构化理财进度条");
        data.add("饼状图（可用hellochart替代）");
        data.add("自定义的RadioButton红点");
        data.add("返回卡路里的数据");
        data.add("垂直的ViewPager显示");
        data.add("高斯模糊Demo");
        data.add("高斯模糊Layout");
        data.add("仿QQ控件下拉效果");
        data.add("火球计划UI效果");
        data.add("ViewPager缩放实现画廊效果");
        data.add("火球复投换地方的弹窗效果");
        data.add("字符串中有数字就变大的效果");
        data.add("代码设置Drawable");
        data.add("结构化在投详情柱状图");
        data.add("击败百分比");

        return data;
    }
}
