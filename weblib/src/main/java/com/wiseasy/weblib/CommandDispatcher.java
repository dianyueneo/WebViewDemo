package com.wiseasy.weblib;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wiseasy.weblib.commands.Commands;
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


    public void init(Context context){
        //WebView进程
        if(!SystemInfoUtil.inMainProcess(context)){
            RemoteWebBinder.getInstance().connectBinderService();
        }
    }

    public void setMainProcessCommands(Commands commands){
        this.mainProcessCommands = commands;
    }

    public void setWebViewProcessCommands(Commands commands){
        this.wevViewProcessCommands = commands;
    }

    public void exec(Context context, String action, String params, final JsResponseCallback callback){
        Log.i("WebViewManager", "action: "+ action + " params: "+ params);
        Map mapParams = gson.fromJson(params, Map.class);

        //WebView进程
        if(!SystemInfoUtil.inMainProcess(context)){
            if(wevViewProcessCommands != null && wevViewProcessCommands.getCommands().containsKey(action)){
                wevViewProcessCommands.getCommands().get(action).exec(context, mapParams, new ResultCallback() {
                    @Override
                    public void handleCallback(String response) {
                        callback.handleCallback(response);
                    }
                });
            }else {
                IWebAidlInterface webAidlInterface = RemoteWebBinder.getInstance().getBinder();
                if(webAidlInterface != null){
                    try {
                        webAidlInterface.handleWebAction(action, params, new IWebAidlCallback.Stub() {

                            @Override
                            public void onResult(String response) throws RemoteException {
                                //todo 不支持的功能
                                callback.handleCallback(response);
                            }
                        });
                    } catch (RemoteException e) {
                        String response = getResponse(mapParams, "Fail，Please try again");
                        callback.handleCallback(response);
                    }
                } else {
                    String response = getResponse(mapParams, "Fail，Please try again");
                    callback.handleCallback(response);
                }
            }
        }else {//主进程
            if(mainProcessCommands != null && mainProcessCommands.getCommands().containsKey(action)){
                mainProcessCommands.getCommands().get(action).exec(context, mapParams, new ResultCallback() {
                    @Override
                    public void handleCallback(String response) {
                        callback.handleCallback(response);
                    }
                });
            }else {
                String response = getResponse(mapParams, "Fail, Unsupported features");
                callback.handleCallback(response);
            }
        }


    }

    private String getResponse(Map mapParams, String errorMsg){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("callbackname", (String)mapParams.get("callback"));
        jsonObject.addProperty("result", "Fail, Unsupported features");
        return jsonObject.toString();
    }





}
