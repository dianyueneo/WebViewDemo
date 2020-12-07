package com.wiseasy.webviewdemo;

import com.wiseasy.weblib.BaseApplication;
import com.wiseasy.weblib.WiseasySmallProgram;
import com.wiseasy.webviewdemo.commands.MainProcessCommands;
import com.wiseasy.webviewdemo.commands.WebViewProcessCommands;


public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        WiseasySmallProgram.init(this);
        WiseasySmallProgram.registMainProcessCommands(this, new MainProcessCommands());
        WiseasySmallProgram.registWebViewProcessCommands(this, new WebViewProcessCommands());

    }



}
