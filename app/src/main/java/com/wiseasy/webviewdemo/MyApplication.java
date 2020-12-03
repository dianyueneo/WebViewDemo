package com.wiseasy.webviewdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.wiseasy.weblib.BaseApplication;


public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if(getPackageName().equals(getProcName())){
            preInitX5Core();
        }

    }

    private void preInitX5Core() {
        Intent intent = new Intent(this, X5InitService.class);
        startService(intent);
    }

    public String getProcName() {
        String procName = null;
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                procName = appProcess.processName;
            }
        }
        return procName;
    }
}
