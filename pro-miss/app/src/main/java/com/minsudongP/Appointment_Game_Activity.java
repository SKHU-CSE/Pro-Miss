package com.minsudongP;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

public class Appointment_Game_Activity extends AppCompatActivity implements OnMapReadyCallback {

    NaverMap mMap;
//    LocationOverlay locationOverlay;//줄어들 원
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment__game_);


        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                locationOverlay.setCircleRadius(100);
            }
        });
    }




    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        //약속 목적지가 중앙
        mMap=naverMap;

        LatLng coord = new LatLng(37.5662952,126.97794509999994);

        Marker marker = new Marker();
        marker.setCaptionText("목적지");

        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setMap(naverMap);

//        locationOverlay=naverMap.getLocationOverlay();
//        locationOverlay.setCircleRadius(300);
//        locationOverlay.setPosition(new LatLng(37.5670135, 126.9783740));
//        locationOverlay.setCircleOutlineColor(Color.GREEN);
//        locationOverlay.setCircleOutlineWidth(5);
//        locationOverlay.setIcon(OverlayImage.fromResource(R.drawable.ic_add_alarm_black_24dp));
//        locationOverlay.setVisible(true);


        CircleOverlay circle = new CircleOverlay();
        circle.setCenter(new LatLng(37.5666102, 126.9783881));
        circle.setRadius(500);
        circle.setColor(Color.alpha(0)); //투명 
        circle.setOutlineWidth(5);
        circle.setOutlineColor(Color.GREEN);
        circle.setMap(naverMap);


        mMap.setCameraPosition(new CameraPosition(coord, 17.0)); // 카메라 위치 셋팅
    }
}
