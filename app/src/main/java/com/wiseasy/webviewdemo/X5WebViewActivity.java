package com.wiseasy.webviewdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.net.URL;

public class X5WebViewActivity extends AppCompatActivity {

    private static final int Msg_What_loadWebView = 1;

    private X5WebView mWebView;
    private URL mIntentUrl;
    private ViewGroup mViewParent;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case Msg_What_loadWebView:
                    loadWebView();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getStringExtra("url"));
            } catch (Exception e) {
            }
        }


        init();

    }

    private void init() {

        mViewParent = findViewById(R.id.webView1);
        findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView = WebViewManager.getInstance().get(mIntentUrl.toString());
        mWebView.attach(this);
        mWebView.loadUrl(mIntentUrl.toString());

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return false;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                handler.sendEmptyMessage(Msg_What_loadWebView);
            }
        });

        if(mIntentUrl.toString().equals(mWebView.getOriginalUrl())){
            loadWebView();
        }

    }

    private void loadWebView(){
        mViewParent.removeAllViews();
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.detach();
    }
}