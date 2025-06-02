package org.project.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Snapper {

    public static Snapshot toSnapshot(Map<String,String>datamap, Map<String,Long>ttlmap){
        return new Snapshot(
                datamap==null? Collections.emptyMap():new HashMap<>(datamap),
                ttlmap==null? Collections.emptyMap():new HashMap<>(ttlmap)
        );
    }
    public static Map<?,?>[] fromSnapshot(Snapshot snap){
        Map<?,?>[] arr = new Map[2];
        if(snap==null){
            arr[0]=new HashMap<>();
            arr[1]=new HashMap<>();
            return arr;
        }else{
            Map<String,String> datamap = snap.getDatamap();
            Map<String,Long> ttlmap = snap.getTtlmap();
            arr[0]=datamap==null?new HashMap<>():datamap;
            arr[1]=ttlmap==null?new HashMap<>():ttlmap;
            return arr;
        }
    }
}
