package com.wiseasy.weblib.commands;

import android.content.Context;

import java.util.Map;

public abstract class Command {

    public abstract String cmdName();

    /**
     * 异步
     */
    public void exec(Context context, Map params, ResultCallback resultBack){

    }

    /**
     * 同步
     */
    public String exec(Context context, Map params){
        return null;
    }

}
