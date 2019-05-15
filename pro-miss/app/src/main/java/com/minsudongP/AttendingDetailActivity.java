package com.minsudongP;

import android.content.Intent;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class AttendingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    NaverMap naverMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_detail);
        View.OnClickListener MainPageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button) findViewById(R.id.atd_detail_backBtn)).setOnClickListener(MainPageListener);


        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

    }




    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                Intent intent = new Intent(AttendingDetailActivity.this, Appointment_Game_Activity.class);
                startActivity(intent);
            }
        });
    }


}
