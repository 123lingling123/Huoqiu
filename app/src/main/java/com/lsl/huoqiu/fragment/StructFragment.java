package com.lsl.huoqiu.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.activity.ui.PagerSlidingTabActivity;
import com.lsl.huoqiu.widget.PagerSlidingTabStrip;
import com.lsl.huoqiu.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Forrest on 16/9/23.
 */
public class StructFragment extends Fragment implements PullToRefreshListView.OnRefreshListener {
    private TextView mText;
    private ArrayAdapter<String> adapter;
    private PullToRefreshListView listView;
    List<String> data = new ArrayList<String>();
    private PagerSlidingTabActivity activity;
    private int id;

    //初始化传递参数
    public  static  StructFragment newInstance(Bundle args){
        StructFragment fragment=new StructFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity= (PagerSlidingTabActivity) getActivity();
        View view =inflater.inflate(R.layout.fragment_struct,null);
        listView= (PullToRefreshListView) view.findViewById(R.id.struct_listview);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1, getData());
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);




        mText= (TextView) view.findViewById(R.id.fragment_text);
        Bundle bundle=getArguments();
        Log.e("SturctFragmen","");
        if (bundle!=null){
            id=bundle.getInt("id");
            //简单的模拟，如果第一次创建这个Fragment就用Activity传递过来的数据，否则就用新的数据
            if (activity.getStartId()==bundle.getInt("id")){
                if (!TextUtils.isEmpty(activity.getStartDate())) {
                    mText.setText(activity.getStartDate());
                }else {
                    mText.setText( bundle.getString("text"));
                }
            }
//            mText.setText( bundle.getString("text"));
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mText.setText("我被点击了");
                }
            });
            Log.e("SturctFragmen",bundle.getString("text"));
        }else {
            Log.e("SturctFragmen","空");
        }


        return view;
    }

    @Override
    public void onRefresh() {
        new CountDownTimer(2000,1){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                listView.onRefreshComplete();

            }
        }.start();

        Toast.makeText(getActivity(),"shuaxian",Toast.LENGTH_SHORT).show();

    }

    private List<String> getData(){

        data = new ArrayList<String>();
        data.add("HuoQiuProgress");
        data.add("HuoQiuProgressActivity");
        data.add("HuoQiuMainActivity");
        data.add("FengActivity");
        data.add("VerticalViewPagerActivity");
        data.add("GussBitmap");
        data.add("GussLayout");
        data.add("PullZoomActivity");
        data.add("MultiViewPagerActivity");

        return data;
    }

    @Override
    public void onResume() {
        super.onResume();
        //只有当目前的id的时候才会刷新数据
        if (id==activity.getCurrentId()){
            mText.setText("刷新数据"+id);
        }
    }
}
