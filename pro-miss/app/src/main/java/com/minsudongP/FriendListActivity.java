package com.minsudongP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FriendListActivity extends AppCompatActivity {

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();
    ArrayList<PromissItem> result=new ArrayList<>();
    ArrayList<String> result_NameList=new ArrayList<>();
    ArrayList<String> result_ImageList=new ArrayList<>();
    ArrayList<Integer> result_IdList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        View.OnClickListener BackButtonListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button)findViewById(R.id.friend_backButton)).setOnClickListener(BackButtonListener);

        RecyclerView recyclerView=findViewById(R.id.friend_recycleview);

        findViewById(R.id.friend_final_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<result.size();i++)
                {
                    result_NameList.add(result.get(i).getName());
                    result_ImageList.add(result.get(i).getProfileImageURl());
                    result_IdList.add(result.get(i).getUser_id());
                }

                Intent intent=getIntent();
                intent.putStringArrayListExtra("name",result_NameList);
                intent.putStringArrayListExtra("Image",result_ImageList);
                intent.putIntegerArrayListExtra("id",result_IdList);
                setResult(1,intent);
                finish();
            }
        });



        adapter=new AllRecyclerAdapter(arrayList,FriendListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {
                PromissItem item=arrayList.get(position);
                if(arrayList.get(position).getIsFollowing()==1)//삭제
                {
                    result.remove(item);
                    arrayList.get(position).setIsFollowing(0);
                }else //추가
                {
                    result.add(item);
                    arrayList.get(position).setIsFollowing(1);
                }
                adapter.notifyDataSetChanged();
            }
        });
        new Thread(){
            @Override
            public void run() {
                UrlConnection.shardUrl.GetRequest("api/followList/"+ UserInfor.shared.getId_num(),callback);
            }
        }.run();
    }

    Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            FriendListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FriendListActivity.this,"네트워크 오류로 목록을 불러오지 못했습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String s=response.body().string();

            try{
                JSONObject jsonObject=new JSONObject(s);
                JSONArray data=jsonObject.getJSONArray("data");

                for(int i=0;i<data.length();i++)
                {
                    jsonObject=data.getJSONObject(i);
                    PromissItem item = new PromissItem(PromissType.MEMBER_ADD,jsonObject.getInt("id"), jsonObject.getString("email"),
                            jsonObject.getString("Image"), jsonObject.getString("name"), 0);
                    arrayList.add(item);
                }
            }catch (JSONException e)
            {
                FriendListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FriendListActivity.this,"서버 오류로 목록을 불러오지 못했습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        }
    };
}
