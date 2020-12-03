// IWebAidlInterface.aidl
package com.wiseasy.weblib;

import com.wiseasy.weblib.IWebAidlCallback;

interface IWebAidlInterface {
     /**
      * actionName: 不同的action， jsonParams: 需要根据不同的action从map中读取并依次转成其他
      */
      void handleWebAction(String actionName, String jsonParams, in IWebAidlCallback callback);
}
