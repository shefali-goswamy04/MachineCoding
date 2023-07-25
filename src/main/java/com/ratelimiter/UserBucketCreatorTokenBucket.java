package com.ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class UserBucketCreatorTokenBucket {


    Map<Integer,TokenBucket> ratelimiter;

    public UserBucketCreatorTokenBucket(int id) {
        TokenBucket window=new TokenBucket(10,10);
        ratelimiter=new HashMap<>();
        ratelimiter.put(id,window);
    }

    void accessApplication(int id){
        if(ratelimiter.get(id).grantAccess()){
            System.out.println(Thread.currentThread().getName()+"---->able to acess");
        }else{
            System.out.println(Thread.currentThread().getName()+"---->Unable to access");
        }
    }
}
