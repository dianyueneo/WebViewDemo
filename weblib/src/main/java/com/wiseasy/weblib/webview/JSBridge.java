package com.wiseasy.weblib.webview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.wiseasy.weblib.CommandDispatcher;
import com.wiseasy.weblib.JsResponseCallback;

class JSBridge implements JsResponseCallback {

    private X5WebView mWebView;

    public JSBridge(X5WebView mWebView) {
        this.mWebView = mWebView;
        mWebView.addJavascriptInterface(this, "webview");
    }


    //此方法在JavaBridge线程运行
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void post(String action, String param){
        dealAction(action, param);
    }


    private void dealAction(final String action, final String param) {
        CommandDispatcher.getInstance().exec(mWebView.getContext(), action, param, this);
    }

    @Override
    public void handleCallback(final String response) {
        Log.i("WebViewManager", "js 返回结果：" + response);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                dealResponse(response);
            }
        });
    }

    private void dealResponse(String response) {
        if(!TextUtils.isEmpty(response)){
            String trigger = "javascript:" + "dj.callback" + "(" + response + ")";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(trigger, null);
            } else {
                mWebView.loadUrl(trigger);
            }
        }
    }
}
