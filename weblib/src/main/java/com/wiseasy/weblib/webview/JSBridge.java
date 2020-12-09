package com.wiseasy.weblib.webview;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.wiseasy.weblib.commands.CommandDispatcher;
import com.wiseasy.weblib.commands.ResultCallback;

class JSBridge {

    private X5WebView mWebView;

    public JSBridge(X5WebView mWebView) {
        this.mWebView = mWebView;
        mWebView.addJavascriptInterface(this, "Native");
        CommandDispatcher.getInstance().init((Application) mWebView.getContext().getApplicationContext());
    }

    /**
     * 异步接口
     * 此方法在JavaBridge线程运行
     */
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void call(String cmd, String param, final String callbackId){
        CommandDispatcher.getInstance().dispatchJSRequest(mWebView.getContext(), cmd, param, new ResultCallback(){

            @Override
            public void handleCallback(final String response) {
                Log.i("WebViewManager", "js 返回结果：" + response);
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        dealResponse(response, callbackId);
                    }
                });
            }
        });
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


    private void dealResponse(String response, String callbackId) {
        if(!TextUtils.isEmpty(response)){
            String trigger = "javascript:" + "dj.callback" + "(" + response + ","+ callbackId +")";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(trigger, null);
            } else {
                mWebView.loadUrl(trigger);
            }
        }
    }
}
