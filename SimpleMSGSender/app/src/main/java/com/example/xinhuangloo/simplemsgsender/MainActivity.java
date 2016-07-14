package com.example.xinhuangloo.simplemsgsender;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
private  static  final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("a","开始检查权限");
                //检查权限
                //ContextCompat.checkSelfPermission，主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager
                        .PERMISSION_GRANTED)
                {
                    Log.d("a","没有授权");


                    //这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹出个授权框，你需要给用户一个结束，为什么授权，则使用该方法
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission
                            .SEND_SMS)){
                        //解释一下
                        Toast.makeText(MainActivity.this, "快点给我授权啊", Toast.LENGTH_SHORT).show();


                        //如果没有授权，申请授权
                        //该方法是异步的，第一个参数是Context；第二个参数是需要申请的权限的字符串数组；第三个参数为requestCode，主要用于回调的时候检测。可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
                        ActivityCompat.requestPermissions(MainActivity.this,new  String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);



                    } else{

                        //如果没有授权，申请授权
                        //该方法是异步的，第一个参数是Context；第二个参数是需要申请的权限的字符串数组；第三个参数为requestCode，主要用于回调的时候检测。可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
                        ActivityCompat.requestPermissions(MainActivity.this,new  String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }


                } else {
                    Log.d("a","已经授权");

                    //如果已经授权
                    //调用发送消息的函数
                    sendMSG();
                }
            }
        });

    }

    public void sendMSG(){

        Log.d("a","发送消息");

        //获取用户输入的电话号码和内容
        EditText input = (EditText) findViewById(R.id.input);
        String num = input.getText().toString();

        EditText mainText = (EditText) findViewById(R.id.mainText);
        String sendData = mainText.getText().toString();


        Log.d("a","电话号码"+num);
        Log.d("a","发送内容"+sendData);

        //直接使用发送短信的api，无需启动系统的短信应用
        SmsManager sm = SmsManager.getDefault();
        //把长短信截成若干条短短信
        ArrayList<String> sms = sm.divideMessage(sendData);
        for (String strng : sms)
        {
            //第一个参数：目标号码
            // 第二个参数：短信中心号码，null表示哦使用默认
            //第三个参数：短信正文
            sm.sendTextMessage(num, null, strng, null, null);
        }


    }


    //处理申请权限回调
    //对于权限的申请结果，首先验证requestCode定位到你的申请，然后验证grantRequest对应于申请的结果，这里的数组对应于申请时的第二个权限字符串数组。如果你同时申请两个权限，那么grantResult
    // 的length就为2，分别记录你两个权限的申请结果。如果申请成功，就可以做你的事情了
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        Log.d("a","申请权限的回调");

        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS)
        {
            Log.d("a","发送短信的权限代码");

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d("a","用户点了给权限");

                sendMSG();
            } else
            {
                Log.d("a","用户没有给权限");

                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
