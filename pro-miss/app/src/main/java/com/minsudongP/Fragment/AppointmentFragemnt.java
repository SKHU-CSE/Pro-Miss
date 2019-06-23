package com.minsudongP.Fragment;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.minsudongP.DialogSelectTimer;
import com.minsudongP.R;

import com.minsudongP.SetDestinyActivity;
import com.minsudongP.appointment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.minsudongP.SetDestinyActivity.result_code;


public class AppointmentFragemnt extends Fragment implements OnMapReadyCallback {

    static final int request_code = 1;

    long mNow;
    Date mDate;
    EditText tvName;
    TextView tvDate;
    TextView tvTime;
    TextView tvTimer;
    TextView tvAddress;
    Marker marker;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    String date_time = "00:00";
    EditText notice;


    NaverMap naverMap;

    int m_year = -1;
    int m_month = -1;
    int m_date = -1;
    int m_hour = -1;
    int m_min = -1;
    int m_timer = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_1, null);

        // 지도 터치 방지를 위해 생성됨
        (view.findViewById(R.id.upperMapView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
                startActivityForResult(intent, request_code);
            }
        });
        // 지도 객체 받아오기
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getChildFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        (view.findViewById(R.id.map)).setClickable(false);
        (view.findViewById(R.id.map)).setFocusable(false);
        (view.findViewById(R.id.map)).setEnabled(false);


        notice=view.findViewById(R.id.appointment_memo);

        tvName = view.findViewById(R.id.appointment_name);


        final TextView upperDate = view.findViewById(R.id.upper_date);
        final TextView upperTime = view.findViewById(R.id.upper_time);
        final TextView upperTimer = view.findViewById(R.id.upper_timer);
        final TextView plusTime = view.findViewById(R.id.appointment_plustime);

        // 날짜
        final Calendar cal = Calendar.getInstance();
        tvDate = view.findViewById(R.id.appointment_date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        if (m_year!=year || m_month!=month || m_date!=date) {
                            tvDate.setText(String.format("%02d-%02d-%02d", year, month + 1, date));
                            tvDate.setTextColor(Color.BLACK);
                            upperDate.setVisibility(View.VISIBLE);
                            m_year = year;
                            m_month = month;
                            m_date = date;

                            // 시간, 타이머 초기화
                            tvTime.setText("시간");
                            tvTime.setTextColor(getResources().getColor(R.color.colorAccent));
                            upperTime.setVisibility(View.INVISIBLE);
                            tvTimer.setText("약속장소로 언제 출발하시나요?");
                            tvTimer.setTextColor(getResources().getColor(R.color.colorAccent));
                            upperTimer.setVisibility(View.INVISIBLE);
                            plusTime.setText("");
                            m_hour = -1;
                            m_min = -1;
                            m_timer = -1;
                            Toast.makeText(getContext(),m_year+"년 "+(m_month+1)+"월 "+m_date+"일\n"+m_hour+"시 "+m_date+"",Toast.LENGTH_LONG).show();
                        }

                        Log.d("현재날짜", cal.get(Calendar.YEAR) + "-" +(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE));
                        Log.d("약속날짜", m_year + "-" +(m_month+1)+"-"+m_date);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMinDate(cal.getTime().getTime());    //현재 날짜 이전으로 클릭 안되게 옵션 설정
                dialog.show();
            }
        });


        // 시간 설정
        tvTime = view.findViewById(R.id.appointment_time);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                // 날짜 선택 먼저 하도록 유도
                if (m_year==-1 && m_month==-1 && m_date==-1) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("날짜 미설정");
                    alert.setMessage("약속 날짜를 먼저 선택해 주세요.");
                    alert.setPositiveButton("확인", null);
                    alert.create().show();
                    return;
                }

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                        int currentMin = cal.get(Calendar.MINUTE);

                        Log.d("현재시간", currentHour + ":" +currentMin);
                        Log.d("약속시간", hour + ":" +min);

                        // 현재시간으로부터 1시간 이후에만 약속 생성
                        if(getGapwithNow(m_year,m_month,m_date,hour,min)<1){
                            AlertDialog.Builder alert_time = new AlertDialog.Builder(getContext());
                            alert_time.setTitle("시간설정 오류");
                            alert_time.setMessage("현재 시간으로부터 1시간 이후의 약속만 생성 가능합니다.");
                            alert_time.setPositiveButton("확인", null);
                            alert_time.create().show();
                            return;
                        }

                        // 제약사항에 걸리지 않으면 바로 생성
                        date_time = hour + ":" + min;
                        String ampm = "AM";
                        if (hour >= 12) {
                            ampm = "PM";
                            if (hour != 12) hour -= 12;
                        }

                        if (m_hour!=hour || m_min != min){
                            tvTime.setText(String.format("%s %02d : %02d", ampm, hour, min));
                            tvTime.setTextColor(Color.BLACK);
                            upperTime.setVisibility(View.VISIBLE);
                            m_hour = hour;
                            if(ampm.equals("PM")) m_hour+=12;
                            m_min = min;

                            tvTimer.setText("약속장소로 언제 출발하시나요?");
                            tvTimer.setTextColor(getResources().getColor(R.color.colorAccent));
                            upperTimer.setVisibility(View.INVISIBLE);
                            plusTime.setText("");
                            m_timer=-1;
                        }
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);//마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                dialog.show();
            }
        });

        // 장소 설정
        tvAddress = view.findViewById(R.id.appointment_address);
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
                startActivityForResult(intent, request_code);
            }
        });

        // 타이머 설정
        tvTimer = view.findViewById(R.id.appointment_timer);
        View.OnClickListener timerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_hour==-1 || m_min==-1) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("시간 미설정");
                    alert.setMessage("약속 시간을 먼저 선택해 주세요.");
                    alert.setPositiveButton("확인", null);
                    alert.create().show();
                } else {
                    Log.d("gap",String.valueOf(getGapwithNow(m_year,m_month,m_date,m_hour,m_min)));
                    DialogSelectTimer dialogSelectTimer = new DialogSelectTimer(getActivity());
                    dialogSelectTimer.callFunction(tvTimer, upperTimer, plusTime,getGapwithNow(m_year,m_month,m_date,m_hour,m_min));
                }
            }
        };

        tvTimer.setOnClickListener(timerListener);
        plusTime.setOnClickListener(timerListener);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setMaxZoom(14.0);
        naverMap.setMinZoom(14.0);

        marker = new Marker();
//        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
//                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
//                startActivityForResult(intent,request_code);
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code) {
            if (resultCode == result_code) {
                LatLng latLng = new LatLng(data.getDoubleExtra("latitude", 37.5662952), data.getDoubleExtra("longitude", 126.97794509999994));
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
                naverMap.setCameraPosition(new CameraPosition(latLng, 17));
                marker.setPosition(latLng);
                marker.setMap(naverMap);

                String address = data.getStringExtra("address");
                if (!address.equals("")) {
                    tvAddress.setText(address);
                    tvAddress.setTextColor(Color.BLACK);
                    ((TextView) getView().findViewById(R.id.upper_address)).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }


    public int getTimer() {
        String s = tvTimer.getText().toString();

        if (tvTimer.getText().equals("약속장소로 언제 출발하시나요?")) {
            return -1;
        } else {
            s = s.replaceAll(" ", "");
            s = s.replaceAll("분", "");
            s = s.trim();
            if (s.contains("시간")) {
                String str[] = s.split("시간");


                int time = 0;
                time += Integer.parseInt(str[0]) * 60;
                if (str.length > 1)
                    time += Integer.parseInt(str[1]);

                return time;
            } else
                return Integer.parseInt(s.trim());
        }
    }


    public void SendDatatoActivity() {
        ((appointment) getActivity()).setAppointment_role_1(tvName.getText().toString(), "" + naverMap.getCameraPosition().target.latitude, "" + naverMap.getCameraPosition().target.longitude
                , "" + getTimer(), tvDate.getText().toString(), date_time,notice.getText().toString());

    }

    public long getGapwithNow(int year, int month, int date, int hour, int min ){
        Calendar currentTime = Calendar.getInstance();

        try {
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd kk:mm");

            String curStr = String.format("%04d.%02d.%02d %02d:%02d", currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DATE),currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE));
            String objStr = String.format("%04d.%02d.%02d %02d:%02d", year,month,date,hour,min);

            Date curDate = dataFormat.parse(curStr);
            Date objDate = dataFormat.parse(objStr);


            long duration = objDate.getTime() - curDate.getTime();
            String tag = objDate.getTime()+"와 "+curDate.getTime()+"의 차이";
            Log.d(tag, String.valueOf(duration/60000));
            return duration/60000;
        }catch (ParseException e){

        }

        return -1;
    }

}
