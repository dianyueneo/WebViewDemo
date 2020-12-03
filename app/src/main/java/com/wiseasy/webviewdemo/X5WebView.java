package com.wiseasy.webviewdemo;

import android.app.Activity;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

class X5WebView extends WebView {

    private int i;//当前使用次数
    private Context context;
    private String loadedUrl;

    public X5WebView(Context context) {
        super(context);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        initWebViewSettings();
        initOtherSetting();
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);

        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().sync();
    }

    private void initOtherSetting(){

        //去除滚动条
        this.setHorizontalScrollBarEnabled(false);//水平不显示小方块
        this.setVerticalScrollBarEnabled(false);//垂直不显示小方块

        IX5WebViewExtension x5WebViewExtension = this.getX5WebViewExtension();
        if(x5WebViewExtension != null){
            x5WebViewExtension.setScrollBarFadingEnabled(false);
        }

        //屏蔽长按事件
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        this.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return false;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                Log.i("WebViewManager", "onPageFinished: ");
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                X5WebView.this.onReceivedError();
            }
        });

        this.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsConfirm(webView, s, s1, jsResult);
            }

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onProgressChanged(WebView webView, int progress) {
                Log.i("WebViewManager", "progress: " + progress);
            }
        });
    }

    @Override
    public void loadUrl(String s) {
        if(!s.equals(loadedUrl)){
            loadedUrl = s;
            super.loadUrl(s);
        }
    }

    public void attach(Activity activity){
        ((MutableContextWrapper) this.getContext()).setBaseContext(activity);

        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                activity.getWindow().setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
    }

    public void detach(){
        MutableContextWrapper ct = (MutableContextWrapper) this.getContext();
        ct.setBaseContext(BaseApplication.context);
    }

    public void addUseTimes(){
        i++;
    }

    public int getUseTimes(){
        return i;
    }

    public void resetTimes(){
        i = 0;
    }

    public String getLoadedUrl(){
        return loadedUrl;
    }

    public void onReceivedError(){
        loadedUrl = null;
    }
}
