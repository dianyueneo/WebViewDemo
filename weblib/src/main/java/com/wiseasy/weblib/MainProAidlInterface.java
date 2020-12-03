package com.wiseasy.weblib;

import android.os.RemoteException;

import com.wiseasy.weblib.IWebAidlCallback;
import com.wiseasy.weblib.IWebAidlInterface;

public class MainProAidlInterface extends IWebAidlInterface.Stub {
    @Override
    public void handleWebAction(String actionName, String jsonParams, IWebAidlCallback callback) throws RemoteException {

    }
}
