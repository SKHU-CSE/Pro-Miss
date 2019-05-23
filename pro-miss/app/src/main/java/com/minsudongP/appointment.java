package com.minsudongP;

import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

public class appointment extends AppCompatActivity {

    AppointmentFragemnt ap_fragment;
    SetMoneyFragemnt  sd_fragment;
    MemberFragment mb_fragment;


    String address;
    String latitude;
    String longitude;
    String date;
    String date_time;
    String Timer;
    String Fine_time;
    String Fine_money;
    String notice;
    ArrayList<PromissItem> member;




    /*첫번째 페이지에서 설정*/
    public void setAppointment_role_1(String address,String latitude,String longitude,String Timer,String date,String date_time)
    {
        this.address=address;
        this.latitude=latitude;
        this.longitude=longitude;
        this.Timer=Timer;
        this.date=date;
        this.date_time=date_time;
    }

    public void setAppointment_role_2(String Fine_time,String Fine_money)
    {
        this.Fine_money=Fine_money;
        this.Fine_time=Fine_time;
    }

    public void setAppointment_role_3(ArrayList<PromissItem> items)
    {
        this.member=items;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewpagerAdapter);

        ap_fragment=new AppointmentFragemnt();
        sd_fragment=new SetMoneyFragemnt();
        mb_fragment=new MemberFragment();
        viewpagerAdapter.addItem(ap_fragment);
        viewpagerAdapter.addItem(sd_fragment);
        viewpagerAdapter.addItem(mb_fragment);

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
