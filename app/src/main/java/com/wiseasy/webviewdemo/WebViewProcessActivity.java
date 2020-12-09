package com.wiseasy.webviewdemo;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_process);

        WebView webView_1 = findViewById(R.id.webView_1);

        webView_1.getSettings().setJavaScriptEnabled(true);
        webView_1.setWebViewClient(new WebViewClient());
        webView_1.loadUrl("http://mc.vip.qq.com/demo/indexv3");
    }
}