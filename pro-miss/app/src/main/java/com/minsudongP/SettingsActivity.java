package com.minsudongP;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.minsudongP.Service.PromissService;
import com.minsudongP.Service.Recoginition;
import com.mommoo.permission.MommooPermission;
import com.mommoo.permission.listener.OnPermissionDenied;
import com.mommoo.permission.repository.DenyInfo;

import java.util.List;

import static com.minsudongP.SaveSharedPreference.clearUserInfo;
import static com.minsudongP.SaveSharedPreference.getSetting_Alert;
import static com.minsudongP.SaveSharedPreference.getSetting_Voice;
import static com.minsudongP.SaveSharedPreference.setSetting_Alert;
import static com.minsudongP.SaveSharedPreference.setSetting_Voice;

public class SettingsActivity extends BaseActivity {

    PromissService mservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Intent intent = new Intent(SettingsActivity.this, Recoginition.class);
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

        if(getSetting_Alert(getApplicationContext())==1)
        {
            swnotice.setChecked(true);
        }
        if(getSetting_Voice(getApplicationContext())==1)
        {
            swvoice.setChecked(true);
        }
        swnotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;
                Intent Service = new Intent(SettingsActivity.this, PromissService.class);
                if (isChecked) {
                    message = "알림이 켜졌습니다";
                    swsound.setClickable(true);
                    swvive.setClickable(true);
                    swsound.setChecked(true);
                    swvive.setChecked(true);
                    setSetting_Alert(getApplicationContext(),1);
                    ContextCompat.startForegroundService(SettingsActivity.this,Service);

                } else {
                    message = "알림이 꺼졌습니다";
                    swsound.setClickable(false);
                    swvive.setClickable(false);
                    stopService(Service);
                    swsound.setChecked(false);
                    swvive.setChecked(false);
                    setSetting_Alert(getApplicationContext(),0);
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

                    // 녹음 권한 허용 필요
                    new MommooPermission.Builder(getApplicationContext())
                            .setPermissions(Manifest.permission.RECORD_AUDIO)
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
                                    "음성 인식 기능을 사용하려면 다음 권한을 허용해주세요.\n" +
                                            "1. 녹음")
                            .setOfferGrantPermissionData("[설정] > [권한]으로 이동",
                                    "1. '설정'에 들어가세요.\n" +
                                            "2. '권한'을 클릭하세요.\n" +
                                            "3. 녹음 권한을 허용해주세요.")
                            .build()
                            .checkPermissions();

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        message = "음성인식이 켜졌습니다";
                        swbackground.setClickable(true);
                        swbackground.setChecked(true);
                        Intent service = new Intent(SettingsActivity.this, Recoginition.class);
                        ContextCompat.startForegroundService(SettingsActivity.this, service);//음성인식 서비스 실행
                    } else {
                        message = "[설정] > [권한]에서 '녹음' 권한을 허용해 주세요.";
                        swvoice.setChecked(false);
                    }
                } else {
                    message = "음성인식이 꺼졌습니다";
                    swbackground.setClickable(false);
                    swbackground.setChecked(false);
                    stopService(intent);
                }
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
        swbackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String message;

                if (isChecked) {
                    message = "백그라운드 음성인식이 켜졌습니다";
                    Intent service = new Intent(SettingsActivity.this, Recoginition.class);
                    ContextCompat.startForegroundService(SettingsActivity.this, service);
                    setSetting_Voice(getApplicationContext(),1);
                } else {
                    Intent service = new Intent(SettingsActivity.this, Recoginition.class);
                    stopService(service);
                    message = "백그라운드 음성인식이 꺼졌습니다";
                    setSetting_Voice(getApplicationContext(),0);

                }
                Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        ((LinearLayout) findViewById(R.id.logout_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();

                // 카카오 로그아웃
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                        return;
                    }
                });

                // 자동 로그인 값 제거
                clearUserInfo(getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
    }




}
