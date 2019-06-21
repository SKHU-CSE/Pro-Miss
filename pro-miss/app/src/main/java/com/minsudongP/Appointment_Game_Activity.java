package com.minsudongP;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Service.PromissService;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Appointment_Game_Activity extends BaseActivity implements OnMapReadyCallback {

    NaverMap mMap;
    CircleOverlay circle; //줄어들 원
    Pusher pusher;
    boolean first=true;
    HashMap<String,Marker> memberMarker=new HashMap<>();
    Intent intent;
    int appointment_id;
    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();
    UrlConnection connection;

    TextView Fine;
    TextView total_game;
    TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment__game_);

        appointment_id=getIntent().getIntExtra("id",0);
        adapter=new AllRecyclerAdapter(arrayList,this);
        recyclerView=findViewById(R.id.game_member_recycle);

        Fine=findViewById(R.id.game_Fine_tv);
        total_game=findViewById(R.id.game_Time_tv);
        timer=findViewById(R.id.game_Time_Subtv);

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {
                CircleImageView circleImageView;
//                if(select_position!=-1)
//                {
//                    circleImageView=arrayList.get(select_position).get
//                }

              //  @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animation = ObjectAnimator.ofFloat(view.getParent(), "translationY", +100f);
              //  animation.setDuration(2000);
              //  animation.start();
                adapter.notifyDataSetChanged();
                circleImageView=(CircleImageView)view;
               circleImageView.setBorderColor(getResources().getColor(R.color.colorAccent));
               circleImageView.setBorderWidth(6);

                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(Double.parseDouble(arrayList.get(position).getPositionX()), Double.parseDouble(arrayList.get(position).getPositionY())))
                        .animate(CameraAnimation.Easing,1000);
                mMap.moveCamera(cameraUpdate);


            }
        });
        recyclerView.setAdapter(adapter);
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
                        final String time_r=object.getString("time");
                        final String total=object.getString("totalTime");
                        Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               total_game.setText(time_r);
                               timer.setText("/"+total+"분");
                            }
                        });

                        final JSONObject data=object.getJSONObject("data");
                        final JSONObject data2=data.getJSONObject("data");


                        if(circle!=null)
                            MapReSetting(data2.getDouble("radius"),data2.getString("Member"));
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
                    e.printStackTrace();
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
                    final JSONObject appoint=data.getJSONObject("appointment");
                    JSONObject member=data.getJSONObject("member");
                    final JSONObject real_member=member.getJSONObject("data");



                            try {
                                circle = new CircleOverlay();
                                circle.setColor(Color.alpha(0)); //투명
                                circle.setOutlineWidth(5);
                                circle.setOutlineColor(Color.GREEN);
                            final LatLng coord = new LatLng(appoint.getDouble("latitude"),appoint.getDouble("longitude"));

                            circle.setCenter(coord); // 약속 장소 위치
                            final Marker marker = new Marker();
                            marker.setCaptionText("목적지");
                            marker.setPosition(coord);
                            Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    marker.setMap(mMap);

                                }
                            });

                            new Thread(){
                                @Override
                                public void run() {

                                    try {

                                        MapReSetting(real_member.getInt("radius"), real_member.getString("Member"));
                                    }catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }.run();


                            Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMap.setCameraPosition(new CameraPosition(coord, 11.0)); // 카메라 위치 셋팅
                                }
                            });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



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

        mMap.setLiteModeEnabled(true);
//        mMap.setMapType(NaverMap.MapType.Navi);
//
//        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>18)
//        mMap.setNightModeEnabled(true);
       // MapReSetting();

        connection=UrlConnection.shardUrl;

        new Thread()
        {
            @Override
            public void run() {
                connection.GetRequest("api/appointment/getSetting/"+appointment_id,callback);
            }
        }.run();

    }




    public void MapReSetting(final double radius, final String member) { //member는 jsonArray로온다.

        Log.d("결과",member);


                 arrayList.clear();
                Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circle.setMap(null);
                        circle.setRadius(radius);

                        circle.setMap(mMap); //원 초기화
                    }
                });





                try {

                    JSONArray jsonArray = new JSONArray(member);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        final JSONObject user = jsonArray.getJSONObject(i);



                        //marker.setIcon(OverlayImage.fromBitmap(Glide.with(getApplicationContext()).asBitmap().load(user.getString("Image")).error(R.drawable.face).submit().get()));
                       final LatLng location=new LatLng(user.getDouble("latitude"), user.getDouble("longitude"));

                        final int user_id=user.getInt("user_id");
                        final String FineText=user.getString("Fine_current");
                        final String name=user.getString("name");
                        Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    memberMarker.get(user_id + "").setPosition(location);
                                }catch (NullPointerException e)
                                {
                                    Marker marker=new Marker();
                                    marker.setCaptionText(name);
                                    marker.setPosition(location);
                                    marker.setMap(mMap);
                                    memberMarker.put(user_id+"",marker);
                                }
                                if(UserInfor.shared.getId_num().equals(""+user_id))
                                {
                                    Fine.setText(FineText);
                                }
                            }
                        });

                        arrayList.add(new PromissItem(PromissType.MEMBER_LIST, user.getString("Image"), user.getString("latitude"), user.getString("longitude"), user.getString("name")));



                        if(first) {
                            first=false;
                            Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(Appointment_Game_Activity.this, "서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.", Toast.LENGTH_LONG).show();
                    finish(); // 네트워크가 안되면 종료
                }

    }
}
