package com.ratelimiter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindow implements RateLimiter{

    int bucketSize;
    long timeSecondswindow;
    Queue<Long> slidingWindow;

    public SlidingWindow(int bucketSize, long initialCapacity) {
        this.bucketSize = bucketSize;
        this.timeSecondswindow = initialCapacity;
        this.slidingWindow = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean grantAccess() {
         //if(slidingWindow.isEmpty()) return true;
         long currentTime=System.currentTimeMillis();
         checkUpdateQueue(currentTime);
         if(slidingWindow.size()<bucketSize) {
             slidingWindow.offer(currentTime);
             return true;
         }

         return false;
    }

    private void checkUpdateQueue(long currentTime) {
        if(slidingWindow.isEmpty()) return ;
        long diff=(currentTime-slidingWindow.peek())/1000;
        while(diff>=timeSecondswindow){
            slidingWindow.poll();
            if(slidingWindow.isEmpty()) break;
            diff=(currentTime-slidingWindow.peek())/1000;
        }

    }
}
