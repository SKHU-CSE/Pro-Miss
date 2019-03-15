package com.minsudongP;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.minsudongP.Fragment.AppointmentFragemnt;
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

        viewpagerAdapter.notifyDataSetChanged();
    }
}
