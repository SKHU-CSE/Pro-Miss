package com.minsudongP;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyPageActivity extends AppCompatActivity {

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList = new ArrayList<>();

    // 금액 충전
    TextView money;
    final int MYPAGE_TO_CHARGE = 3000;
    final UserInfor infor = UserInfor.shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        RecyclerView recyclerView = findViewById(R.id.mypage_recycleview);

        // user info 불러오기
        ((TextView) findViewById(R.id.mypage_name)).setText(infor.getName());

        // 팔로우 목록 불러오기

        final UrlConnection urlConnection = UrlConnection.shardUrl;
        new Thread() {
            @Override
            public void run() {
                super.run();
                urlConnection.GetRequest("api/followList/"+infor.getId_num(), followingCallback );
            }
        }.run();

        // 약속 달성률 계산
        if (infor.getAppoint_num() != 0) {
            ((TextView) findViewById(R.id.mypage_rangeNum_text)).setText("" + (infor.getSuccess_appoint_num() / infor.getAppoint_num() * 100) + "%");
            ((TextView) findViewById(R.id.mypage_rangePercent_text)).setText(infor.getSuccess_appoint_num() + "/" + infor.getAppoint_num());
            ((ProgressBar) findViewById(R.id.mypage_range_progress)).setProgress((infor.getSuccess_appoint_num() / infor.getAppoint_num()) * 100);
        } else {
            ((TextView) findViewById(R.id.mypage_rangeNum_text)).setText("0%");
            ((TextView) findViewById(R.id.mypage_rangePercent_text)).setText("0/0");
            ((ProgressBar) findViewById(R.id.mypage_range_progress)).setProgress(0);
        }

        // 약속 달성률 버튼 클릭 시 -> 지난 약속 목록으로 이동
        findViewById(R.id.mypage_range_cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, PastActivity.class);
                startActivity(intent);
            }
        });

        // 금액 불러오기
        money = findViewById(R.id.mypage_money_value);
        money.setText(infor.getMoney() + "원");

        // 금액 충전 액티비티로 이동
        findViewById(R.id.mypage_money_addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, ChargeActivity.class);
                startActivityForResult(intent, MYPAGE_TO_CHARGE);
            }
        });

        // 참여중인 약속
        View.OnClickListener AttendingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, AttendingActivity.class);
                startActivity(intent);
            }
        };

        // 설정
        View.OnClickListener SettingsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        };

        // 지난 약속
        View.OnClickListener PastListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, PastActivity.class);
                startActivity(intent);
            }
        };

        // 팔로우하기
        View.OnClickListener FollowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, FollowActivity.class);
                startActivity(intent);
            }
        };

        // 돌아가기(main)
        View.OnClickListener MainListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        ((Button) findViewById(R.id.mypage_backButton)).setOnClickListener(MainListener);
        ((Button) findViewById(R.id.mypage_attendingButton)).setOnClickListener(AttendingListener);
        ((Button) findViewById(R.id.mypage_setButton)).setOnClickListener(SettingsListener);
        ((Button) findViewById(R.id.mypage_pastButton)).setOnClickListener(PastListener);
        ((Button) findViewById(R.id.mypage_addFollow)).setOnClickListener(FollowListener);

        // 팔로우 목록 어뎁터
        adapter = new AllRecyclerAdapter(arrayList, MyPageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 팔로우 임시 목록
//        arrayList.add(new PromissItem(PromissType.FriendLIst, 1, "urltest", "양민욱"));
//        arrayList.add(new PromissItem(PromissType.FriendLIst, 2, "urltest", "구동섭"));
//        arrayList.add(new PromissItem(PromissType.FriendLIst, 3, "urltest", "김종인"));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MYPAGE_TO_CHARGE:

                    final UrlConnection urlConnection = UrlConnection.shardUrl;
                    final HashMap<String, String> hash = new HashMap<>();
                    hash.put("id", infor.getId_num());
                    hash.put("money", data.getStringExtra("money"));
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            urlConnection.PostRequest("api/money/add", moneyCallback, hash);
                        }
                    }.run();
            }
        }
    }

    private Callback moneyCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            MyPageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyPageActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("result") == 1000) {
                    MyPageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(MyPageActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    final JSONObject object = jsonObject.getJSONObject("data");
                    MyPageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(MyPageActivity.this, (Integer.parseInt(object.getString("money")) - infor.getMoney()) + "원이 충전되었습니다.", Toast.LENGTH_LONG).show();
                                money.setText(object.getString("money") + "원");
                                infor.setMoney(Integer.parseInt(object.getString("money")));
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

                        PromissItem item = new PromissItem(PromissType.FriendLIst, id, image, name);
                        arrayList.add(item);
                    }

                    MyPageActivity.this.runOnUiThread(new Runnable() {
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
}
