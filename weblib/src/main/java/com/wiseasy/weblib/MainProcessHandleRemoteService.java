package com.wiseasy.weblib;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

public class MainProcessHandleRemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        CommandDispatcher.getInstance().init(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MainProcessAidlInterface();
    }


    class MainProcessAidlInterface extends IWebAidlInterface.Stub {
        @Override
        public void handleWebAction(final String actionName, final String jsonParams, final IWebAidlCallback callback) throws RemoteException {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dealAction(actionName, jsonParams, callback);
                }
            });
        }


    }

    private void dealAction(String actionName, String jsonParams, final IWebAidlCallback callback) {
        CommandDispatcher.getInstance().exec(MainProcessHandleRemoteService.this, actionName, jsonParams, new JsResponseCallback() {
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
