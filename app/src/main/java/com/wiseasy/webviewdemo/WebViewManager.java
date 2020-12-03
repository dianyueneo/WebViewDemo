package com.wiseasy.webviewdemo;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.HashMap;

class WebViewManager {

    private static final String PreLoad = "PreLoad";
    private static final int MaxSize = 2;
    private static WebViewManager manager;
    private HashMap<String, X5WebView> maps = new HashMap<>(MaxSize);//复用池
    private Context context = BaseApplication.context;

    private WebViewManager(){}

    public  static WebViewManager getInstance(){
        if(manager == null){
            manager = new WebViewManager();
        }
        return manager;
    }

    /**
     * 预加载
     */
    public void preLoad(){
        if(maps.size() < MaxSize){
            createWebView(PreLoad);
        }
    }

    private void createWebView(String key){

        Log.i("WebViewManager", "create X5WebView");

        MutableContextWrapper contextWrapper = new MutableContextWrapper(context);
        X5WebView mWebView = new X5WebView(contextWrapper);

        maps.put(key, mWebView);

    }

    public X5WebView get(String url){

        for (String s : maps.keySet()) {
            Log.i("WebViewManager", "webview: " + s);
        }

        X5WebView targetWebView = maps.get(url);
        if(targetWebView != null){
            cleanWebView(targetWebView);
            targetWebView.addUseTimes();
            return targetWebView;
        }

        X5WebView cachedWebView = maps.get(PreLoad);
        if(cachedWebView != null){
            replaceKey(PreLoad, url);
            return cachedWebView;
        }

        if(maps.size() < MaxSize){
            createWebView(url);
        }else {
            String oldKey = getMiniTimesWebViewKey();
            replaceKey(oldKey, url);
        }

        return maps.get(url);
    }

    private void replaceKey(String oldKey, String newKey){
        X5WebView x5WebView = maps.get(oldKey);
        x5WebView.resetTimes();
        cleanWebView(x5WebView);
        maps.remove(oldKey);
        maps.put(newKey, x5WebView);
    }

    private String getMiniTimesWebViewKey(){
        String targetKey = null;
        int max = Integer.MAX_VALUE;
        for (String key : maps.keySet()) {
            X5WebView x5WebView = maps.get(key);
            if(x5WebView.getUseTimes() < max){
                max = x5WebView.getUseTimes();
                targetKey = key;
            }
        }
        return targetKey;

    }


    private void cleanWebView(X5WebView mWebView){
        ViewParent parent = mWebView.getParent();
        if(parent != null){
            try {
                ((ViewGroup)parent).removeView(mWebView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
