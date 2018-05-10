package com.example.downn.test6;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MSG_SUCCESS = 0;//获取图片成功的标识
    private static final int MSG_FAILURE = 1;//获取图片失败的标识
    private Thread mThread;

    private Button button;
    private TextView text;

    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case MSG_SUCCESS:
                    text.setText((String) msg.obj);//imageview显示从网络获取到的logo
                    Toast.makeText(getApplication(), "表演成功",
                            Toast.LENGTH_LONG).show();
                    break;

                case MSG_FAILURE:
                    Toast.makeText(getApplication(), "并没有信息，表演失败",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mThread == null) {
                    mThread = new Thread(runnable);
                    mThread.start();//线程启动
                }
                else {
                    Toast.makeText(getApplication(), "线程启动", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    Runnable runnable = new Runnable() {

        @Override
        public void run() {//run()在新的线程中运行
            String info;
            try {
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                String name = pref.getString("name","没有输入");
                String id = pref.getString("id","没有输入");
                String age = pref.getString("age","没有输入");
                if(name.isEmpty()) name = "没有输入";
                if(id.isEmpty()) id = "没有输入";
                if(age.isEmpty()) age = "没有输入";

               info = ("姓名："+name+"\n"+"学号："+id+"\n"+"年龄："+age);
            } catch (Exception e) {
                mHandler.obtainMessage(MSG_FAILURE).sendToTarget();//获取图片失败
                return;
            }
            mHandler.obtainMessage(MSG_SUCCESS,info).sendToTarget();//获取图片成功，向ui线程发送MSG_SUCCESS标识和bitmap对象

        }
    };
}
