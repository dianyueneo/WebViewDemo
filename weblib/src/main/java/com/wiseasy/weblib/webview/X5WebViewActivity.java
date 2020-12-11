package com.wiseasy.weblib.webview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.wiseasy.weblib.R;

import java.net.URL;

public class X5WebViewActivity extends AppCompatActivity{

    private URL mIntentUrl;
    private boolean cache;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("WebViewManager", "X5WebViewActivity onCreate "+ X5WebViewActivity.this);
        setContentView(R.layout.activity_x5webview);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getStringExtra("url"));
            } catch (Exception e) {
            }
        }
        cache = intent.getBooleanExtra("cache", true);

        init();


    }

    private void init() {

        String name = getIntent().getStringExtra("name");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(name);

        ViewGroup mViewParent = findViewById(R.id.webView1);
        findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View loadingView = LayoutInflater.from(this).inflate(R.layout.view_loading, null);


        new AgentWebView.Builder()
                .with(this)
                .setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .setWebViewParent(mViewParent)
                .setLoadingView(loadingView)
                .setUseCache(cache)
                .load(mIntentUrl.toString());

    }


}