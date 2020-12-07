package com.wiseasy.webviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wiseasy.weblib.CommandDispatcher;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        WebView webView = findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/aidl.html");

        webView.addJavascriptInterface(new JsRemoteInterface(), "webview");

    }

    public final class JsRemoteInterface {

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void post(String cmd, String param){
            Log.i("WebViewManager", "cmd: "+ cmd + " params: "+ param);
        }

    }
}