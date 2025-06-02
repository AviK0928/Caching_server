package org.project.persistence;

import java.io.Serializable;
import java.util.Map;

public class Snapshot implements Serializable {
    private static final long versionUID = 1L;
    private final Map<String,String> datamap;
    private final Map<String,Long> ttlmap;

    public Snapshot(Map<String,String> datamap, Map<String,Long> ttlmap){
        this.datamap = datamap;
        this.ttlmap = ttlmap;
    }
    public Map<String,String> getDatamap(){
        return datamap;
    }
    public Map<String,Long> getTtlmap(){
        return ttlmap;
    }

}
