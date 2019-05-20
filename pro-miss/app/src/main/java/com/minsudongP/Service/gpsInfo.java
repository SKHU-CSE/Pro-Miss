package com.minsudongP.Service;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.minsudongP.App;
import com.minsudongP.Appointment_Game_Activity;
import com.minsudongP.MainActivity;
import com.minsudongP.R;

import static com.minsudongP.App.CHAANEL_ID;

public class gpsInfo extends Service implements LocationListener {


    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat; // 위도
    double lon; // 경도

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("gps","startcommand");
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

        }else {
            Intent notificationIntent=new Intent(this, Appointment_Game_Activity.class);

            PendingIntent pendingIntent=PendingIntent.getActivity(this
                    ,0,notificationIntent,0); //알람을 눌렀을 때 해당 엑티비티로

            Notification notification=new NotificationCompat.Builder(this,CHAANEL_ID)
                    .setContentTitle("Game Service")
                    .setContentText("현재 위치 추적중")
                    .setSmallIcon(R.drawable.ic_add_alarm_black_24dp)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(2,notification);

            locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);




            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    this);

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);//더 이상 필요하지 않을 때, 자원 누락을 방지하기 위해

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendMessage(double latitude,double longitude){
        Log.d("GPSService", "Broadcasting message");
        Intent intent = new Intent("GPS-event-name");
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude", longitude);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d("gps","LocationChanged");
        this.location=location;
        sendMessage(location.getLatitude(),location.getLongitude());
       // Log.d("gps",location.getLatitude()+","+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("gps","statusChange");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("gps","providerEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("gps","providerDisabled");
    }
}
