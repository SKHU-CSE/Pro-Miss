package com.minsudongP.Fragment;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import com.minsudongP.DialogSelectTimer;
import com.minsudongP.R;

import com.minsudongP.SetDestinyActivity;
import com.minsudongP.Model.appointment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.minsudongP.SetDestinyActivity.result_code;


public class AppointmentFragemnt extends Fragment implements OnMapReadyCallback {

    long mNow;
    Date mDate;
    TextView tvDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    static final int request_code=1;
    public String latitude;
    public String longitude;
    NaverMap naverMap;
    TextView tvTimer;
    String date_time="00:00";
    EditText text;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_1, null);

        // 지도 객체 받아오기
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getChildFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        text=view.findViewById(R.id.atd_detail_title);

        tvDate = view.findViewById(R.id.frg_appoint1_card1_DateText);
        tvDate.setText(getTime());
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {


                        tvDate.setText(String.format("%02d-%02d-%02d", year, month + 1, date));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                //dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        final TextView tvTime = view.findViewById(R.id.frg_appoint1_card1_TimeText);
        tvTime.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                final Calendar cal = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        date_time=hour+":"+min;
                        String ampm = "AM";

                        if (hour >= 12) {
                            ampm = "PM";
                            if (hour != 12) hour -= 12;
                        }

                        tvTime.setText(String.format("%s %02d : %02d", ampm, hour, min));
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);//마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                dialog.show();

            }

        });
        tvTimer = view.findViewById(R.id.atd_detail_timer_t2);
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectTimer dialogSelectTimer = new DialogSelectTimer(getActivity());

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                dialogSelectTimer.callFunction(tvTimer);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        View.OnClickListener AppointmentListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(getContext());
                alert_ex.setMessage("취소하고 메인페이지로 돌아갑니다.");

                alert_ex.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert_ex.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                alert_ex.setTitle("작성을 취소하시겠습니까?");
                AlertDialog alert = alert_ex.create();
                alert.show();

            }
        };
        ((Button) getActivity().findViewById(R.id.appointment_backButton)).setOnClickListener(AppointmentListener);

    }


    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap=naverMap;
        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
                startActivityForResult(intent,request_code);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==request_code)
        {
            if(resultCode==result_code)
            {
                LatLng latLng=new LatLng(data.getDoubleExtra("latitude",37.5662952),data.getDoubleExtra("longitude",126.97794509999994));
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
                naverMap.setCameraPosition(new CameraPosition(latLng,17));

            }
        }
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }





    public int getTimer()
    {
        String s=tvTimer.getText().toString();

        s=s.replaceAll(" ","");
        s=s.replaceAll("분","");
        s=s.trim();
        String str[]=s.split("시간");


        int time=0;
        time += Integer.parseInt(str[0])*60;
        if(str.length>1)
            time += Integer.parseInt(str[1]);

        return time;
    }


    public void SendDatatoActivity()
    {
        ((appointment) getActivity()).setAppointment_role_1(text.getText().toString(), "" + naverMap.getCameraPosition().target.latitude, "" + naverMap.getCameraPosition().target.longitude
                ,""+getTimer(),tvDate.getText().toString(),date_time);
    }

}
