package com.lsl.huoqiu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lsl.huoqiu.activity.TestActivity;
import com.lsl.huoqiu.activity.ui.MainActivity;

/**
 * Created by Forrest on 16/8/11.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("BROADCAST_TAG", "onReceive");
        Bundle bundle = intent.getExtras();
        if(intent.getAction().equals(TestActivity.ACTION_INTENT_TEST))
        {
            processCustomMessage(context, bundle);
        }
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {

        Intent mIntent=new Intent(TestActivity.ACTION_INTENT_RECEIVER);
        mIntent.putExtra("message", "测试Broadcast与Activity之间的通信");
        context.sendBroadcast(mIntent);

    }
}
