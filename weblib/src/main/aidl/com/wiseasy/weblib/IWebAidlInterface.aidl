package com.wiseasy.weblib;

import com.wiseasy.weblib.IWebAidlCallback;

interface IWebAidlInterface {
      void handleWebCmd(String cmd, String jsonParams, in IWebAidlCallback callback);
}
