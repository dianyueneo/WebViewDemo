package com.wiseasy.weblib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wiseasy.weblib.commands.CommandDispatcher;
import com.wiseasy.weblib.commands.Commands;
import com.wiseasy.weblib.utils.SystemInfoUtil;
import com.wiseasy.weblib.webview.X5InitService;
import com.wiseasy.weblib.webview.X5WebViewActivity;

public class WiseasySmallProgram {

    /**
     * 在Application 中初始化
     * @param context
     */
    public static void init(Context context){
        if(SystemInfoUtil.inMainProcess(context)){
            preInitX5Core(context);
        }
    }

    /**
     * 注册在主进程中处理的命令
     * @param context
     * @param commands
     */
    public static void registerMainProcessCommands(Context context, Commands commands){
        if(SystemInfoUtil.inMainProcess(context)) {
            CommandDispatcher.getInstance().setMainProcessCommands(commands);
        }
    }

    /**
     * 注册在WebView进程中处理的命令
     * @param context
     * @param commands
     */
    public static void registerWebViewProcessCommands(Context context, Commands commands){
        if(SystemInfoUtil.isWebViewProcess(context)) {
            CommandDispatcher.getInstance().setWebViewProcessCommands(commands);
        }
    }

    private static void preInitX5Core(Context context) {
        Intent intent = new Intent(context, X5InitService.class);
        context.startService(intent);
    }

    /**
     * 启动小程序，默认有缓存
     * @param context
     * @param url
     * @param title
     */
    public static void start(Activity context, String url, String title){
        Intent intent = new Intent(context, X5WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("name", title);
        context.startActivity(intent);
    }

    /**
     * 启动小程序,无缓存
     * @param context
     * @param url
     * @param title
     */
    public static void startNoCache(Activity context, String url, String title){
        Intent intent = new Intent(context, X5WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("name", title);
        intent.putExtra("cache", false);
        context.startActivity(intent);
    }

}
