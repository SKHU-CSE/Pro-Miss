package com.minsudongP.Singletone;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UrlConnection {
    private OkHttpClient client;
    public static UrlConnection shardUrl=new UrlConnection();


    private String Mainurl="http://106.10.58.28/";
    private String SpeekUrl="https://naveropenapi.apigw.ntruss.com/voice/v1/tts";

    private UrlConnection(){
        this.client=new OkHttpClient();
    }

    public void GetRequest(String PHP,  Callback callback, HashMap<String,String> map)
    {
        String url=Mainurl;

        Request request=new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void PostRequest(String PHP,  Callback callback, HashMap<String,String> map)
    {

    }

    public void DeleteRequest(String PHP,  Callback callback, HashMap<String,String> map)
    {

    }

    public void PutRequest(String PHP,  Callback callback, HashMap<String,String> map)
    {

    }

    public void PostSpeekRequest(String Text,Callback callback)
    {




            FormBody body = new FormBody.Builder()
                    .add("speaker", "mijin")
                    .add("speed", "0")
                    .add("text", Text)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .addHeader("charset","UTF-8")
                    .addHeader("X-NCP-APIGW-API-KEY-ID", "733fhqom8j")
                    .addHeader("X-NCP-APIGW-API-KEY", "jwxEUZxCO1lKD0c1R1CDcahGlgm4iy7jLZHVRaAP")
                    .url(SpeekUrl)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);

    }






}
