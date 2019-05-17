package com.minsudongP;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.minsudongP.Fragment.AppointmentFragemnt;
import com.minsudongP.Fragment.MemberFragment;
import com.minsudongP.Fragment.SetMoneyFragemnt;

public class appointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewpagerAdapter);

        viewpagerAdapter.addItem(new AppointmentFragemnt());
        viewpagerAdapter.addItem(new SetMoneyFragemnt());
        viewpagerAdapter.addItem(new MemberFragment());

        viewpagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("appoint","actResume");
    }
}
