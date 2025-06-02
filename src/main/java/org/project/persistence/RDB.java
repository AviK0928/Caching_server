package org.project.persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class RDB {

    private final String filePath;
    private final File file;
    public RDB(){
        String userHome = System.getProperty("user.home");
        Path snapshotDir = Paths.get(userHome, "mycacheserver", "data");
        this.filePath = String.valueOf(snapshotDir.resolve(snapshotDir.resolve("server.dat")));
        this.file=new File(filePath);
        try {
            Files.createDirectories(snapshotDir);  // Creates all nonexistent parent dirs
        } catch (IOException e) {
            System.err.println("Error creating directory for snapshot: " + snapshotDir);
            e.printStackTrace();
        }
    }


    public synchronized boolean save(Map<String,String>datamap, Map<String,Long>ttlmap){
        if(datamap==null||ttlmap==null){
            System.out.println("MAPS ARE NULL");
            return false;
        }else{
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(Snapper.toSnapshot(datamap,ttlmap));
                System.out.println("SNAPSHOT SAVED AT " + file.getCanonicalPath());
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("FAILED TO SAVE SNAPSHOT");
                return false;
            }
        }
    }
    public synchronized Map<?, ?>[] load(){
        if(!file.exists()){
            System.out.println("FILE DOESN'T EXIST");
            return Snapper.fromSnapshot(null);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            Object object = in.readObject();
            if (object instanceof Snapshot snapshot){
                return Snapper.fromSnapshot(snapshot);
            }else{
                System.out.println("FILE IS NOT A VALID SNAPSHOT");
            }
        }catch (Exception e){
            System.out.println("FAILED TO LOAD SNAPSHOT");
            System.out.println(e.getMessage());
        }
        return Snapper.fromSnapshot(null);
    }

}
