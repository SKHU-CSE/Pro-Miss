package com.minsudongP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // OnClick

        View.OnClickListener MakeProtocalListenr=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,appointment.class);
                startActivity(intent);
                //되돌아 올수 있어 finish()를 하지 않음
            }
        };

        View.OnClickListener MyPageListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MyPageActivity.class);
                startActivity(intent);
            }
        };


        ((ImageButton)findViewById(R.id.main_MakeProtocal)).setOnClickListener(MakeProtocalListenr);
        ((TextView)findViewById(R.id.main_MakeProtocal_text)).setOnClickListener(MakeProtocalListenr);
        ((LinearLayout)findViewById(R.id.main_MyPage)).setOnClickListener(MyPageListener);
        ((TextView)findViewById(R.id.main_MyPage_text)).setOnClickListener(MyPageListener);

    }
}
