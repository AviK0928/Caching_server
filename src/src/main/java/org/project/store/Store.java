package org.project.store;

import org.project.persistence.RDB;
import org.project.persistence.Snapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Store {
    private final ConcurrentHashMap<String, String> datamap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> ttlmap = new ConcurrentHashMap<>();
    private final RDB rdb = new RDB();
    private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentHashMap<String, String> getDatamap() {
        return datamap;
    }
    public ConcurrentHashMap<String, Long> getTtlmap() {
        return ttlmap;
    }

    public String set(ArrayList<String> list){
        if(list.size()<2){
            return "ERROR: Need a Key and a Value";
        }else{
            datamap.put(list.get(0),list.get(1));
            return "OK";
        }
    }

    public String get(ArrayList<String> list) {
        String key = list.getFirst();
        if(isExpired(key)){
            return "NIL";
        }else{
            return datamap.getOrDefault(list.getFirst(), "NIL");
        }

    }

    public String del(ArrayList<String> list) {
        String key = list.getFirst();
        if(datamap.containsKey(key)){
            datamap.remove(key);
            ttlmap.remove(key);
            return "OK";
        }else{
            return "NIL";
        }
    }

    public String exp(ArrayList<String> list){
        String key = list.get(0);
        try {
            long seconds=Long.parseLong(list.get(1));
            long time = System.currentTimeMillis()+(seconds*1000);
            boolean updated = datamap.computeIfPresent(key, (k,v) ->{
                ttlmap.put(key,time);
                return v;
            })!=null;
            return updated? "OK":"NIL";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "NIL";
        }
    }

    public String ttl(ArrayList<String> list){
        String key = list.getFirst();
        if(!datamap.containsKey(key)){
            return "ERROR: Key not available";
        }
        if(!ttlmap.containsKey(key)){
            return "NIL";
        }
        long remaining = ttlmap.get(key)-System.currentTimeMillis();
        if(remaining<=0){
            lock.lock();
            try {
                datamap.remove(key);
                ttlmap.remove(key);
            }finally {
                lock.unlock();
            }
            return "EXPIRED";
        }else{
            return String.valueOf(remaining/1000);
        }
    }

    public String clearDB() {
        lock.lock();
        try{
            datamap.clear();
            ttlmap.clear();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            lock.unlock();
        }
        return "OK";
    }

    private boolean isExpired(String key){
        if(!ttlmap.containsKey(key)){
            return false;
        }
        if(System.currentTimeMillis()>ttlmap.get(key)){
            lock.lock();
            try {
                datamap.remove(key);
                ttlmap.remove(key);
            }finally{
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public synchronized String save() {
        if(rdb.save(datamap,ttlmap)){
            return "OK";
        }else{
            return "ERROR: Failed to save";
        }
    }

    public synchronized String close() {
        if(rdb.save(datamap,ttlmap)){
            return "CLOSING CONNECTION, LATEST DATA SAVED";
        }else{
            return "CLOSING CONNECTION, LATEST DATA NOT SAVED";
        }
    }

    public synchronized void saveMaps(Map<?,?>[] arr){
        HashMap<String,String> map1 = (HashMap<String, String>) arr[0];
        HashMap<String,Long> map2 = (HashMap<String, Long>) arr[1];
        datamap.putAll(map1);
        ttlmap.putAll(map2);
    }
}