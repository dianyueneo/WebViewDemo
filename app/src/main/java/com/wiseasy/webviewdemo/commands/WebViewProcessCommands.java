package com.wiseasy.webviewdemo.commands;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.wiseasy.weblib.commands.ResultCallback;
import com.wiseasy.weblib.commands.Command;
import com.wiseasy.weblib.commands.Commands;
import com.wiseasy.weblib.utils.ParamsUtil;

import java.util.List;
import java.util.Map;

public class WebViewProcessCommands extends Commands {

    public WebViewProcessCommands() {
        registCommand(showToastCommand);
        registCommand(showDialogCommand);
        registCommand(getSysInfoCommand);
    }

    private final Command getSysInfoCommand = new Command() {
        @Override
        public String cmdName() {
            return "getSysInfo";
        }

        @Override
        public void exec(Context context, Map params, ResultCallback resultBack) {

        }

        @Override
        public String exec(Context context, Map params) {
            return "android 8.1";
        }
    };

    private final Command showToastCommand = new Command() {
        @Override
        public String cmdName() {
            return "showToast";
        }

        @Override
        public void exec(Context context, Map params, ResultCallback resultBack) {
            Toast.makeText(context, String.valueOf(params.get("message")), Toast.LENGTH_SHORT).show();
        }

        @Override
        public String exec(Context context, Map params) {
            return null;
        }
    };

    private final Command showDialogCommand = new Command() {
        @Override
        public String cmdName() {
            return "showDialog";
        }

        @Override
        public void exec(Context context, Map params, final ResultCallback resultBack) {
            if (ParamsUtil.isNotNull(params)) {
                String title = (String) params.get("title");
                String content = (String) params.get("content");
                int canceledOutside = 0;
                if (params.get("canceledOutside") != null) {
                    canceledOutside = (int) (double) params.get("canceledOutside");
                }
                List<Map<String, String>> buttons = (List<Map<String, String>>) params.get("buttons");
                final String callbackName = (String) params.get("callback");
                if (!TextUtils.isEmpty(content)) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(content)
                            .create();
                    dialog.setCanceledOnTouchOutside(canceledOutside == 1 ? true : false);
                    if (ParamsUtil.isNotNull(buttons)) {
                        for (int i = 0; i < buttons.size(); i++) {
                            final Map<String, String> button = buttons.get(i);
                            int buttonWhich = getDialogButtonWhich(i);

                            if (buttonWhich == 0) {return;}

                            dialog.setButton(buttonWhich, button.get("title"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    button.put("callbackname", callbackName);
                                    resultBack.handleCallback(new Gson().toJson(button));
                                }
                            });
                        }
                    }
                    dialog.show();
                }
            }
        }

        @Override
        public String exec(Context context, Map params) {
            return null;
        }

        private int getDialogButtonWhich(int index) {
            switch (index) {
                case 0:
                    return DialogInterface.BUTTON_POSITIVE;
                case 1:
                    return DialogInterface.BUTTON_NEGATIVE;
                case 2:
                    return DialogInterface.BUTTON_NEUTRAL;
            }
            return 0;
        }
    };
}
