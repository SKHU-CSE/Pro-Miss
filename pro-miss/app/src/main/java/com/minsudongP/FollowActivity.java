package com.minsudongP;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
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

public class FollowActivity extends AppCompatActivity {

    final UserInfor infor = UserInfor.shared;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        RecyclerView recyclerView = findViewById(R.id.follow_recycler);

        ((Button) findViewById(R.id.follow_backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });

        // 팔로우 목록 어뎁터
        adapter = new AllRecyclerAdapter(arrayList, FollowActivity.this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 팔로우 목록 불러오기
        final UrlConnection urlConnection = UrlConnection.shardUrl;
        new Thread() {
            @Override
            public void run() {
                super.run();
                urlConnection.GetRequest("api/allUserList/" + infor.getId_num(), userListCallback);
            }
        }.run();

        adapter.notifyDataSetChanged();

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {
                Button btn = (Button) view;
                PromissItem item = arrayList.get(position);

                final HashMap<String, String> hash = new HashMap<>();
                hash.put("follower", infor.getId_num());
                hash.put("following", String.valueOf(item.getUser_id()));

                if (item.getIsFollowing() == 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            urlConnection.PostRequest("api/follow", followCallback, hash);
                        }
                    }.run();

                    item.setIsFollowing(1);

                    FollowActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            urlConnection.PostRequest("api/unFollow", unfollowCallback, hash);

                        }
                    }.run();

                    item.setIsFollowing(0);

                    FollowActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private final Callback userListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

            FollowActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FollowActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
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

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject followerObj = data.getJSONObject(i);
                        Log.d("followerObj", followerObj.toString());

                        int id = followerObj.getInt("id");
                        String email = followerObj.getString("email");
                        String image = followerObj.getString("Image");
                        String name = followerObj.getString("name");
                        int isFollowing = followerObj.getInt("isFollowing");

                        PromissItem item = new PromissItem(PromissType.UserList, id, email, image, name, isFollowing);
                        arrayList.add(item);
                    }

                    FollowActivity.this.runOnUiThread(new Runnable() {
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

    private final Callback followCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            FollowActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FollowActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("result") == 2000) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    Log.d("follow data",data.toString());
                        FollowActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FollowActivity.this, "팔로우 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private final Callback unfollowCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            FollowActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FollowActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("result") == 2000 && jsonObject.getInt("data")>0) {
                    FollowActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "언팔로우 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}