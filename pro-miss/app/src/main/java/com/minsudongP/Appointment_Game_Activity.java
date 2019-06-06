package com.minsudongP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.minsudongP.Service.PromissService;
import com.minsudongP.Singletone.UrlConnection;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Appointment_Game_Activity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int PROMISS_GAME_READY = 0;
    public static final int PROMISS_GAME_DC_CIRCLE = 1;//점점 줄어드는 원
    public static final int PROMISS_GAME_STOP_CIRCLE = 2;//페이지 시 멈출 때

    NaverMap mMap;
    CircleOverlay circle; //줄어들 원
    int radius = 500;
    Pusher pusher;
    Marker Mymarker;
    HashMap<String,Marker> memberMarker=new HashMap<>();
    Intent intent;
    String appointment_id;
    UrlConnection connection;
//    LocationOverlay locationOverlay;//줄어들 원

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment__game_);

        appointment_id=getIntent().getStringExtra("id");


        //Game Pusher Event Alert///////////////////////////////////////////////////////////////////////////////////////////////
        PusherOptions options = new PusherOptions();
        options.setCluster("ap3");
        pusher = new Pusher("60518d2597abbeaa238c", options);

        Channel channel = pusher.subscribe("ProMiss");

        channel.bind("event_game"+appointment_id, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String Receivedata) {
                System.out.println(Receivedata);

                try{
                    JSONObject object=new JSONObject(Receivedata);

                    if(object.getInt("result")==2000)
                    {
                        final JSONObject data=object.getJSONObject("data");

                        Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MapReSetting(data.getInt("radius"),data.getString("Member"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Appointment_Game_Activity.this,"서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.",Toast.LENGTH_LONG).show();
                                finish(); // 네트워크가 안되면 종료
                            }
                        });
                    }


                }catch (JSONException e)
                {
                    Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Appointment_Game_Activity.this,"네트워크를 확인해주세요",Toast.LENGTH_LONG).show();
                            finish(); // 네트워크가 안되면 종료
                        }
                    });
                }
            }
        });

        pusher.connect();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);



        connection=UrlConnection.shardUrl;

        new Thread()
        {
            @Override
            public void run() {
                connection.GetRequest("api/appointment/getSetting/"+appointment_id,callback);
            }
        }.run();


    }



    private Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Appointment_Game_Activity.this,"네트워크를 확인해주세요",Toast.LENGTH_LONG).show();
                    finish(); // 네트워크가 안되면 종료
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();

            try{
                JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.getInt("result")==2000)
                {
                    final JSONObject data=jsonObject.getJSONObject("data");

                    Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MapReSetting(data.getInt("radius"),data.getString("Member"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Appointment_Game_Activity.this,"서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.",Toast.LENGTH_LONG).show();
                            finish(); // 네트워크가 안되면 종료
                        }
                    });
                }
            }catch (JSONException e)
            {
                Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Appointment_Game_Activity.this,"서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.",Toast.LENGTH_LONG).show();
                        finish(); // 네트워크가 안되면 종료
                    }
                });
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        //약속 목적지가 중앙
        mMap = naverMap;
        LatLng coord = new LatLng(37.5662952, 126.97794509999994);

        Marker marker = new Marker();
        marker.setCaptionText("목적지");
        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setMap(naverMap);

        circle = new CircleOverlay();
        circle.setCenter(new LatLng(37.5670135, 126.9783740)); // 약속 장소 위치
        circle.setRadius(radius); // 타이머에 따른 크기
        circle.setColor(Color.alpha(0)); //투명
        circle.setOutlineWidth(5);
        circle.setOutlineColor(Color.GREEN);
        circle.setMap(mMap);

       // MapReSetting();

        mMap.setCameraPosition(new CameraPosition(coord, 17.0)); // 카메라 위치 셋팅
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("GPS-event-name")
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {    //Service에서 받아온 사용자의 위치에 마커를 표시
            // TODO Auto-generated method stub // Get extra data included in the

            if (intent.getStringExtra("send").equals("error")) {
                Toast.makeText(Appointment_Game_Activity.this, intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
            } else {
                if (Mymarker == null) {
                    Mymarker = new Marker();
                } else {
                    Mymarker.setMap(null);
                }
                Mymarker.setCaptionText("나의 위치");

                Mymarker.setPosition(new LatLng(intent.getDoubleExtra("latitude", 36), intent.getDoubleExtra("longitude", 126)));
                Mymarker.setMap(mMap);
            }
        }

    };

    public void MapReSetting(int radius, String member) { //member는 jsonArray로온다.

        circle.setMap(null);
        circle.setRadius(radius);
        circle.setMap(mMap); //원 초기화

        for(String key:memberMarker.keySet()) // 마커 초기화
        {
            memberMarker.get(key).setMap(null);
        }


        try{
            JSONArray jsonArray=new JSONArray(member);
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject user=jsonArray.getJSONObject(i);

                Marker marker=new Marker();
                marker.setCaptionText(user.getString("name"));
                marker.setPosition(new LatLng(user.getDouble("latitude"),user.getDouble("longitude")));
                marker.setMap(mMap);


                memberMarker.put(user.getString("id"),marker);

            }
        }catch (JSONException e)
        {
            Toast.makeText(Appointment_Game_Activity.this,"서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.",Toast.LENGTH_LONG).show();
            finish(); // 네트워크가 안되면 종료
        }

    }
}
