package com.wiseasy.weblib.commands;

import java.util.HashMap;

public class Commands {

    private HashMap<String, Command> commands = new HashMap<>();

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    protected void registCommand(Command command){
        commands.put(command.cmdName(), command);
    }
}
