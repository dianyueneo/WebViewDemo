package com.wiseasy.webviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wiseasy.weblib.WiseasySmallProgram;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_webview= findViewById(R.id.btn_webview);
        Button btn_webview_process= findViewById(R.id.btn_webview_process);
        Button btn_1= findViewById(R.id.btn_1);
        Button btn_2= findViewById(R.id.btn_2);
        Button btn_3= findViewById(R.id.btn_3);
        Button btn_4= findViewById(R.id.btn_4);
        Button btn_5= findViewById(R.id.btn_5);


        btn_webview.setOnClickListener(this);
        btn_webview_process.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_webview:
                Intent intent2 = new Intent(this, WebViewActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_webview_process:
                Intent intent3 = new Intent(this, WebViewProcessActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_1:
                WiseasySmallProgram.start(this, "http://mc.vip.qq.com/demo/indexv3", "吉野家");
                break;
            case R.id.btn_2:
                WiseasySmallProgram.start(this, "http://www.baidu.com", "百度");
                break;
            case R.id.btn_3:
                WiseasySmallProgram.start(this, "file:///android_asset/aidl.html", "AIDL测试");
                break;
            case R.id.btn_4:
                WiseasySmallProgram.start(this, "https://new.qq.com/rain/a/20201202A06V2A00", "人民日报");
                break;
            case R.id.btn_5:
                WiseasySmallProgram.start(this, "https://new.qq.com/rain/a/20201201A0FE3W00", "侠客岛");
                break;
            default:
                break;
        }

    }
}