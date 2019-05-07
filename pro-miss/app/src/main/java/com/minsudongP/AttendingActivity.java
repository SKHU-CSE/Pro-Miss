package com.minsudongP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AttendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending);

        View.OnClickListener MYPageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        View.OnClickListener CardViewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AttendingActivity.this,AttendingDetailActivity.class);
                startActivity(intent);
                finish();
            }
        };
        ((Button) findViewById(R.id.attending_backButton)).setOnClickListener(MYPageListener);
        ((View)findViewById(R.id.attending_cardview)).setOnClickListener(CardViewClickListener);
    }
}
