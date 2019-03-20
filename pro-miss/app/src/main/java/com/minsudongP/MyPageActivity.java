package com.minsudongP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);


        View.OnClickListener AttendingListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyPageActivity.this,AttendingActivity.class);
                startActivity(intent);
                finish();
            }
        };

        ((Button)findViewById(R.id.mypage_attendingButton)).setOnClickListener(AttendingListener);
    }
}
