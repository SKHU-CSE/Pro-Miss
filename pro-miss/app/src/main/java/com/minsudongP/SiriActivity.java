package com.minsudongP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.dnkilic.waveform.WaveView;

public class SiriActivity extends Activity {

    private WaveView mWaveView;
    PowerManager powerManager;

    PowerManager.WakeLock wakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siri);
        mWaveView=findViewById(R.id.vw2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);


        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");

        wakeLock.acquire(); // WakeLock 깨우기

        wakeLock.release(); // WakeLock 해제

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                start(new View(getApplicationContext()));
                startSpeech(new View(getApplicationContext()));
            }
        }, 500);// 0.5초 정도 딜레이를 준 후 시작


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                finish();
            }
        }, 5500);// 0.5초 정도 딜레이를 준 후 시작
    }

    public void reset() {
        mWaveView.stop();
    }

    public void start(View v) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWaveView.initialize(dm);
    }

    public void startSpeech(View v) {
        mWaveView.speechStarted();
    }

    public void endSpeech() {
        mWaveView.speechEnded();
    }

    public void pauseSpeech() {
        mWaveView.speechPaused();
    }
}
