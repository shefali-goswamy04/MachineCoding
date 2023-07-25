package com.ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class UserBucketCreatorSlidingWindow {

    Map<Integer,SlidingWindow> ratelimiter;

    public UserBucketCreatorSlidingWindow(int id) {
        SlidingWindow window=new SlidingWindow(10,1);
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
