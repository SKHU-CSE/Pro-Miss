package com.minsudongP;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.minsudongP.Service.Recoginition;
import com.minsudongP.Singletone.UrlConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    Button testButton;
    TextView testText;
    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testButton = findViewById(R.id.testButton);
//        testText = findViewById(R.id.testText);

        // location
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                testText.setText("위치정보 : " + provider + "\n" +
                        "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n" +
                        "고도  : " + altitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        View.OnClickListener locationUpdateTest=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdating){
                    lm.removeUpdates(gpsLocationListener);
                    testButton.setText("위치 업데이트 테스트");
                    testText.setText("..");
                    isUpdating=false;
                }
                else {
                    if (Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                0);
                    } else {
                        isUpdating=true;
                        testButton.setText("업데이트 중단");
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        String provider = location.getProvider();
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        double altitude = location.getAltitude();

                        testText.setText("위치정보 : " + provider + "\n" +
                                "위도 : " + longitude + "\n" +
                                "경도 : " + latitude + "\n" +
                                "고도  : " + altitude);

                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                    }
                }
            }
        };



        // OnClick
        View.OnClickListener MakeProtocalListenr=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,appointment.class);
                startActivity(intent);
                //되돌아 올수 있어 finish()를 하지 않음
            }
        };


        findViewById(R.id.main_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View.OnClickListener MyPageListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MyPageActivity.class);
                startActivity(intent);
            }
        };


        View.OnClickListener AttendingListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AttendingActivity.class);
                startActivity(intent);
            }
        };


        View.OnClickListener AlertListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AlertActivity.class);
                startActivity(intent);
            }
        };

        ((ImageButton)findViewById(R.id.main_MakeProtocal)).setOnClickListener(MakeProtocalListenr);
        ((TextView)findViewById(R.id.main_MakeProtocal_text)).setOnClickListener(MakeProtocalListenr);
        ((LinearLayout)findViewById(R.id.main_MyPage)).setOnClickListener(MyPageListener);
        ((TextView)findViewById(R.id.main_MyPage_text)).setOnClickListener(MyPageListener);
        ((ImageButton)findViewById(R.id.main_ShowProtocal)).setOnClickListener(AttendingListener);
        ((ImageView)findViewById(R.id.main_alertbtn)).setOnClickListener(AlertListener);
//        ((Button)findViewById(R.id.testButton)).setOnClickListener(locationUpdateTest);

    }
}
