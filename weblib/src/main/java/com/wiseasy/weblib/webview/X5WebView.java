package com.wiseasy.weblib.webview;

import android.app.Activity;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
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
    private boolean loadFinished = false;
    private OnPageFinishedListener onPageFinishedListener;

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

    private void init(Context context) {
        this.context = context;
        initWebViewSettings();
        initOtherSetting();
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();

        //js
        webSetting.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSetting.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSetting.setLoadWithOverviewMode(true);// 缩放至屏幕的大小

        //缩放操作
        webSetting.setSupportZoom(false);//支持缩放，默认为true。是下面那个的前提。
//        webSetting.setBuiltInZoomControls(false);//设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSetting.setDisplayZoomControls(false);//隐藏原生的缩放控件

        webSetting.setAllowFileAccess(true);//设置可以访问文件
        webSetting.setSupportMultipleWindows(false);//多窗口

        webSetting.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        webSetting.setAppCacheEnabled(true);//开启 Application Caches 功能
        webSetting.setDatabaseEnabled(true);//开启 database storage API 功能
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);//设置  Application Caches 缓存大小
        webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());//设置  Application Caches 缓存目录
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());//设置 database storage 缓存目录
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());

        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);

        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().sync();
    }

    private void initOtherSetting() {

        //去除滚动条
        this.setHorizontalScrollBarEnabled(false);//水平不显示小方块
        this.setVerticalScrollBarEnabled(false);//垂直不显示小方块

        IX5WebViewExtension x5WebViewExtension = this.getX5WebViewExtension();
        if (x5WebViewExtension != null) {
            x5WebViewExtension.setScrollBarFadingEnabled(false);
        }

        //屏蔽长按事件
        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }

    private void initClient(){
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return false;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                Log.i("WebViewManager", "onPageFinished: ");
                if(onPageFinishedListener != null){
                    onPageFinishedListener.onPageFinished();
                }
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                X5WebView.this.onReceivedError();
                Log.i("WebViewManager", "onReceivedError: ");
            }
        });

        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                Log.i("WebViewManager", "progress: " + progress);
                loadFinished = progress == 100;
            }
        });
    }

    @Override
    public void loadUrl(String s) {
        if (!s.equals(loadedUrl)) {
            loadedUrl = s;
            super.loadUrl(s);
        }else if(loadFinished && onPageFinishedListener != null) {
            onPageFinishedListener.onPageFinished();
        }
    }

    public void attach(Activity activity) {
        ((MutableContextWrapper) this.getContext()).setBaseContext(activity);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }else {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
        }

        initClient();

        registerJSApi();
    }

    public void detach() {
        MutableContextWrapper ct = (MutableContextWrapper) this.getContext();
        ct.setBaseContext(this.getContext().getApplicationContext());
        setWebChromeClient(null);
        setWebViewClient(null);
        onPageFinishedListener = null;
    }

    public void reset(){
        Log.i("WebViewManager", "reset: ");
        loadUrl("");
        stopLoading();
        clearCache(true);
        clearHistory();
    }

    private void registerJSApi(){
        JSBridge jsBridge = new JSBridge(this);
    }

    public void addUseTimes() {
        i++;
    }

    public int getUseTimes() {
        return i;
    }

    public void resetTimes() {
        i = 0;
    }

    public String getLoadedUrl() {
        return loadedUrl;
    }

    public void onReceivedError() {
        loadedUrl = null;
    }

    public boolean isLoadFinished(){
        return loadFinished;
    }

    public void setOnPageFinishedListener(OnPageFinishedListener onPageFinishedListener) {
        this.onPageFinishedListener = onPageFinishedListener;
    }

    public interface OnPageFinishedListener {
        void onPageFinished();
    }

}
