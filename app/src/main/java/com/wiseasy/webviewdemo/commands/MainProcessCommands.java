package com.wiseasy.webviewdemo.commands;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.wiseasy.weblib.commands.ResultCallback;
import com.wiseasy.weblib.commands.Command;
import com.wiseasy.weblib.commands.Commands;
import com.wiseasy.webviewdemo.CashPayActivity;

import java.util.Map;

public class MainProcessCommands extends Commands {

    public MainProcessCommands() {

        registCommand(payCommand);
    }

    private final Command payCommand = new Command() {

        private ResultCallback myCallback;
        private Map mapParams;

        @Override
        public String cmdName() {
            return "pay";
        }

        @Override
        public void exec(Context context, final Map params, final ResultCallback callback) {
            Intent intent = new Intent(context, CashPayActivity.class);
            intent.putExtra("amount", (String)params.get("amount"));
            context.startActivity(intent);

            myCallback = callback;
            mapParams = params;

            LocalBroadcastManager.getInstance(context).registerReceiver(new MyBroadcastReceiver(), new IntentFilter(CommandsConstants.Action_Pay));

        }

        @Override
        public String exec(Context context, Map params) {
            return null;
        }

        class MyBroadcastReceiver extends BroadcastReceiver{

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("WebViewManager", "pay 收到响应");
                String result = intent.getStringExtra("result");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("callbackname", (String)mapParams.get("callback"));
                jsonObject.addProperty("result", result);
                myCallback.handleCallback(jsonObject.toString());

            }
        }
    };

}
