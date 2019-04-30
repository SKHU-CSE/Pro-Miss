package com.minsudongP.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.minsudongP.DialogSelectTimer;
import com.minsudongP.LoginActivity;
import com.minsudongP.MainActivity;
import com.minsudongP.R;
import com.minsudongP.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AppointmentFragemnt extends Fragment {
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_1, null);


        final TextView tvDate = view.findViewById(R.id.frg_appoint1_card1_DateText);
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
        final TextView tvTimer = view.findViewById(R.id.frg_appoint1_timer_t2);
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


}
