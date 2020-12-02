package com.wiseasy.webviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EmptyWebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_webview);

        WebView webView_1 = findViewById(R.id.webView_1);

        webView_1.getSettings().setJavaScriptEnabled(true);
        webView_1.setWebViewClient(new WebViewClient());
        webView_1.loadUrl("http://mc.vip.qq.com/demo/indexv3");
    }
}