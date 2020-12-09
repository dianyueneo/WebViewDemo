package com.wiseasy.weblib.commands;

import android.app.Application;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wiseasy.weblib.IWebAidlCallback;
import com.wiseasy.weblib.IWebAidlInterface;
import com.wiseasy.weblib.remote.RemoteWebBinder;
import com.wiseasy.weblib.utils.SystemInfoUtil;

import java.util.Map;

public class CommandDispatcher {

    private CommandDispatcher(){}

    private static class Holder{
        private static final CommandDispatcher instance = new CommandDispatcher();
    }

    public static CommandDispatcher getInstance(){
        return Holder.instance;
    }


    private Gson gson = new Gson();
    private Commands mainProcessCommands;
    private Commands wevViewProcessCommands;

    /**
     * 需要在不同的进程中初始化
     * @param application
     */
    public void init(Application application){
        //WebView进程
        if(!SystemInfoUtil.inMainProcess(application)){
            RemoteWebBinder.getInstance().connectBinderService(application);
        }
    }

    public void setMainProcessCommands(Commands commands){
        this.mainProcessCommands = commands;
    }

    public void setWebViewProcessCommands(Commands commands){
        this.wevViewProcessCommands = commands;
    }

    public String dispatchJSRequest(Context context, String cmd, String params){
        Log.i("WebViewManager", "cmd: "+ cmd + " params: "+ params);
        Map mapParams = gson.fromJson(params, Map.class);

        if(wevViewProcessCommands != null && wevViewProcessCommands.getCommands().containsKey(cmd)){
            return wevViewProcessCommands.getCommands().get(cmd).exec(context, mapParams);
        }else {
            return getResponse("Fail, Unsupported features");
        }
    }

    public void dispatchJSRequest(Context context, String cmd, String params, final ResultCallback callback){
        Log.i("WebViewManager", "cmd: "+ cmd + " params: "+ params);
        Map mapParams = gson.fromJson(params, Map.class);

        //WebView进程
        if(!SystemInfoUtil.inMainProcess(context)){
            if(wevViewProcessCommands != null && wevViewProcessCommands.getCommands().containsKey(cmd)){
                wevViewProcessCommands.getCommands().get(cmd).exec(context, mapParams, new ResultCallback() {
                    @Override
                    public void handleCallback(String response) {
                        callback.handleCallback(response);
                    }
                });
            }else {
                IWebAidlInterface webAidlInterface = RemoteWebBinder.getInstance().getBinder();
                if(webAidlInterface != null){
                    try {
                        webAidlInterface.handleWebCmd(cmd, params, new IWebAidlCallback.Stub() {

                            @Override
                            public void onResult(String response) throws RemoteException {
                                callback.handleCallback(response);
                            }
                        });
                    } catch (RemoteException e) {
                        String response = getResponse("Fail，Please try again");
                        callback.handleCallback(response);
                    }
                } else {
                    String response = getResponse("Fail，Please try again");
                    callback.handleCallback(response);
                }
            }
        }else {//主进程
            if(mainProcessCommands != null && mainProcessCommands.getCommands().containsKey(cmd)){
                mainProcessCommands.getCommands().get(cmd).exec(context, mapParams, new ResultCallback() {
                    @Override
                    public void handleCallback(String response) {
                        callback.handleCallback(response);
                    }
                });
            }else {
                String response = getResponse("Fail, Unsupported features");
                callback.handleCallback(response);
            }
        }


    }

    private String getResponse(String errorMsg){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", errorMsg);
        return jsonObject.toString();
    }





}
