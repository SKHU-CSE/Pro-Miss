package com.minsudongP;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();

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

        adapter=new AllRecyclerAdapter(arrayList,FriendListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
//
//        arrayList.add(new PromissItem(PromissType.FriendLIst,"urltest","양민욱"));
//        arrayList.add(new PromissItem(PromissType.FriendLIst,"urltest","구동섭"));
//        arrayList.add(new PromissItem(PromissType.FriendLIst,"urltest","김종인"));


        adapter.notifyDataSetChanged();
    }
}
