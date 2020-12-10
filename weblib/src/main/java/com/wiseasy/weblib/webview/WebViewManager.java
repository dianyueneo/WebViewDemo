package com.wiseasy.weblib.webview;

import android.app.Application;
import android.content.MutableContextWrapper;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.HashMap;

class WebViewManager {

    private static final String PreLoad = "PreLoad";
    private static final int MaxSize = 2;
    private HashMap<String, X5WebView> maps = new HashMap<>(MaxSize);//复用池

    private WebViewManager(){}

    private static class Holder{
        private static final WebViewManager instance = new WebViewManager();
    }

    public static WebViewManager getInstance(){
        return Holder.instance;
    }

    /**
     * 预加载, 如果x5没有初始化成功则生成原生webview
     * 如果预加载的原生webview没有使用则删除
     */
    public void preLoad(Application application){
        if(maps.size() < MaxSize){
            if(maps.get(PreLoad) != null){
                Log.i("WebViewManager", "remove unused Native WebView");
                maps.remove(PreLoad);
            }
            createWebView(application, PreLoad);
        }
    }

    private void createWebView(Application application, String key){

        Log.i("WebViewManager", "create X5WebView");

        X5WebView mWebView = new X5WebView(new MutableContextWrapper(application));

        maps.put(key, mWebView);

    }

    public X5WebView get(Application application, String url){

        long p = System.currentTimeMillis();

        for (String s : maps.keySet()) {
            Log.i("WebViewManager", "webview in pool : " + s);
        }

        X5WebView targetWebView = maps.get(url);
        if(targetWebView != null){
            cleanWebView(targetWebView);
            targetWebView.addUseTimes();
            long n = System.currentTimeMillis();
            Log.i("WebViewManager", "testWebViewFirstInit use time:" + (n-p));
            return targetWebView;
        }

        X5WebView cachedWebView = maps.get(PreLoad);
        if(cachedWebView != null){
            replaceKey(PreLoad, url);
            long n = System.currentTimeMillis();
            Log.i("WebViewManager", "testWebViewFirstInit use time:" + (n-p));
            return cachedWebView;
        }

        if(maps.size() < MaxSize){
            createWebView(application, url);
        }else {
            String oldKey = getMiniTimesWebViewKey();
            replaceKey(oldKey, url);
        }

        long n = System.currentTimeMillis();
        Log.i("WebViewManager", "testWebViewFirstInit use time:" + (n-p));

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
