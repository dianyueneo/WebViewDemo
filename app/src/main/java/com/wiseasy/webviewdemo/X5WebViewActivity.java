package com.wiseasy.webviewdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wiseasy.weblib.CommandDispatcher;

import java.net.URL;

public class X5WebViewActivity extends AppCompatActivity{

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

        CommandDispatcher.getInstance().init();
    }

    private void init() {

        String name = getIntent().getStringExtra("name");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(name);

        mViewParent = findViewById(R.id.webView1);
        findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView = WebViewManager.getInstance().get(mIntentUrl.toString());
        mWebView.attach(this);

        if(mIntentUrl.toString().equals(mWebView.getLoadedUrl()) && mWebView.isLoadFinished()){
            loadWebView();
        } else  {
            TextView tv_name = findViewById(R.id.tv_name);
            tv_name.setText(name);
        }

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


            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                mWebView.onReceivedError();
            }
        });

        mWebView.addJavascriptInterface(new JsRemoteInterface(), "webview");

    }

    public final class JsRemoteInterface {

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void post(String cmd, String param){
            CommandDispatcher.getInstance().exec(X5WebViewActivity.this, cmd, param, mWebView);
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