package com.wiseasy.webviewdemo;

import android.content.Context;

import androidx.multidex.MultiDexApplication;
import com.wiseasy.weblib.WiseasySmallProgram;
import com.wiseasy.webviewdemo.commands.MainProcessCommands;
import com.wiseasy.webviewdemo.commands.WebViewProcessCommands;


public class MyApplication extends MultiDexApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        WiseasySmallProgram.init(this);
        WiseasySmallProgram.registerMainProcessCommands(this, new MainProcessCommands());
        WiseasySmallProgram.registerWebViewProcessCommands(this, new WebViewProcessCommands());

    }



}
