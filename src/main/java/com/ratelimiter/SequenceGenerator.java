package com.ratelimiter;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

public class SequenceGenerator {

    private static final int UNUSED_BIT=1;
    private static final int EPOCH_BITS=41;
    private static final int NODE_INT_BITS=10;
    private static final int SEQUENCE_BITS=12;

    private static final int maxNodeId=(int)(Math.pow(2,NODE_INT_BITS)-1);
    private static final int maxSequence=(int)(Math.pow(2,SEQUENCE_BITS)-1);

    private static final long CUSTOM_EPOCH=1420070400000L;

    private final int nodeId;
    private volatile long lastTimeStamp=-1L;
    private volatile long sequence=0L;

    public SequenceGenerator(int nodeId) {
        if(nodeId< 0 || nodeId>maxNodeId){
            throw new IllegalArgumentException(String.format("NodeId must be between %d and %d",0,maxNodeId));

        }
        this.nodeId = nodeId;
    }

    public SequenceGenerator() {
        this.nodeId = createNodeId();
    }

    private int createNodeId() {
        int nodeId;
        try{
            StringBuilder sb=new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaceEnumeration=NetworkInterface.getNetworkInterfaces();
            while(networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface=networkInterfaceEnumeration.nextElement();
                byte[] mac=networkInterface.getHardwareAddress();
                if(mac!=null){
                    for(int i=0;i<mac.length;i++){
                        sb.append(String.format("%02X",mac[i]));
                    }
                }
            }
            nodeId=sb.toString().hashCode();

        }catch (Exception ex){
           nodeId=new SecureRandom().nextInt();
        }
        nodeId=nodeId & maxNodeId;
       return nodeId;
    }

    public synchronized long nextId(){
        long currentTimeStamp= Instant.now().toEpochMilli()-CUSTOM_EPOCH;
        if(currentTimeStamp<lastTimeStamp){
            throw new IllegalArgumentException("Invalid Clock Time");
        }

        if(currentTimeStamp==lastTimeStamp){
            sequence=(sequence+1) & maxSequence;
            if(sequence==0){
                currentTimeStamp=waitTillNextTimestamp(currentTimeStamp);
            }
        }else{
            sequence=0;
        }
        lastTimeStamp=currentTimeStamp;

        long id=currentTimeStamp<<(NODE_INT_BITS+SEQUENCE_BITS);
        id|=nodeId<<SEQUENCE_BITS;
        id|=sequence;
        return id;
    }

    private long waitTillNextTimestamp(long currentTimeStamp) {
        while(currentTimeStamp==lastTimeStamp){
            currentTimeStamp=Instant.now().toEpochMilli()-CUSTOM_EPOCH;
        }

        return currentTimeStamp;
    }

    public static void main(String args[]){
        SequenceGenerator sequenceGenerator=new SequenceGenerator();
        System.out.println(sequenceGenerator.nextId());
        System.out.println(sequenceGenerator.nextId());
    }
}
