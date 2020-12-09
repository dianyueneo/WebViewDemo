package com.wiseasy.weblib.webview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.wiseasy.weblib.commands.CommandDispatcher;

class JSBridge implements JsResponseCallback {

    private X5WebView mWebView;

    public JSBridge(X5WebView mWebView) {
        this.mWebView = mWebView;
        mWebView.addJavascriptInterface(this, "Native");
    }

    /**
     * 异步接口
     * 此方法在JavaBridge线程运行
     */
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void call(String cmd, String param){
        CommandDispatcher.getInstance().dispatchJSRequest(mWebView.getContext(), cmd, param, this);
    }

    /**
     * 同步接口
     * 此方法在JavaBridge线程运行
     */
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public String callSync(String cmd, String param){
        return CommandDispatcher.getInstance().dispatchJSRequest(mWebView.getContext(), cmd, param);
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
