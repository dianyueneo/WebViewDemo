package com.wiseasy.weblib.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class SystemInfoUtil {


    /**
     * 判断当前是否是主进程，context 是 ApplicationContext
     */
    public static boolean inMainProcess(Context context) {
        String mainProcessName = context.getPackageName();
        String processName = getProcessName(context, android.os.Process.myPid());
        return mainProcessName.equals(processName);
    }

    /**
     * 判断当前是否是WebView进程，context 是 ApplicationContext
     */
    public static boolean isWebViewProcess(Context context){
        String webViewProcessName = context.getPackageName()+ ":applet";
        String processName = getProcessName(context, android.os.Process.myPid());
        return webViewProcessName.equals(processName);
    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    public static String getProcessName(Context context, int pid) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
