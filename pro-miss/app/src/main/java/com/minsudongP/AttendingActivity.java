package com.minsudongP;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AttendingActivity extends AppCompatActivity {
    AnimatedRecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();


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
        setContentView(R.layout.activity_attending);

        recyclerView=(AnimatedRecyclerView)findViewById(R.id.attend_recycle);
        adapter=new AllRecyclerAdapter(arrayList,AttendingActivity.this);
//
//        arrayList.add(new PromissItem(PromissType.Attend_Appoint,"6/6","오후 06:00","놀러 가기"));
//        arrayList.add(new PromissItem(PromissType.Attend_Appoint,"6/28","오후 03:00","롯데월드 소풍"));

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(AttendingActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {

                    Intent intent = new Intent(AttendingActivity.this, AttendingDetailActivity.class);
                    intent.putExtra("status",arrayList.get(position).getAppointment_status());
                    intent.putExtra("id",arrayList.get(position).getAppointment_id());
                    startActivity(intent);

            }
        });



        View.OnClickListener BackListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button) findViewById(R.id.attending_backButton)).setOnClickListener(BackListener);

        new Thread(){
            @Override
            public void run() {
                UrlConnection.shardUrl.GetRequest("api/appointment/get/"+ UserInfor.shared.getId_num(),myAppointment);
            }
        }.run();
    }
    private Callback myAppointment=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            AttendingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AttendingActivity.this,"네트워크 문제로 약속을 불러올 수 없습니다.",Toast.LENGTH_LONG).show();
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


//                    arrayList.add(new PromissItem(PromissType.Attend_Appoint,"6/28","오후 03:00","롯데월드 소풍"));
                    arrayList.add(new PromissItem(PromissType.Attend_Appoint,object.getInt("id"),object.getString("date"),GetTime(object.getString("date_time").substring(0,5)),object.getString("address"),object.getInt("status")));

                }
                AttendingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    }
                });
            }catch (JSONException e)
            {
                AttendingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AttendingActivity.this,"네트워크 문제로 약속속을 불러올 수 없습니다.",Toast.LENGTH_LONG).show();
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
