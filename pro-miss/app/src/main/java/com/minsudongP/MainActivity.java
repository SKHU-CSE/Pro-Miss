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

                new Thread() {

                    @Override
                    public void run() {
                        UrlConnection connection= UrlConnection.shardUrl;
                        connection.PostSpeekRequest("음성 테스트입니다. 남수형 바보",new

                                Callback() {
                                    @Override
                                    public void onFailure (Call call, IOException e){

                                    }

                                    @Override
                                    public void onResponse (Call call, Response response) throws IOException {

                                        Log.d("code",""+response.code());
                                        if (response.code() == 200) {

/*
                                            InputStream is = response.body().byteStream();
                                            int read = 0;
                                            byte[] bytes = new byte[1024];
                                            // Creates randomly named MP3 files
                                           // String tempname = Long.valueOf(new Date().getTime()).toString();
                                            File f = new File(getApplicationContext().getFilesDir().getPath().toString()+ "speek"+ ".mp3");
                                            f.createNewFile();
                                            OutputStream outputStream = new FileOutputStream(f);
                                            while ((read = is.read(bytes)) != -1) {
                                                outputStream.write(bytes, 0, read);
                                            }
                                            outputStream.flush();
                                            outputStream.close();
                                            is.close();
                                            */

                                            byte[] bytes=response.body().bytes();
                                            Log.d("code",""+bytes.length);
                                            File f = new File(getApplicationContext().getFilesDir().getPath().toString()+ "speek"+ ".mp3");

                                            FileOutputStream outputStream = new FileOutputStream(f);

                                            outputStream.write(bytes,0,bytes.length);
                                            outputStream.flush();
                                            outputStream.close();

                                            FileInputStream fs = new FileInputStream(f);



                                            MediaPlayer mediaPlayer = new MediaPlayer();
                                            mediaPlayer.setDataSource(fs.getFD());
                                            mediaPlayer.prepare();
                                            fs.close();
                                            // mediaPlayer.prepareAsync();
                                            mediaPlayer.start();
//                                            final SoundPool soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
//
//
//                                            final int music=soundPool.load(outputStream.getFD(),0,f.length(),1);
//                                           // int music=soundPool.load("speek.mp3",1);
//
//
//                                            MainActivity.this.runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    soundPool.play(music, 1f, 1f, 1, 0, 1f);
//                                                }
//                                            });

                                        }
                                    }
                                });
                    }
                }.start();
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
