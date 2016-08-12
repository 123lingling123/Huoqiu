package com.lsl.huoqiu.activity.function;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsl.huoqiu.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Forrest on 16/7/13.
 */
public class OKHttpActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mButtonStart;
    private TextView mText;
    private TextView mTextTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok);
        mButtonStart= (Button) findViewById(R.id.button_start);
        mText= (TextView) findViewById(R.id.content);
        mTextTest= (TextView) findViewById(R.id.test);
        mTextTest.setText("我是\n森林");
        mTextTest.setText(Html.fromHtml("预计\n到账金额为<font color='#ff5722'>0.00</font>元，提现\n\n\n\n手续费由<font color='#ff5722'>火球理财</font>支付"));

        mButtonStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                sendRequest();
                break;
        }
    }


    private void sendRequest(){
        //创建一个OKHTTP对象
        OkHttpClient mOkHttpClient=new OkHttpClient();
        //设置超时
        mOkHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30,TimeUnit.SECONDS);

//        mOkHttpClient.setSslSocketFactory();
        //创建一个请求
        Request request=new Request.Builder()
                .url("http://www.huoqiu.cn/mobile/app/product/hots").build();

        Call call=mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }
            //返回字符串response.body().string()
            //返回二进制字节数组response.body().bytes()
            //返回inputStream则response.body().byteStream()
            @Override
            public void onResponse(final Response response) throws IOException {
                //OnResPonse并不是在主线程中的因此控制控件需要调用主线程
                
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mText.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
