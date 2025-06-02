package org.project.server;

import org.project.persistence.RDB;
import org.project.store.Store;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable{
    private final int port;
    private final Store store = new Store();
    private final RDB rdb = new RDB();
    private ScheduledExecutorService scheduler;
    private static final int AUTO_SAVE_INTERVAL_SECONDS = 30;
    private volatile boolean isRunning=true;
    private ServerSocket server;

    public Server(int port) {
        this.port = port;
    }

    public void run(){
        try{
            server = new ServerSocket(port);
            System.out.println("LISTENING ON PORT: "+port);
            autoLoad();
            autoSave(rdb,store);
            while(isRunning){
                try {
                    Socket client = server.accept();
                    startClients(client);
                }catch (SocketException se){
                    if (!isRunning) {
                        break;
                    }
                    System.out.println(se.getMessage());
                    se.printStackTrace();
                }catch (IOException ioe ){
                    System.out.println(ioe.getMessage());
                    ioe.printStackTrace();
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void startClients(Socket client) throws IOException {
        System.out.println("CLIENT CONNECTED: "+client.getInetAddress());
        ClientHandler clientHandler = new ClientHandler(client, store);
        new Thread(clientHandler).start();
    }

    private void autoLoad(){
        Map<?,?>[] maps = rdb.load();
        store.saveMaps(maps);
    }

    private void autoSave(RDB rdb, Store store){
        scheduler= Executors.newSingleThreadScheduledExecutor();
        Runnable autoSave = ()->{
            try {
                ConcurrentHashMap<String,String> datamap = store.getDatamap();
                ConcurrentHashMap<String,Long> ttlmap = store.getTtlmap();
                rdb.save(datamap,ttlmap);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        };
        scheduler.scheduleAtFixedRate(autoSave, AUTO_SAVE_INTERVAL_SECONDS, AUTO_SAVE_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public void shutdown(){
        isRunning=false;
        try {
            if(server!=null && !server.isClosed()){
                server.close();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        if(scheduler!=null){
            scheduler.shutdown();
            try {
                if(!scheduler.awaitTermination(5,TimeUnit.SECONDS)){
                    scheduler.shutdownNow();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        rdb.save(store.getDatamap(),store.getTtlmap());
    }
}