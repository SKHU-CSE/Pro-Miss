
package com.minsudongP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.renderscript.Allocation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.mommoo.permission.MommooPermission;
import com.mommoo.permission.listener.OnPermissionDenied;
import com.mommoo.permission.listener.OnPermissionGranted;
import com.mommoo.permission.repository.DenyInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlertActivity extends AppCompatActivity {

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();
    UrlConnection connection;
    AnimatedRecyclerView recyclerView;

    public String GetTime(String time)
    {
        String temp[]=time.split(":");
        String AMorPM="오전";
        try{
            int hour=Integer.parseInt(temp[0]);
            if(hour>=12)
            {
                AMorPM="오후 ";
                temp[0]= hour==12?""+12 :""+(hour-12);
            }else
            {
                AMorPM="오전 ";
            }

            return AMorPM+temp[0]+":"+temp[1];
        }catch (IndexOutOfBoundsException e)
        {
            return time;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        recyclerView = (AnimatedRecyclerView)findViewById(R.id.alert_recycle);
        connection=UrlConnection.shardUrl;

        adapter=new AllRecyclerAdapter(arrayList,AlertActivity.this);

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, final int position) {
                final HashMap<String,String> hash=new HashMap<>();
                try{
                    if(((Button)view).getText().toString().equals("수락"))
                    {
                            // 약속 초대 수락
                        hash.put("appointment_id",""+arrayList.get(position).getAppointment_id());
                        hash.put("user_id",UserInfor.shared.getId_num());
                        hash.put("notification_id",""+arrayList.get(position).getNotification_id());
                        connection.PostRequest("api/appointment/newMember",deleteNotify,hash);
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();

                    }else if(((Button)view).getText().toString().equals("거절")){
                            //해당 테이블 행 삭제
                        hash.put("id",""+arrayList.get(position).getNotification_id());
                       connection.DeleteRequest("api/notification/notifyDelete",deleteNotify,hash);
                       arrayList.remove(position);
                       adapter.notifyDataSetChanged();
                    }else if(((Button)view).getText().toString().equals("갱신")){
                        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        new MommooPermission.Builder(AlertActivity.this)
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
                                .setOnPermissionGranted(new OnPermissionGranted() {
                                    @Override
                                    public void onGranted(List<String> permissionList) {
                                        final @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        new Thread() {
                                            @Override
                                            public void run() {

                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("user_id",UserInfor.shared.getId_num());
                                                hashMap.put("appointment_id",""+arrayList.get(position).getAppointment_id());
                                                hashMap.put("notification_id",""+arrayList.get(position).getNotification_id());
                                                hashMap.put("latitude",""+location.getLatitude());
                                                hashMap.put("longitude",""+location.getLongitude());

                                                connection.PostRequest("api/appointment/gpsReload",GPScallback,hashMap);
                                            }

                                        }.run();
                                    }
                                })
                                .setPreNoticeDialogData("권한 허용 요청",
                                        "프로미스를 사용하려면 다음 권한을 허용해주세요.\n" +
                                                "위치: 실시간 위치 공유\n")
                                .setOfferGrantPermissionData("[설정] > [권한]으로 이동",
                                        "1. '설정'에 들어가세요.\n" +
                                                "2. '권한'을 클릭하세요.\n" +
                                                "3. 모든 권한을 허용해주세요.")
                                .build()
                                .checkPermissions();



                    }
                }catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
            }
        });
        View.OnClickListener AlertListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button)findViewById(R.id.alert_backButton)).setOnClickListener(AlertListener);


//        arrayList.add(new PromissItem(PromissType.New_Appoint,"5/14","오후 07:00","영등포 맛집 탐방"));
//        arrayList.add(new PromissItem(PromissType.Accept,"5/16","오후 04:00","롯데월드 소풍","구동섭"));
//        arrayList.add(new PromissItem(PromissType.Cancel,"5/19","오후 02:00","잠실야구장 소풍","구동섭"));
//        arrayList.add(new PromissItem(PromissType.Late_Member,"5/24","오후 12:30","부산 여행","구동섭,임수현,양민욱"));
//        arrayList.add(new PromissItem(PromissType.Appoint_START,"6/06","오전 07:00","남산 타워 소풍"));
//        arrayList.add(new PromissItem(PromissType.Follow,"","","","구동섭"));
//        arrayList.add(new PromissItem(PromissType.Time_Late,"5/15","오후 08:00","서울 맛집 탐방","1000"));
//
//        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        connection.GetRequest("api/notification/myNotify/"+ UserInfor.shared.getId_num(),myNotify);
    }

    private Callback GPScallback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            AlertActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AlertActivity.this,"네트워크 문제로 GPS를 갱신 할 수 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();
            Log.d("url",s);
            try{
                JSONObject jsonObject=new JSONObject(s);
            }catch (JSONException e)
            {
                AlertActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AlertActivity.this,"네트워크 문제로 GPS를 갱신 할 수 없습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

        }
    };
    private Callback deleteNotify=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            AlertActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AlertActivity.this,"네트워크 문제로 알람을 삭제할 수 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();
            Log.d("url",s);
            try{
                JSONObject jsonObject=new JSONObject(s);
            }catch (JSONException e)
            {
                AlertActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AlertActivity.this,"네트워크 문제로 알람을 삭제할 수 없습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        }
    };
    private Callback myNotify=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            AlertActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AlertActivity.this,"네트워크 문제로 알람을 불러올 수 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();

            Log.d("url",s);
                try{

                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray notify=jsonObject.getJSONArray("data");

                    for(int i=0;i<notify.length();i++)
                    {
                        JSONObject object=notify.getJSONObject(i);

                        switch (object.getInt("type"))
                        {

                            case 0: //약속 초대
//                                arrayList.add(new PromissItem(PromissType.New_Appoint,"5/14","오후 07:00","영등포 맛집 탐방"));
                                arrayList.add(new PromissItem(PromissType.New_Appoint,object.getInt("id"),object.getInt("send_id"),object.getInt("appointment_id"),object.getString("created_at").substring(5,10),object.getString("date").substring(5,10),
                                        GetTime(object.getString("date_time").substring(0,5)), object.getString("address")));
                                break;
                            case 1:
                                arrayList.add(new PromissItem(PromissType.GPS_ALERT,object.getInt("appointment_id"),object.getInt("id")));
                                break;
                            default:
                                arrayList.add(new PromissItem(PromissType.New_Appoint,object.getInt("id"),object.getInt("send_id"),object.getInt("appointment_id"),object.getString("created_at").substring(5,10),object.getString("date").substring(5,10),
                                        GetTime(object.getString("date_time").substring(0,5)), object.getString("address")));
                        }
                    }
                    AlertActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                        }
                    });
                }catch (JSONException e)
                {
                    AlertActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlertActivity.this,"네트워크 문제로 알람을 불러올 수 없습니다.",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }catch (StringIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
        }
    };
}
