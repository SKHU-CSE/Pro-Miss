
package com.minsudongP;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;

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


//        arrayList.add(new PromissItem(PromissType.New_Appoint,"5/14","오후 07:00","영등포 맛집 탐방"));
//        arrayList.add(new PromissItem(PromissType.Accept,"5/16","오후 04:00","롯데월드 소풍","구동섭"));
//        arrayList.add(new PromissItem(PromissType.Cancel,"5/19","오후 02:00","잠실야구장 소풍","구동섭"));
//        arrayList.add(new PromissItem(PromissType.Late_Member,"5/24","오후 12:30","부산 여행","구동섭,임수현,양민욱"));
//        arrayList.add(new PromissItem(PromissType.Appoint_START,"6/06","오전 07:00","남산 타워 소풍"));
//        arrayList.add(new PromissItem(PromissType.Follow,"","","","구동섭"));
//        arrayList.add(new PromissItem(PromissType.Time_Late,"5/15","오후 08:00","서울 맛집 탐방","1000"));
//
//        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(AlertActivity.this));
        recyclerView.setAdapter(adapter);
    }
}
