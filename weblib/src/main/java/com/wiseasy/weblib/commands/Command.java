package com.wiseasy.weblib.commands;

import android.content.Context;

import java.util.Map;

public interface Command {

    String cmdName();

    void exec(Context context, Map params, ResultCallback resultBack);

}
