package com.ratelimiter;

import org.apache.commons.codec.digest.MurmurHash3;

public class BloomFilter {

    byte[] filter;
    private int size;

    public BloomFilter(int size){
        filter=new byte[size];
        this.size=size;
    }

    public void add(String key){
       int index=murmurHashFunction(key,size);
       filter[index]=1;
        System.out.println(key+" :"+index);
    }

    public boolean exists(String key){
        int index=murmurHashFunction(key,size);
        return filter[index]==1?true:false;
    }

    private int murmurHashFunction(String key,int size){
       return Math.abs(MurmurHash3.hash32x86(key.getBytes()))%size;
    }
    public static void main(String args[]){
        BloomFilter obj=new BloomFilter(25);
        String inputString[]={"a","e","g","h","y","r","t","s","b","c","d","z","x","o","j","l"};
        for(int i=0;i<inputString.length;i++){
            obj.add(inputString[i]);
        }

        for(int i=0;i<inputString.length;i++){
            System.out.println(inputString[i]+" :"+obj.exists(inputString[i]));
        }
        System.out.println("v"+" :"+obj.exists("v"));
    }
}
