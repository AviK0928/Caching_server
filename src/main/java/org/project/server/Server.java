package org.project.server;

import org.project.store.Store;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final Store store = new Store();

    public Server(int port) {
        this.port = port;
    }

    public void start(){
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Listening at port: "+port);

            // Creation of client-server connection
            Socket client = server.accept();

            // Create client handler that will take care of all commands for the server
            ClientHandler clientHandler = new ClientHandler(client, store);
            clientHandler.run();

            // Close connection with Client and Close server
            client.close();
            server.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
