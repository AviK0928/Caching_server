package org.project.server;

import org.project.command.Command;
import org.project.command.Executor;
import org.project.command.Parser;
import org.project.store.Store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    private final Socket client;
    private final Store store;

    public ClientHandler(Socket client, Store store) {
        this.client = client;
        this.store = store;
    }

    public void run() {
        try {
            // Create Reader and Writer for a client
            BufferedReader bis = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream(),true);

            pw.println("CONNECTED");
            // Take input from client
            String input;
            while ((input=bis.readLine())!=null){
                // Convert the input into a list where list[0] is the command and rest are key, value depending
                // upon the command
                Command command = new Parser(input).parse();    // created a parser which converts input into command

                // Send the command to an executor which will return a string depending upon the operation performed
                String ans = Executor.execute(command,store);   // The executor will take in a command and the common
                // store as arguments and will process the command
                if(command.getName().equals("CLOSE")){
                    pw.println(ans);
                    break;
                }else{
                    pw.println(ans); // return
                }
            }
            bis.close();
            pw.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
