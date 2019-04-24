package com.minsudongP;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View.OnClickListener MYPageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button) findViewById(R.id.setting_backButton)).setOnClickListener(MYPageListener);

        final Switch swnotice = (Switch) findViewById(R.id.setting_noticeSwitch);
        final Switch swsound = (Switch) findViewById(R.id.setting_soundSwitch);
        final Switch swvive = (Switch) findViewById(R.id.setting_viveSwitch);
        final Switch swvoice = (Switch) findViewById(R.id.setting_voiceSwitch);
        final Switch swbackground = (Switch) findViewById(R.id.setting_backvoiceSwitch);

        swsound.setClickable(false);
        swvive.setClickable(false);
        swbackground.setClickable(false);

        swnotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;

                if (isChecked) {
                    message = "알림이 켜졌습니다";
                    swsound.setClickable(true);
                    swvive.setClickable(true);
                    swsound.setChecked(true);
                    swvive.setChecked(true);
                } else {
                    message = "알림이 꺼졌습니다";
                    swsound.setClickable(false);
                    swvive.setClickable(false);

                    swsound.setChecked(false);
                    swvive.setChecked(false);
                }
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });


        swsound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;

                if (isChecked)
                    message = "소리가 켜졌습니다";
                else
                    message = "소리가 꺼졌습니다";
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
        swvive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;

                if (isChecked)
                    message = "진동이 켜졌습니다";
                else
                    message = "진동이 꺼졌습니다";
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
        swvoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;

                if (isChecked) {
                    message = "음성인식이 켜졌습니다";
                    swbackground.setClickable(true);
                    swbackground.setChecked(true);
                } else {
                    message = "음성인식이 꺼졌습니다";
                    swbackground.setClickable(false);
                    swbackground.setChecked(false);
                }
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
        swbackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;

                if (isChecked)
                    message = "백그라운드 음성인식이 켜졌습니다";
                else
                    message = "백그라운드 음성인식이 꺼졌습니다";
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        ((ConstraintLayout) findViewById(R.id.setting_logoutLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
