package com.minsudongP.Singletone;

import okhttp3.OkHttpClient;

public class UrlConnection {
    private OkHttpClient client;
    public static UrlConnection shardUrl=new UrlConnection();


    private String url;

    private UrlConnection(){
        this.client=new OkHttpClient();
    }

    


}
