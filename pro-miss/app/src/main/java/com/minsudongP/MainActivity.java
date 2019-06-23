package com.minsudongP;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Model.RangkingAdapter;
import com.minsudongP.Model.RangkingItem;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;
import com.mommoo.permission.MommooPermission;
import com.mommoo.permission.listener.OnPermissionDenied;
import com.mommoo.permission.repository.DenyInfo;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.channel.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {


    final int MAIN_TO_CHARGE = 3000;
    final int MAIN_TO_FOLLOW = 4000;
    View view;  // main xml 이곳에 레이아웃을 추가한다.
    Pusher pusher;

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList;
    ArrayList<RangkingItem> rangkingItems;
    RangkingAdapter adapter_rangking;
    TextView FIne;
    TextView totalTime;
    TextView money;
    TextView lastTime;
    ProgressBar appoint_progress;
    SeekBar appoint_marker;

    // OnClick
    View.OnClickListener MakeProtocalListenr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, appointment.class);
            startActivity(intent);
            //되돌아 올수 있어 finish()를 하지 않음
        }
    };

    View.OnClickListener AttendingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AttendingActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener AlertListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AlertActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener SettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener MyPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
            startActivity(intent);
        }
    };



    View.OnClickListener AttendingDetailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AttendingDetailActivity.class);
            intent.putExtra("status", UserInfor.shared.getAppointment_status());
            intent.putExtra("id", UserInfor.shared.getAppointment_id());
            startActivity(intent);
        }
    };
    // 팔로우하기
    View.OnClickListener FollowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, FollowActivity.class);
            startActivityForResult(intent, MAIN_TO_FOLLOW);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view=getLayoutInflater().inflate(R.layout.activity_main,null);



        if(UserInfor.shared.getAppointment_status()==1)
        {

                Status_Appointment();
        }else
        {
            Status_Idle();
        }





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



 //       ((ImageButton) findViewById(R.id.main_MakeProtocal)).setOnClickListener(MakeProtocalListenr);

        //((TextView) findViewById(R.id.main_MyPage_text)).setOnClickListener(MyPageListener);
        //((ImageView) findViewById(R.id.main_alertbtn)).setOnClickListener(AlertListener);
//        ((Button)findViewById(R.id.testButton)).setOnClickListener(locationUpdateTest);

    }

    void Status_Idle(){
        View subView=getLayoutInflater().inflate(R.layout.activity_status_idle, (ViewGroup) view,false);

        RecyclerView recyclerView = subView.findViewById(R.id.main_recycleview);
        arrayList=new ArrayList<PromissItem>();

        adapter = new AllRecyclerAdapter(arrayList, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        final UrlConnection urlConnection = UrlConnection.shardUrl;
        new Thread() {
            @Override
            public void run() {
                super.run();
                urlConnection.GetRequest("api/followList/"+UserInfor.shared.getId_num(), followingCallback );
            }
        }.run();



        ((ViewGroup) view).addView(subView);
        setContentView(view);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String getTime = sdf.format(date);


        TextView successPercent=subView.findViewById(R.id.main_rangeNum_text);
        TextView successPercent_text=subView.findViewById(R.id.main_rangePercent_text);
        ProgressBar progressbar=findViewById(R.id.main_range_progress);

        int success=UserInfor.shared.getSuccess_appoint_num();
        int total=UserInfor.shared.getAppoint_num();
        successPercent_text.setText(success+"/"+total);
        int percent=0;
        if(total!=0)
        percent=success/total *100;

        successPercent.setText(percent+"%");
        progressbar.setProgress(percent);
        progressbar.setMax(100);

        // 금액 충전 액티비티로 이동
        findViewById(R.id.main_money_addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChargeActivity.class);
                startActivityForResult(intent, MAIN_TO_CHARGE);
            }
        });


        money=subView.findViewById(R.id.main_money_value);
        money.setText(UserInfor.shared.getMoney()+"");
        ((Button) subView.findViewById(R.id.main_makeAppointment)).setOnClickListener(MakeProtocalListenr);
        ((TextView)subView.findViewById(R.id.main_current_date)).setText(getTime);
        ((TextView) subView.findViewById(R.id.main_notify)).setOnClickListener(AlertListener);
        ((Button) subView.findViewById(R.id.main_setting)).setOnClickListener(SettingListener);
        ((Button) subView.findViewById(R.id.main_addFollow)).setOnClickListener(FollowListener);

        ((TextView)subView.findViewById(R.id.main_user_name)).setText(UserInfor.shared.getName());
        ((TextView)subView.findViewById(R.id.main_user_id)).setText("ID : "+UserInfor.shared.getID());


        ((Button)findViewById(R.id.main_range_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PastActivity.class);
                startActivity(intent);
            }
        });

        if(UserInfor.shared.getAppointment_address()!=null) {
            //약속이 있을때
            ((Button) subView.findViewById(R.id.main_more_btn)).setOnClickListener(AttendingDetailListener);
            ((TextView) subView.findViewById(R.id.main_appoint_name)).setText(UserInfor.shared.getAppointment_address());
        }else
        {
            ((Button) subView.findViewById(R.id.main_more_btn)).setVisibility(View.GONE);
            ((TextView) subView.findViewById(R.id.main_appoint_name)).setVisibility(View.GONE);
            ((TextView)subView.findViewById(R.id.main_appoint_notice)).setText("약속이 없습니다. 약속을 만들어주세요.");
        }

        ((Button)findViewById(R.id.main_appointmentList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendingActivity.class);
                startActivity(intent);
            }
        });

    }


    void Status_Appointment()
    {
        View subView=getLayoutInflater().inflate(R.layout.activity_status_appoint, (ViewGroup) view,false);
        ((ViewGroup) view).addView(subView);
        setContentView(view);


        RecyclerView recyclerView = subView.findViewById(R.id.main_recycleview);
        rangkingItems=new ArrayList<RangkingItem>();

        adapter_rangking = new RangkingAdapter(rangkingItems, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_rangking);

        FIne =findViewById(R.id.main_game_Fine_tv);
        lastTime=findViewById(R.id.main_game_Time_tv);
        totalTime=findViewById(R.id.main_game_Time_Subtv);
        appoint_marker=findViewById(R.id.main_Seekbar);
        appoint_progress=findViewById(R.id.main_progressbar);
        ((Button) subView.findViewById(R.id.main_more_btn)).setOnClickListener(AttendingDetailListener);
        ((TextView)subView.findViewById(R.id.main_appoint_date)).setText(UserInfor.shared.getAppintment_date());
        ((TextView)subView.findViewById(R.id.main_appoint_name)).setText(UserInfor.shared.getAppointment_address());

        setSeekberThumb(appoint_marker,getResources());
        //Game Pusher Event Alert///////////////////////////////////////////////////////////////////////////////////////////////
        PusherOptions options = new PusherOptions();
        options.setCluster("ap3");
        pusher = new Pusher("60518d2597abbeaa238c", options);

        Channel channel = pusher.subscribe("ProMiss");

        channel.bind("event_game"+ UserInfor.shared.getAppointment_id(), new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String Receivedata) {
                System.out.println(Receivedata);

                rangkingItems.clear();
                try{
                    JSONObject object=new JSONObject(Receivedata);

                    if(object.getInt("result")==2000)
                    {
                        final String time_r=object.getString("time");
                        final String total=object.getString("totalTime");
                        final int appoint_radius=object.getInt("appoint_radius");
                        JSONObject user=object.getJSONObject("data");
//                        final String fine_num=user.getString("Fine_current");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lastTime.setText(time_r);
                                //                              FIne.setText(fine_num);
                                // appoint_marker.setProgress(user_radius);
                                appoint_progress.setProgress(appoint_radius);
                                totalTime.setText("/"+total+"분");
                            }
                        });

                        object=object.getJSONObject("data");
                        object=object.getJSONObject("data");
                        JSONArray member=object.getJSONArray("Member");

                        for(int i=0;i<member.length();i++)
                        {
                            JSONObject user_o=member.getJSONObject(i);
                            final String Fine= user_o.getString("Fine_current");
                            final int radius=user_o.getInt("user_radius");
                            if((""+user_o.getInt("user_id")).equals(UserInfor.shared.getId_num()))
                            {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lastTime.setText(time_r);
                                        FIne.setText(Fine);
                                        appoint_marker.setProgress(radius);
//                                        appoint_progress.setProgress(appoint_radius);
//                                        totalTime.setText("/"+total+"분");
                                    }
                                });
                            }

                                rangkingItems.add(new RangkingItem(user_o.getInt("user_radius"), user_o.getString("name")));

                        }
                        Collections.sort(rangkingItems);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter_rangking.notifyDataSetChanged();
                            }
                        });

                    }else{
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"서버가 문제가 있습니다. 서버관리자에게 문의를 주세요.",Toast.LENGTH_LONG).show();
                                finish(); // 네트워크가 안되면 종료
                            }
                        });
                    }


                }catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"네트워크를 확인해주세요",Toast.LENGTH_LONG).show();
                            finish(); // 네트워크가 안되면 종료
                        }
                    });
                }
            }
        });

        pusher.connect();


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        subView.findViewById(R.id.main_Seekbar).setOnTouchListener(new View.OnTouchListener() { //사용자 터치 막기
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ((TextView) subView.findViewById(R.id.main_notify)).setOnClickListener(AlertListener);
        ((Button) subView.findViewById(R.id.main_MyPage)).setOnClickListener(MyPageListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pusher!=null)
        pusher.disconnect();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            final UrlConnection urlConnection = UrlConnection.shardUrl;
            switch (requestCode) {
                case MAIN_TO_CHARGE:
                    final HashMap<String, String> hash = new HashMap<>();
                    hash.put("id", UserInfor.shared.getId_num());
                    hash.put("money", data.getStringExtra("money"));
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            urlConnection.PostRequest("api/money/add", moneyCallback, hash);
                        }
                    }.run();
                    break;
                case MAIN_TO_FOLLOW:
                    arrayList.clear();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            urlConnection.GetRequest("api/followList/"+UserInfor.shared.getId_num(), followingCallback );
                        }
                    }.run();
                    break;
            }
        }
    }
    private Callback moneyCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("result") == 1000) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    final JSONObject object = jsonObject.getJSONObject("data");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(MainActivity.this, (Integer.parseInt(object.getString("money")) - UserInfor.shared.getMoney()) + "원이 충전되었습니다.", Toast.LENGTH_LONG).show();
                                money.setText(object.getString("money") + "원");
                                UserInfor.shared.setMoney(Integer.parseInt(object.getString("money")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private final Callback followingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String s = response.body().string();

            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("result") == 2000) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i<data.length(); i++) {
                        JSONObject followerObj = data.getJSONObject(i);

                        int id = followerObj.getInt("Following");
                        String image = followerObj.getString("Image");
                        String name = followerObj.getString("name");

                        PromissItem item = new PromissItem(PromissType.FriendList, id, image, name);
                        arrayList.add(item);
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    public static void setSeekberThumb(final SeekBar seekBar, final Resources res) {
        seekBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                Drawable thumb = res.getDrawable(R.drawable.marker_me);
                int h = seekBar.getMeasuredHeight() * 1; // 8 * 1.5 = 12
                int w = h;
                Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
                Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                seekBar.setThumb(newThumb);

                seekBar.getViewTreeObserver().removeOnPreDrawListener(this);

                return true;
            }
        });
    }

}
