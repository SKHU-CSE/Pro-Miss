package com.minsudongP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.minsudongP.Fragment.AppointmentFragemnt;
import com.minsudongP.Fragment.MemberFragment;
import com.minsudongP.Fragment.SetMoneyFragemnt;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.ViewpagerAdapter;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class appointment extends AppCompatActivity {

    AppointmentFragemnt ap_fragment;
    SetMoneyFragemnt  sd_fragment;
    MemberFragment mb_fragment;
    ViewpagerAdapter viewpagerAdapter;

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
        this.notice="";
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
//        viewpagerAdapter.clear();
        Log.d("memberSize",""+member.size());
        member.remove(0);
        member.remove(member.size()-1);
        final UrlConnection connection=UrlConnection.shardUrl;
        final HashMap<String,String> hash=new HashMap<>();

        hash.put("id", UserInfor.shared.getId_num());
        hash.put("latitude",latitude);
        hash.put("longitude",longitude);
        hash.put("address",address);
        hash.put("date",date);
        hash.put("date_time",date_time);
        hash.put("Timer",Timer);
        hash.put("num",""+member.size());
        hash.put("Fine_time",Fine_time);
        hash.put("Fine_money",Fine_money);
        hash.put("notice",notice);

        for(int i=0;i<member.size();i++)
        {
            Log.d("waitting","ok");
            hash.put("member_id"+i,""+member.get(i).getUser_id());
        }

        new Thread(){
            @Override
            public void run() {
                connection.PostRequest("api/appointment/up",callback,hash);
            }
        }.run();
    }



    private Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            appointment.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appointment.this, "네트워크에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();

            Log.d("url",s);
            try{
                final JSONObject object=new JSONObject(s);

                if(object.getInt("result")==2000)
                {
                    appointment.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(appointment.this,object.getString("data"),Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    finish();
                }else
                {
                    appointment.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(appointment.this,object.getString("message"),Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewpagerAdapter);

        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        viewpagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        ap_fragment=new AppointmentFragemnt();
        sd_fragment=new SetMoneyFragemnt();
        mb_fragment=new MemberFragment();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                try {
                    switch (i - 1) {
                        case 0:
                            ((AppointmentFragemnt) viewpagerAdapter.getItem(0)).SendDatatoActivity();
                            break;
                        case 1:
                            ((SetMoneyFragemnt) viewpagerAdapter.getItem(1)).SendDatatoActivity();
                            break;
                    }
                }catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }
        });

        viewpagerAdapter.addItem(ap_fragment);
        viewpagerAdapter.addItem(sd_fragment);
        viewpagerAdapter.addItem(mb_fragment);

        viewpagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("취소하고 메인페이지로 돌아갑니다.");

        alert_ex.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_ex.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert_ex.setTitle("작성을 취소하시겠습니까?");
        AlertDialog alert = alert_ex.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("appoint","actResume");
    }


}
