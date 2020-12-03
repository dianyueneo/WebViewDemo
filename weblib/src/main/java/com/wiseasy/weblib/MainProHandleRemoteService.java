package com.wiseasy.weblib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainProHandleRemoteService extends Service {

    public MainProHandleRemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MainProAidlInterface();
    }
}
