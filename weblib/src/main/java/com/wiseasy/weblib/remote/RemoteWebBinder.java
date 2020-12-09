package com.wiseasy.weblib.remote;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.wiseasy.weblib.IWebAidlInterface;

public class RemoteWebBinder {

    private RemoteWebBinder(){
    }

    private static class Holder{
        private static final RemoteWebBinder instance = new RemoteWebBinder();
    }

    public static RemoteWebBinder getInstance(){
        return Holder.instance;
    }



    private IWebAidlInterface webAidlInterface;
    private Application application;


    public void connectBinderService(Application application){
        this.application = application;
        Intent intent = new Intent(application, MainProcessHandleRemoteService.class);
        application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            webAidlInterface = IWebAidlInterface.Stub.asInterface(service);
            try {
                webAidlInterface.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            webAidlInterface.asBinder().unlinkToDeath(deathRecipient, 0);
            webAidlInterface = null;
            connectBinderService(application);
        }
    };

    public IWebAidlInterface getBinder(){
        return webAidlInterface;
    }

}
