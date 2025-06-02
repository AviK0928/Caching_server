package org.project.main;

import org.project.server.Server;

import java.util.Scanner;

public class Main {
    private static Server server;
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("DO YOU WANT TO START THE SERVER: (Y/N)");
        String ans = scan.nextLine();
        while (!ans.equalsIgnoreCase("n") && !ans.equalsIgnoreCase("y")){
            System.out.println("WRONG INPUT");
            ans = scan.nextLine();
        }
        if(ans.equalsIgnoreCase("n")){
            scan.close();
        }else if(ans.equalsIgnoreCase("y")){

            // Getting Server details
            System.out.print("ENTER PORT NO: ");
            int port = scan.nextInt();
            scan.nextLine();

            // Starting Server
            System.out.println("SERVER STARTED ON PORT: "+port);
            server = new Server(port);
            Thread serverThread = new Thread(server,"Server-Thread");
            serverThread.start();
            System.out.println("SERVER-THREAD STATE: "+serverThread.getState());

            // Waiting for Shutdown command from server admin
            while(true){
                String line = scan.nextLine();
                if(line.equalsIgnoreCase("shutdown")){
                    System.out.println("SHUTTING DOWN SERVER");
                    server.shutdown();
                    break;
                }
            }
            // Waiting for server thread to join
            try {
                System.out.println("WAITING FOR SERVER-THREAD TO JOIN");
                System.out.println("SERVER-THREAD STATE: " + serverThread.getState());
                serverThread.join();
            } catch (InterruptedException e) {
                System.out.println("INTERRUPTION IN JOINING: " + e.getMessage());
            }
            // Closing resources of main class
            scan.close();
        }
    }
}