package com.wiseasy.webviewdemo;

import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDexApplication;


public class BaseApplication extends MultiDexApplication {

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        preInitX5Core();

    }

    private void preInitX5Core(){
        Intent intent = new Intent(this, X5InitService.class);
        startService(intent);
    }
}
