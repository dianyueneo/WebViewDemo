package com.wiseasy.weblib.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.wiseasy.weblib.commands.CommandDispatcher;
import com.wiseasy.weblib.IWebAidlCallback;
import com.wiseasy.weblib.IWebAidlInterface;
import com.wiseasy.weblib.webview.JsResponseCallback;

public class MainProcessHandleRemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        CommandDispatcher.getInstance().init(this.getApplication());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MainProcessAidlInterface();
    }


    class MainProcessAidlInterface extends IWebAidlInterface.Stub {
        @Override
        public void handleWebCmd(final String cmd, final String jsonParams, final IWebAidlCallback callback) throws RemoteException {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dealCmd(cmd, jsonParams, callback);
                }
            });
        }


    }

    private void dealCmd(String cmd, String jsonParams, final IWebAidlCallback callback) {
        CommandDispatcher.getInstance().dispatchJSRequest(MainProcessHandleRemoteService.this, cmd, jsonParams, new JsResponseCallback() {
            @Override
            public void handleCallback(String response) {
                try {
                    if (callback != null) {
                        callback.onResult(response);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
