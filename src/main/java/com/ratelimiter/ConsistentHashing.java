package com.ratelimiter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing {

    private final int numberOfReplicas;
    private final TreeMap<Long,String> ring;
    private final MessageDigest md;

    public ConsistentHashing(int numberOfReplicas) throws NoSuchAlgorithmException {
        this.numberOfReplicas = numberOfReplicas;
        this.ring = new TreeMap<>();
        this.md = MessageDigest.getInstance("MD5");
    }

    public void addServer(String server){
        for(int i=0;i<numberOfReplicas;i++){
            ring.put(generateHashKey(server+i),server);
        }

    }
    public String getServer(String key){
        long hash=generateHashKey(key);
        if(ring.isEmpty()) return null;
        if(!ring.containsKey(hash)){
            SortedMap<Long,String> tail=ring.tailMap(hash);
            hash=tail.isEmpty()?ring.firstKey():tail.firstKey();
        }
        return ring.get(hash);
    }
    public void removeServer(String server){
        for(int i=0;i<numberOfReplicas;i++){
            ring.remove(generateHashKey(server+i));
        }
    }

    private Long generateHashKey(String key){
        md.reset();
        md.update(key.getBytes());
        byte[] digest=md.digest();
        long hash=((long) (digest[3] & 0xFF << 24) | (long) (digest[2] & 0xFF << 16) |(long) (digest[1] & 0xFF << 8)|(long) (digest[0] & 0xFF ));

        return hash;
    }

    public static void main(String args[]) throws NoSuchAlgorithmException {
        ConsistentHashing ch = new ConsistentHashing(3);
        ch.addServer("server1");
        ch.addServer("server2");
        ch.addServer("server3");


        System.out.println("key1: is present on server: " + ch.getServer("key1"));
        System.out.println("key67890: is present on server: " + ch.getServer("key67890"));

        ch.removeServer("server3");
        System.out.println("After removing server1");

        System.out.println("key1: is present on server: " + ch.getServer("key1"));
        System.out.println("key67890: is present on server: " + ch.getServer("key67890"));

    }
}
