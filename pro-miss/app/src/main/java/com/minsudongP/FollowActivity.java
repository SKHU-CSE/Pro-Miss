package com.minsudongP;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Singletone.UrlConnection;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {

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
                finish();
            }
        });

        // 팔로우 목록 어뎁터
        adapter = new AllRecyclerAdapter(arrayList, FollowActivity.this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 팔로우 임시 목록
        arrayList.add(new PromissItem(PromissType.FriendLIst, 1, "urltest", "양민욱"));
        arrayList.add(new PromissItem(PromissType.FriendLIst, 2, "urltest", "구동섭"));
        arrayList.add(new PromissItem(PromissType.FriendLIst, 3, "urltest", "김종인"));
        adapter.notifyDataSetChanged();
    }


}
