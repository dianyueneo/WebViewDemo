package com.wiseasy.weblib.commands;

import android.content.Context;

import com.wiseasy.weblib.ResultCallback;

import java.util.Map;

public interface Command {

    String action();

    void exec(Context context, Map params, ResultCallback resultBack);

}
