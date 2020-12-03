package com.wiseasy.weblib;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }
}
