
package com.minsudongP;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        recyclerView=findViewById(R.id.alert_recycle);
        adapter=new AllRecyclerAdapter(arrayList,AlertActivity.this);
        View.OnClickListener AlertListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button)findViewById(R.id.alert_backButton)).setOnClickListener(AlertListener);


        arrayList.add(new PromissItem(PromissType.New_Appoint,"5/14","오후 07:00","영등포 맛집 탐방"));

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(AlertActivity.this));
        recyclerView.setAdapter(adapter);
    }
}
