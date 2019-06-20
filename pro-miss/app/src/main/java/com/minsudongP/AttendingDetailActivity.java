package com.minsudongP;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Singletone.UrlConnection;
import com.mommoo.permission.MommooPermission;
import com.mommoo.permission.listener.OnPermissionDenied;
import com.mommoo.permission.repository.DenyInfo;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AttendingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    NaverMap naverMap;

    int Appointment_id;
    int status;

    TextView appointment_date;
    TextView appointment_time;
    TextView appointment_Fine_time;
    TextView appointment_Fine_value;
    TextView appointment_address;
    TextView appointment_timer;

    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_detail);
        View.OnClickListener AttendingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button) findViewById(R.id.atd_detail_backBtn)).setOnClickListener(AttendingListener);


        recyclerView=findViewById(R.id.attending_detail_recycle);
        adapter=new AllRecyclerAdapter(items,this);

        recyclerView.setAdapter(adapter);

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {

            }
        });
        appointment_address=findViewById(R.id.atd_detail_title);
        appointment_date=findViewById(R.id.atd_detail_card1_DateText);
        appointment_time=findViewById(R.id.atd_detail_card1_TimeText);
        appointment_Fine_time=findViewById(R.id.atd_detail_min);
        appointment_Fine_value=findViewById(R.id.atd_detail_money);
        appointment_timer=findViewById(R.id.atd_detail_timer_t2);

        Appointment_id= getIntent().getIntExtra("id",0);
        status=getIntent().getIntExtra("status",0);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        new Thread()
        {
            @Override
            public void run() {
                UrlConnection.shardUrl.GetRequest("api/appointment/appointment/"+Appointment_id,callback);
            }
        }.run();
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                // 위치 권한 허용
                new MommooPermission.Builder(getApplicationContext())
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                        .setOnPermissionDenied(new OnPermissionDenied() {
                            @Override
                            public void onDenied(List<DenyInfo> deniedPermissionList) {
                                for (DenyInfo denyInfo : deniedPermissionList) {
                                    System.out.println("isDenied : " + denyInfo.getPermission() + " , " +
                                            "userNeverSeeChecked : " + denyInfo.isUserNeverAskAgainChecked());
                                }
                            }
                        })
                        .setPreNoticeDialogData("권한 허용 요청",
                                "실시간 위치 공유 기능을 사용하려면 다음 권한을 허용해주세요.\n" +
                                        "1. 위치")
                        .setOfferGrantPermissionData("[설정] > [권한]으로 이동",
                                "1. '설정'에 들어가세요.\n" +
                                        "2. '권한'을 클릭하세요.\n" +
                                        "3. 위치 권한을 허용해주세요.")
                        .build()
                        .checkPermissions();

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    if(status==1) {
                        Intent intent = new Intent(AttendingDetailActivity.this, Appointment_Game_Activity.class);
                        intent.putExtra("id",Appointment_id);
                        startActivity(intent);
                    }else
                    {
                        Toast.makeText(AttendingDetailActivity.this,"약속이 시작하지 않았습니다.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private Callback callback=new Callback(){

        @Override
        public void onFailure(Call call, IOException e) {
            AttendingDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AttendingDetailActivity.this,"네트워크 문제로 약속을 불러오지 못했습니다.",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String s=response.body().string();

            try{
                final JSONObject data=new JSONObject(s);

                if(data.getInt("result")==2000)
                {
                    final JSONObject result=data.getJSONObject("data");
                    final JSONObject object=result.getJSONObject("appoint");
                    AttendingDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                appointment_address.setText(object.getString("address"));
                                appointment_date.setText(object.getString("date"));
                                appointment_time.setText(object.getString("date_time").substring(0,5));
                                appointment_Fine_time.setText(object.getString("Fine_time"));
                                appointment_Fine_value.setText(object.getString("Fine_money"));
                                int timer=object.getInt("Timer");
                                String time=timer/60+"시간 ";
                                if(timer%60!=0)
                                    time= time+timer%60+"분";
                                appointment_timer.setText(time);
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                    JSONObject memberO=result.getJSONObject("member");

                    memberO =memberO.getJSONObject("data");
                    final JSONArray member=memberO.getJSONArray("Member");
                    for(int i=0;i<member.length();i++)
                    {
                        JSONObject user=member.getJSONObject(i);
                        items.add(new PromissItem(PromissType.MEMBER_LIST, user.getString("Image"), user.getString("latitude"), user.getString("longitude"), user.getString("name")));

                    }

                }
            }catch (JSONException e)
            {
                AttendingDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AttendingDetailActivity.this,"서버 문제로 약속을 불러오지 못했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
