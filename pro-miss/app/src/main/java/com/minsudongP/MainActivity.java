package com.minsudongP;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mommoo.permission.MommooPermission;
import com.mommoo.permission.listener.OnPermissionDenied;
import com.mommoo.permission.repository.DenyInfo;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends BaseActivity {

    Button testButton;
    TextView testText;
    boolean isUpdating = false;


    Pusher pusher;
    final int REQUEST_CODE_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view=getLayoutInflater().inflate(R.layout.activity_main,null);

        View subView=getLayoutInflater().inflate(R.layout.activity_status_appoint, (ViewGroup) view,false);

        ((ViewGroup) view).addView(subView);


        //Game Pusher Event Alert///////////////////////////////////////////////////////////////////////////////////////////////
        PusherOptions options = new PusherOptions();
        options.setCluster("ap3");
        pusher = new Pusher("60518d2597abbeaa238c", options);

        Channel channel = pusher.subscribe("ProMiss");

//        channel.bind("event_game"+appointment_id, new SubscriptionEventListener() {
//            @Override
//            public void onEvent(String channelName, String eventName, final String Receivedata) {
//                System.out.println(Receivedata);
//
//                try{
//                    JSONObject object=new JSONObject(Receivedata);
//
//                    if(object.getInt("result")==2000)
//                    {
//                        final String time_r=object.getString("time");
//                        final String total=object.getString("totalTime");
//                        Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                total_game.setText(time_r);
//                                timer.setText("/"+total+"분");
//                            }
//                        });
//
//                        final JSONObject data=object.getJSONObject("data");
//                        final JSONObject data2=data.getJSONObject("data");
//
//
//                        if(circle!=null)
//                            MapReSetting(data2.getDouble("radius"),data2.getString("Member"));
//                    }else{
//                        Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(Appointment_Game_Activity.this,"서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.",Toast.LENGTH_LONG).show();
//                                finish(); // 네트워크가 안되면 종료
//                            }
//                        });
//                    }
//
//
//                }catch (JSONException e)
//                {
//                    e.printStackTrace();
//                    Appointment_Game_Activity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(Appointment_Game_Activity.this,"네트워크를 확인해주세요",Toast.LENGTH_LONG).show();
//                            finish(); // 네트워크가 안되면 종료
//                        }
//                    });
//                }
//            }
//        });
//
//        pusher.connect();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        setContentView(view);
        subView.findViewById(R.id.main_Seekbar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        // 권한 허용 (위치, 녹음)
        new MommooPermission.Builder(this)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO)
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
                        "프로미스를 사용하려면 다음 권한을 허용해주세요.\n" +
                                "1. 위치: 실시간 위치 공유\n" +
                                "2. 녹음: 음성 챗봇")
                .setOfferGrantPermissionData("[설정] > [권한]으로 이동",
                        "1. '설정'에 들어가세요.\n" +
                                "2. '권한'을 클릭하세요.\n" +
                                "3. 모든 권한을 허용해주세요.")
                .build()
                .checkPermissions();




        // OnClick
        View.OnClickListener MakeProtocalListenr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, appointment.class);
                startActivity(intent);
                //되돌아 올수 있어 finish()를 하지 않음
            }
        };




        View.OnClickListener MyPageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        };


        View.OnClickListener AttendingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendingActivity.class);
                startActivity(intent);
            }
        };


        View.OnClickListener AlertListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlertActivity.class);
                startActivity(intent);
            }
        };

 //       ((ImageButton) findViewById(R.id.main_MakeProtocal)).setOnClickListener(MakeProtocalListenr);
        ((TextView) subView.findViewById(R.id.main_notify)).setOnClickListener(MakeProtocalListenr);
        ((Button) subView.findViewById(R.id.main_MyPage)).setOnClickListener(MyPageListener);
        //((TextView) findViewById(R.id.main_MyPage_text)).setOnClickListener(MyPageListener);
        ((Button) findViewById(R.id.main_more_btn)).setOnClickListener(AttendingListener);
        //((ImageView) findViewById(R.id.main_alertbtn)).setOnClickListener(AlertListener);
//        ((Button)findViewById(R.id.testButton)).setOnClickListener(locationUpdateTest);

    }
}
