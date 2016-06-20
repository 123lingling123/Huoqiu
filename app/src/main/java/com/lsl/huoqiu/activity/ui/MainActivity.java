package com.lsl.huoqiu.activity.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lsl.huoqiu.widget.HuoqiuProgress;
import com.lsl.huoqiu.widget.MyProgress;
import com.lsl.huoqiu.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 火球的进度条展示，在结构化理财界面
 * 第一个是钟老师写的Demo
 */
public class MainActivity extends AppCompatActivity {

    private int progress= 0;
    MyProgress myprogress;
    HuoqiuProgress huoqiu;
    /** Magic number for current version of cache file format. */
    private static final int CACHE_MAGIC = 0x20120504;
    private Button button;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x23:
                    if(progress<100) {
                        progress++;
                    }else{
                        progress= 0;
                    }
                    myprogress.setProgress(progress);
                    handler.sendEmptyMessageDelayed(0x23,100);
                    break;
            }
        }
    };
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myprogress = (MyProgress) findViewById(R.id.myprogress);
        huoqiu= (HuoqiuProgress) findViewById(R.id.huoqiu);
        button= (Button) findViewById(R.id.button);
    File file=new File(getCacheDir(),"word.txt");
    try {
        FileOutputStream out = new FileOutputStream(file);
        byte buy[] = "我有一只小毛驴，我从来也不骑。".getBytes();
        writeInt(out, CACHE_MAGIC);
        out.write(buy);
        out.close(); // 将流关闭
    }catch (Exception e)
    {
        e.printStackTrace();
    }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a++;
                if (a>10){
                    a+=10;
                }
                if (a>99){
                    a=0;
                }
                huoqiu.setProgress(a);
            }
        });
        handler.sendEmptyMessage(0x23);
    }
    static void writeInt(OutputStream os, int n) throws IOException {
        os.write((n >> 0) & 0xff);
        int a=(n >> 0) & 0xff;
        Log.d("右移之后N的值", "" + a);
        os.write((n >> 8) & 0xff);
        int b=(n >> 8) & 0xff;
        Log.d("右移之后N的值", "" + b);
        os.write((n >> 16) & 0xff);
        int c=(n >> 16) & 0xff;
        Log.d("右移之后N的值", "" + c);
        os.write((n >> 24) & 0xff);
        int d=(n >> 24) & 0xff;
        Log.d("右移之后N的值", "" + d);
    }
//    File file=new File(getCacheDir(),"word.txt");
//    try {
//        FileOutputStream out = new FileOutputStream(file);
//        byte buy[] = "我有一只小毛驴，我从来也不骑。".getBytes();
//        out.write(buy);
//        out.close(); // 将流关闭
//    }
//    catch (Exception e)
//    {
//        e.printStackTrace();
//    }
//    try {
//        FileInputStream in = new FileInputStream(file);
//        byte byt[] = new byte[1024];
//        int len = in.read(byt);
////00100000 00010010 00000101 00000100
//        System.out.println("文件中的信息是：" + new String(byt, 0, len));
//        System.out.println("前一段字节：" + new String(byt, 0, 20));
//        System.out.println("后一段字节：" + new String(byt, 20, 45));
//        System.out.println("创建数组的长度"+byt.length+"读取的长度"+len);
//
//        in.close();
//    }
//    catch (Exception e) {
//        e.printStackTrace();
//    }
}
