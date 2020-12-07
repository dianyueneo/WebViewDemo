package com.wiseasy.weblib.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.wiseasy.weblib.R;

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
        Log.i("WebViewManager", "X5WebViewActivity onCreate "+ X5WebViewActivity.this);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getStringExtra("url"));
            } catch (Exception e) {
            }
        }


        init();

        CommandDispatcher.getInstance().init(this);
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

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                Log.i("WebViewManager", "shouldOverrideUrlLoading: " + s);
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

        mWebView.removeJavascriptInterface("webview");

        mWebView.addJavascriptInterface(new JsRemoteInterface(this), "webview");

        mWebView.loadUrl(mIntentUrl.toString());

    }

    public final class JsRemoteInterface {

        private Context context;

        public JsRemoteInterface(Context context) {
            this.context = context;
            Log.i("WebViewManager", "JsRemoteInterface : "+ context);
        }

        //此方法在JavaBridge线程运行
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void post(final String action, final String param){
            Log.i("WebViewManager", "JsRemoteInterface post: "+ context);
            dealAction(action, param);
        }

        private void dealAction(final String action, final String param) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommandDispatcher.getInstance().exec(context, action, param, mWebView);
                }
            });
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
        Log.i("WebViewManager", "X5WebViewActivity onDestroy "+ X5WebViewActivity.this);
    }

}