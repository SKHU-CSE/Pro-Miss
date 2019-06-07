package com.minsudongP;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;

import java.util.ArrayList;

public class AttendingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending);

        recyclerView=findViewById(R.id.attend_recycle);
        adapter=new AllRecyclerAdapter(arrayList,AttendingActivity.this);

        arrayList.add(new PromissItem(PromissType.Attend_Appoint,"6/6","오후 06:00","놀러 가기"));
        arrayList.add(new PromissItem(PromissType.Attend_Appoint,"6/28","오후 03:00","롯데월드 소풍"));

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(AttendingActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent=new Intent(AttendingActivity.this,AttendingDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });



        View.OnClickListener BackListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        ((Button) findViewById(R.id.attending_backButton)).setOnClickListener(BackListener);

    }
}
