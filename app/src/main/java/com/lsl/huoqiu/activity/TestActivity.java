package com.lsl.huoqiu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsl.huoqiu.R;
import com.lsl.huoqiu.game.model.SingleCell;
import com.lsl.huoqiu.game.model.SingleGrid;
import com.lsl.huoqiu.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Forrest on 16/8/11.
 */
public class TestActivity extends AppCompatActivity {
    private TextView result;
    private Button button;
    StringBuffer buffer = new StringBuffer();
    public static String ACTION_INTENT_TEST = "com.lsl.huoqiu.test";
    public static String ACTION_INTENT_RECEIVER = "com.lsl.huoqiu.receiver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        result = (TextView) findViewById(R.id.result);
        button = (Button) findViewById(R.id.test_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getTestOne();
//              getTestTwo();
                getTestThree();
            }
        });
        //注册广播
        registerMessageReceiver();
    }
    private void getTestThree(){
        Intent mIntent = new Intent(ACTION_INTENT_TEST);
        sendBroadcast(mIntent);

    }
    public MessageReceiver mMessageReceiver;
    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();

        filter.addAction(ACTION_INTENT_RECEIVER);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                result.setText(intent.getStringExtra("message"));
            }
        }
    }

    private void getTestTwo(){
        SingleCell cell=new SingleCell(0,1);
        SingleCell vetocer=new SingleCell(0,1);
        SingleCell[] positions=findFarthestPosition(cell,vetocer);
        buffer.append(positions[0].getX()+"BBBB"+positions[0].getY()+"\n"
                +"第二个"+positions[1].getX()+"AAAA"+positions[1].getY());
        result.setText(buffer);
    }
    /**
     * 寻找最远的位置
     */
    private SingleCell[] findFarthestPosition(SingleCell cell, SingleCell vector) {
        grid=new SingleGrid(4,4);
        SingleCell previous;
        SingleCell nextCell = new SingleCell(cell.getX(), cell.getY());
        do {
            previous = nextCell;
            nextCell = new SingleCell(previous.getX() + vector.getX(),
                    previous.getY() + vector.getY());
        } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

        SingleCell[] answer = {previous, nextCell};
        return answer;
    }

    private void getTestOne() {

        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx < 10; xx++) {
            traversals.add(xx);
        }
        //测试结果为颠倒顺序
        Collections.reverse(traversals);//修改通过颠倒元素的顺序指定列表。
        for (int xx = 0; xx < 10; xx++) {

            buffer.append(traversals.get(xx) + "\n");
        }
        result.setText(buffer);
    }
    private SingleGrid grid=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}