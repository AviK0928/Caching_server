package org.project.command;

import java.util.ArrayList;

public class Command {
    private final String name;
    private final ArrayList<String> list;

    public Command(String name, ArrayList<String> list) {
        this.name = name;
        this.list = list;
    }
    public String getName(){
        return name;
    }
    public ArrayList<String> getList(){
        return list;
    }
}
