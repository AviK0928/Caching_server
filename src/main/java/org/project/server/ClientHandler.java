package org.project.server;

import org.project.command.Command;
import org.project.command.Executor;
import org.project.command.Parser;
import org.project.store.Store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket client;
    private final Store store;

    public ClientHandler(Socket client, Store store) {
        this.client = client;
        this.store = store;
    }

    public void run() {
        try (
                BufferedReader bis = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
        ){
            pw.println("CONNECTED");
            // Take input from client
            String input;
            while ((input=bis.readLine())!=null){
                Command command = new Parser(input).parse();

                String ans = Executor.execute(command,store);

                if(command.getName().equals("CLOSE")){
                    pw.println(ans);
                    break;
                }else{
                    pw.println(ans);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                client.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
