package com.minsudongP;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        ((ImageButton)findViewById(R.id.main_MakeProtocal)).setOnClickListener(MakeProtocalListenr);
        ((TextView)findViewById(R.id.main_MakeProtocal_text)).setOnClickListener(MakeProtocalListenr);
        ((LinearLayout)findViewById(R.id.main_MyPage)).setOnClickListener(MyPageListener);
        ((TextView)findViewById(R.id.main_MyPage_text)).setOnClickListener(MyPageListener);
        ((ImageButton)findViewById(R.id.main_ShowProtocal)).setOnClickListener(AttendingListener);

    }
}
