package org.project.store;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class Store {
    private final HashMap<String, String> data = new HashMap<>();
    private final HashMap<String, Long> ttlmap = new HashMap<>();

    public synchronized String set(ArrayList<String> list){
        if(list.size()<2){
            return "ERROR: Need a Key and a Value";
        }else{
            data.put(list.get(0),list.get(1));
            return "OK";
        }
    }

    public synchronized String get(ArrayList<String> list) {
        String key = list.getFirst();
        if(isExpired(key)){
            return "NIL";
        }else{
            return data.getOrDefault(list.getFirst(), "NIL");
        }

    }

    public synchronized String del(ArrayList<String> list) {
        String key = list.getFirst();
        if(data.containsKey(key)){
            data.remove(key);
            ttlmap.remove(key);
            return "OK";
        }else{
            return "NIL";
        }
    }

    public synchronized String exp(ArrayList<String> list){
        String key = list.get(0);
        if(!data.containsKey(key)){
            return "NIL";
        }else{
            try {
                long seconds = Long.parseLong(list.get(1));
                ttlmap.put(key, System.currentTimeMillis()+(seconds*1000));
                return "OK";
            }catch (NumberFormatException e){
                return "ERROR: Invalid TTL";
            }
        }
    }

    public synchronized String ttl(ArrayList<String> list){
        String key = list.getFirst();
        if(!data.containsKey(key)){
            return "ERROR: Key not available";
        }
        if(!ttlmap.containsKey(key)){
            return "NIL";
        }
        long remaining = ttlmap.get(key)-System.currentTimeMillis();
        if(remaining<=0){
            data.remove(key);
            ttlmap.remove(key);
            return "EXPIRED";
        }else{
            return String.valueOf(remaining/1000);
        }
    }

    private boolean isExpired(String key){
        if(!ttlmap.containsKey(key)){
            return false;
        }
        if(System.currentTimeMillis()>ttlmap.get(key)){
            data.remove(key);
            ttlmap.remove(key);
            return true;
        }
        return false;
    }
}
