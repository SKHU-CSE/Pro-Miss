package com.minsudongP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        RecyclerView recyclerView=findViewById(R.id.mypage_recycleview);

        View.OnClickListener AttendingListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyPageActivity.this,AttendingActivity.class);
                startActivity(intent);
                finish();
            }
        };

        adapter=new AllRecyclerAdapter(arrayList,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ((Button)findViewById(R.id.mypage_attendingButton)).setOnClickListener(AttendingListener);


        arrayList.add(new PromissItem(PromissType.FriendLIst,"urltest","양민욱"));

        arrayList.add(new PromissItem(PromissType.FriendLIst,"urltest","구동섭"));

        arrayList.add(new PromissItem(PromissType.FriendLIst,"urltest","김종인"));

        adapter.notifyDataSetChanged();
    }
}
