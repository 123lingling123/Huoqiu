package com.lsl.huoqiu.activity.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.huoqiu.AppContext;
import com.lsl.huoqiu.R;
import com.lsl.huoqiu.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/5/4.
 */
public class VerticalViewPagerActivity extends AppCompatActivity {
    private LinearLayout linear_contain;
    private ListView mListView;
    private List<String > mData=new ArrayList<String>();
    private IndexAdapter mAdapter;
    private static final int AUTO_MEG = 606;
    private static final int FAN_MEG = 607;
    private static final long AUTO_DELAY = 2000;
    private int titleHeight= DeviceUtils.getWindowHeight(AppContext.getInstance())/10;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            if (mAdapter!=null){
                if (mData!=null&&mData.size()>1){
                    switch (msg.what){
                        case AUTO_MEG:
                            if (mListView.getLastVisiblePosition()==mData.size()-1){
//                                mListView.smoothScrollToPosition(0);
                                sendEmptyMessageDelayed(FAN_MEG, AUTO_DELAY);
                            }else {
                                mListView.smoothScrollBy(titleHeight,1000);
                                sendEmptyMessageDelayed(AUTO_MEG, AUTO_DELAY);
                            }

                            break;
                        case FAN_MEG:
                            if (mListView.getLastVisiblePosition()==0){
                                sendEmptyMessageDelayed(AUTO_MEG, AUTO_DELAY);
                            }else {
                                mListView.smoothScrollBy(-titleHeight, 1000);
                                sendEmptyMessageDelayed(FAN_MEG, AUTO_DELAY);
                            }

                            break;
                    }


                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_viewpager);
        linear_contain= (LinearLayout) findViewById(R.id.linear_contain);

        mData.add(new String("第一"));
        mData.add(new String("第er"));
        mData.add(new String("第san"));
        mData.add(new String("第si"));
        initListView();

    }

    private void initListView() {
        mListView=new ListView(this);
        mListView.setEnabled(false);//设置不可点击
        mAdapter=new IndexAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        mListView.setVerticalScrollBarEnabled(false);
        linear_contain.addView(mListView, LinearLayout.LayoutParams.MATCH_PARENT,
                DeviceUtils.getWindowHeight(AppContext.getInstance())/10);
        handler.removeMessages(AUTO_MEG);
        handler.sendEmptyMessageDelayed(AUTO_MEG, AUTO_DELAY);
    }

    private class IndexAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mData==null?0:mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view;
            final String bean=mData.get(position);
            if (convertView!=null){
                view= (TextView) convertView;
            }else {
                view=new TextView(getApplicationContext());
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        titleHeight+1);
                view.setGravity(Gravity.CENTER_VERTICAL);
                view.setLayoutParams(params);
                view.setBackgroundColor(Color.WHITE);
                view.setTextColor(Color.BLACK);
            }
            view.setText(bean);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VerticalViewPagerActivity.this,bean,Toast.LENGTH_SHORT).show();
                }
            });


            return view;
        }
    }
}
