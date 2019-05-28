package com.minsudongP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyPageActivity extends AppCompatActivity {

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();
    TextView money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        RecyclerView recyclerView=findViewById(R.id.mypage_recycleview);

        final UserInfor infor= UserInfor.shared;

        money=findViewById(R.id.mypage_money_value);
        ((TextView)findViewById(R.id.mypage_name)).setText(infor.getName());
         money.setText(infor.getMoney()+"원");
        if(infor.getAppoint_num()!=0) {
            ((TextView) findViewById(R.id.mypage_rangeNum_text)).setText("" + (infor.getSuccess_appoint_num() / infor.getAppoint_num() * 100) + "%");
            ((TextView) findViewById(R.id.mypage_rangePercent_text)).setText(infor.getSuccess_appoint_num() + "/" + infor.getAppoint_num());
            ((ProgressBar)findViewById(R.id.mypage_range_progress)).setProgress((infor.getSuccess_appoint_num()/infor.getAppoint_num())*100);
        }else
        {
            ((TextView) findViewById(R.id.mypage_rangeNum_text)).setText("0%");
            ((TextView) findViewById(R.id.mypage_rangePercent_text)).setText("0/0");
            ((ProgressBar)findViewById(R.id.mypage_range_progress)).setProgress(0);
        }

        findViewById(R.id.mypage_money_addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UrlConnection urlConnection = UrlConnection.shardUrl;

                final HashMap<String, String> hash = new HashMap<>();
                hash.put("id", infor.getId_num());
                hash.put("money", "300");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        urlConnection.PostRequest("api/money/add", callback, hash);
                    }
                }.run();
            }
        });


        View.OnClickListener AttendingListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyPageActivity.this,AttendingActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener SettingsListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyPageActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener PastListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyPageActivity.this,PastActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener MainListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button)findViewById(R.id.mypage_backButton)).setOnClickListener(MainListener);


        adapter=new AllRecyclerAdapter(arrayList,MyPageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ((Button)findViewById(R.id.mypage_attendingButton)).setOnClickListener(AttendingListener);
        ((Button)findViewById(R.id.mypage_setButton)).setOnClickListener(SettingsListener);
        ((Button)findViewById(R.id.mypage_pastButton)).setOnClickListener(PastListener);


        arrayList.add(new PromissItem(PromissType.FriendLIst,1,"urltest","양민욱"));

        arrayList.add(new PromissItem(PromissType.FriendLIst,2,"urltest","구동섭"));

        arrayList.add(new PromissItem(PromissType.FriendLIst,3,"urltest","김종인"));

        adapter.notifyDataSetChanged();
    }


    private Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            MyPageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyPageActivity.this,"네트워크를 확인해주세요.",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();

            try {
                final JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.getInt("result")==1000)
                {
                    MyPageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(MyPageActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else
                {
                    final JSONObject object=jsonObject.getJSONObject("data");
                    MyPageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                money.setText(object.getString("money")+"원");
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
}
