package com.wiseasy.webviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_1= findViewById(R.id.btn_1);
        Button btn_2= findViewById(R.id.btn_2);
        Button btn_3= findViewById(R.id.btn_3);
        Button btn_4= findViewById(R.id.btn_4);
        Button btn_5= findViewById(R.id.btn_5);
        Button btn_6= findViewById(R.id.btn_6);
        Button btn_7= findViewById(R.id.btn_7);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_1:
                Intent intent = new Intent(this, EmptyActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_2:
                Intent intent2 = new Intent(this, EmptyWebviewActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_3:
                Intent intent3 = new Intent(this, X5WebViewActivity.class);
                intent3.putExtra("url", "http://mc.vip.qq.com/demo/indexv3");
                intent3.putExtra("name", "吉野家");
                startActivity(intent3);
                break;
            case R.id.btn_4:
                Intent intent4 = new Intent(this, X5WebViewActivity.class);
                intent4.putExtra("url", "http://www.baidu.com");
                intent4.putExtra("name", "百度");
                startActivity(intent4);
                break;
            case R.id.btn_5:
                Intent intent5 = new Intent(this, X5WebViewActivity.class);
                intent5.putExtra("url", "file:///android_asset/aidl.html");
                intent5.putExtra("name", "AIDL测试");
                startActivity(intent5);
                break;
            case R.id.btn_6:
                Intent intent6 = new Intent(this, X5WebViewActivity.class);
                intent6.putExtra("url", "https://new.qq.com/rain/a/20201202A06V2A00");
                intent6.putExtra("name", "人民日报");
                startActivity(intent6);
                break;
            case R.id.btn_7:
                Intent intent7 = new Intent(this, X5WebViewActivity.class);
                intent7.putExtra("url", "https://new.qq.com/rain/a/20201201A0FE3W00");
                intent7.putExtra("name", "侠客岛");
                startActivity(intent7);
                break;
            default:
                break;
        }

    }
}