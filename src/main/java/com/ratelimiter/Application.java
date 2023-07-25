package com.ratelimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void main(String args[]){
//        UserBucketCreatorSlidingWindow bucketCreator=new UserBucketCreatorSlidingWindow(1);
//        ExecutorService service= Executors.newFixedThreadPool(10);
//        for(int i=1;i<=13;i++){
//            service.execute(()->bucketCreator.accessApplication(1));
//        }
//        service.shutdown();
//        ExecutorService service= Executors.newFixedThreadPool(10);
//        UserBucketCreatorLeakyBucket leakyBucket=new UserBucketCreatorLeakyBucket(1);
//        for(int i=0;i<15;i++){
//            service.execute(()->leakyBucket.accessApplication(1));
//        }
//        service.shutdown();

        ExecutorService service= Executors.newFixedThreadPool(12);
        UserBucketCreatorTokenBucket leakyBucket=new UserBucketCreatorTokenBucket(1);
        for(int i=0;i<15;i++){
            service.execute(()->leakyBucket.accessApplication(1));
        }
        service.shutdown();
    }
}
