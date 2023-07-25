package com.ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class UserBucketCreatorLeakyBucket {


    Map<Integer,LeakyBucket> ratelimiter;

    public UserBucketCreatorLeakyBucket(int id) {
        LeakyBucket bucket=new LeakyBucket(10);
        ratelimiter=new HashMap<>();
        ratelimiter.put(id,bucket);
    }

    void accessApplication(int id){
        if(ratelimiter.get(id).grantAccess()){
            System.out.println(Thread.currentThread().getName()+"---->able to acess");
        }else{
            System.out.println(Thread.currentThread().getName()+"---->429");
        }
    }
}
