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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mommoo.permission.MommooPermission;
import com.mommoo.permission.listener.OnPermissionDenied;
import com.mommoo.permission.repository.DenyInfo;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.List;

public class AttendingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_detail);
        View.OnClickListener AttendingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AttendingDetailActivity.this,AttendingActivity.class);
                startActivity(intent);
                finish();
            }
        };
        ((Button) findViewById(R.id.atd_detail_backBtn)).setOnClickListener(AttendingListener);


        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
                    Intent intent = new Intent(AttendingDetailActivity.this, Appointment_Game_Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
