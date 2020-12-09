package com.wiseasy.weblib.commands;

import android.content.Context;

import java.util.Map;

public interface Command {

    String cmdName();

    /**
     * 异步
     */
    void exec(Context context, Map params, ResultCallback resultBack);

    /**
     * 同步
     */
    String exec(Context context, Map params);

}
