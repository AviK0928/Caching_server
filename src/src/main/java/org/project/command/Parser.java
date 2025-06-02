package org.project.command;

import org.project.store.Store;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private final String input;
    public Parser(String input) {
        this.input=input;
    }
    public Command parse(){
        String command = input.trim().replaceAll("\\s+"," ");
        String[] tempList1 = command.split("\\s");
        String name = tempList1[0].toUpperCase();
        String[] tempList = new String[tempList1.length-1];
        System.arraycopy(tempList1, 1, tempList, 0, tempList.length);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(tempList));
        return new Command(name,list);
    }
}
