package com.ratelimiter;

import java.util.HashMap;

public class UrlShortner {

    HashMap<Integer,String> urlToCounter;
    HashMap<String,Integer> counterToUrl;
    static int COUNTER=1000000000;
    String elements;

    public UrlShortner() {
        this.urlToCounter = new HashMap<>();
        this.counterToUrl = new HashMap<>();
        elements="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }

    public String longToShortUrl(String longUrl){
        String shortUrl=baseNto62(COUNTER);
        urlToCounter.put(COUNTER,longUrl);
        counterToUrl.put(longUrl,COUNTER);
        ++COUNTER;
        return "http://tiny.url/"+shortUrl;
    }

    public String shortToLongUrl(String shortUrl){
        int counter=base62toN(shortUrl.substring("http://tiny.url/".length()));
        return urlToCounter.get(counter);
    }

    private String baseNto62(int counter){
        StringBuilder sb=new StringBuilder();
        while(counter!=0){
            int r=counter%62;
            sb.insert(0,elements.charAt(r));
            counter/=62;
        }
        if(sb.length()!=7){
            sb.insert(0,'0');
        }
        return sb.toString();


    }

    private int base62toN(String convert){
        int counter=0;
        for(int i=0;i<convert.length();i++){
            counter=convertChar(convert.charAt(i))+counter*62;
        }

        return counter;
    }

    private int convertChar(char c){
        if(c>='0' && c<='9'){
            return c -'0';
        } if(c>='a' && c<='z'){
            return c -'a'+10;
        } if(c>='A' && c<='Z'){
            return c -'A'+36;
        }

        return -1;

    }

    public static void main(String args[]){
        String url="http://tiny.url/geeksforgeeksAndMachineCoding";
        UrlShortner obj=new UrlShortner();
        String shorrtUrl=obj.longToShortUrl(url);
        System.out.println("ShortUrl: "+shorrtUrl);
        System.out.println("LongUrl: "+obj.shortToLongUrl(shorrtUrl));
    }

}
