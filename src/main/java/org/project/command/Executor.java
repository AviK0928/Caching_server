package org.project.command;

import org.project.store.Store;

public class Executor {
    public static String execute(Command command, Store store){
        return switch (command.getName()) {
            case "SET" -> store.set(command.getList());
            case "GET" -> store.get(command.getList());
            case "DEL" -> store.del(command.getList());
            case "EXP" -> store.exp(command.getList());
            case "TTL" -> store.ttl(command.getList());
            case "SAVE" -> store.save();
            case "CLOSE" -> store.close();
            case "CLEAR" -> store.clearDB();
            default -> "ERROR: Wrong Command";
        };
    }
}
