package com.wiseasy.weblib.webview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class AgentWebView implements LifecycleObserver {

    private ComponentActivity activity;
    private ViewGroup parent;
    private ViewGroup.LayoutParams layoutParams;
    private View loadingView;
    private Boolean useCache;
    private String url;

    private X5WebView webView;

    private AgentWebView(Builder builder){
        activity = builder.activity;
        parent = builder.parent;
        layoutParams = builder.layoutParams;
        loadingView = builder.loadingView;
        useCache = builder.useCache;
        url = builder.url;

        init();
    }

    private void init(){

        activity.getLifecycle().addObserver(this);

        if(loadingView != null){
            parent.addView(loadingView, layoutParams);
        }

        webView = WebViewManager.getInstance().get(activity.getApplication(), url);
        webView.attach(activity);

        webView.setOnPageFinishedListener(new X5WebView.OnPageFinishedListener() {
            @Override
            public void onPageFinished() {
                loadWebView(webView);
            }
        });

        webView.loadUrl(url);
    }

    private void loadWebView(X5WebView webView){
        parent.removeAllViews();
        parent.addView(webView, layoutParams);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        Log.i("WebViewManager", "X5WebViewActivity ON_DESTROY ");
        activity.getLifecycle().removeObserver(this);
        webView.detach();
        if(!useCache){
            webView.reset();
        }
    }

    public static class Builder{

        private ComponentActivity activity;
        private ViewGroup parent;
        private ViewGroup.LayoutParams layoutParams;
        private View loadingView;
        private Boolean useCache;
        private String url;

        public Builder with(ComponentActivity activity){
            this.activity = activity;
            return this;
        }

        public Builder setLayoutParams(ViewGroup.LayoutParams layoutParams){
            this.layoutParams = layoutParams;
            return this;
        }

        public Builder setWebViewParent(ViewGroup parent){
            this.parent = parent;
            return this;
        }

        public Builder setLoadingView(View loadingView){
            this.loadingView = loadingView;
            return this;
        }

        public Builder setUseCache(boolean useCache){
            this.useCache = useCache;
            return this;
        }

        public AgentWebView load(String url){
            this.url = url;
            return new AgentWebView(this);
        }
    }

}
