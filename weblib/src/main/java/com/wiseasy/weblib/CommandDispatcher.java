package com.wiseasy.weblib;

import android.content.Context;
import android.util.Log;

public class CommandDispatcher {

    private CommandDispatcher(){}

    private static class Holder{
        private static final CommandDispatcher instance = new CommandDispatcher();
    }

    public static CommandDispatcher getInstance(){
        return Holder.instance;
    }



    protected IWebAidlInterface webAidlInterface;

    public void init(){
        webAidlInterface = RemoteWebBinder.getInstance().getBinder();
    }

    public void exec(Context context, String cmd, String params, JsRemoteCallback callback){
        Log.i("WebViewManager", "cmd: "+ cmd + " params: "+ params);
    }



}
