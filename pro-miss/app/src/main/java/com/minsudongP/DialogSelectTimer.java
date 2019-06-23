package com.minsudongP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.Fragment.AppointmentFragemnt;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogSelectTimer {
    private Context context;
    TextView tvApDate;
    TextView tvApTime;
    TextView tvTmDate;
    TextView tvTmTime;
    TextView tvPlusTime;

    public DialogSelectTimer(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView timer_label, final TextView upper_label, final TextView plus_label, String date, String time) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_select_timer);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btnMinusHour = (Button) dlg.findViewById(R.id.dialog_btnMinusHour);
        final Button btnPlusHour = (Button) dlg.findViewById(R.id.dialog_btnPlusHour);
        final Button btnMinusMin = (Button) dlg.findViewById(R.id.dialog_btnMinusMin);
        final Button btnPlusMin = (Button) dlg.findViewById(R.id.dialog_btnPlusMin);
        final TextView tvHour = (TextView) dlg.findViewById(R.id.dialog_tvHour);
        final TextView tvMin = (TextView) dlg.findViewById(R.id.dialog_tvMin);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_okButton);
        tvApDate = (TextView) dlg.findViewById(R.id.dialog_tvApDate);
        tvApTime = (TextView) dlg.findViewById(R.id.dialog_tvApTime);
        tvTmDate = (TextView) dlg.findViewById(R.id.dialog_tvTmDate);
        tvTmTime = (TextView) dlg.findViewById(R.id.dialog_tvTmTime);
        tvPlusTime = (TextView) dlg.findViewById(R.id.dialog_tvPlusTime);
        tvApDate.setText(date);
        tvApTime.setText(time);
        tvPlusTime.setText(" + "+getPlusTime(30)+" 분");
        getStartTime(30);



        btnMinusHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(tvHour.getText().toString());
                int min = Integer.parseInt(tvMin.getText().toString());
                if (min <= 0 && hour <= 1) {
                    Toast.makeText(context, "0분으로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (hour > 0) {
                    if (getStartTime((hour - 1) * 60 + Integer.parseInt(tvMin.getText().toString()))) {
                        tvHour.setText("" + (hour - 1));

                        tvPlusTime.setText(" + " + getPlusTime((hour - 1) * 60 + Integer.parseInt(tvMin.getText().toString())) + " 분");
                    }
                }
            }
        });
        btnPlusHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(tvHour.getText().toString());
                int min = Integer.parseInt(tvMin.getText().toString());
                if (hour >= 5) {
                    Toast.makeText(context, "6시간 이상으로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (getStartTime((hour + 1) * 60 + Integer.parseInt(tvMin.getText().toString()))) {
                        tvHour.setText("" + (hour + 1));
                        tvPlusTime.setText(" + " + getPlusTime((hour + 1) * 60 + Integer.parseInt(tvMin.getText().toString())) + " 분");
                    }
                }
            }
        });
        btnMinusMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(tvHour.getText().toString());
                int min = Integer.parseInt(tvMin.getText().toString());
                if (min <= 5 && hour <= 0) {
                    Toast.makeText(context, "0분으로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (min > 0) {
                    if (getStartTime(Integer.parseInt(tvHour.getText().toString()) * 60 + (min - 5))) {
                        tvMin.setText("" + (min - 5));
                        tvPlusTime.setText(" + " + getPlusTime(Integer.parseInt(tvHour.getText().toString()) * 60 + (min - 5)) + " 분");
                    }
                } else if (min <= 0 && hour > 0) {

                    if (getStartTime((hour - 1) * 60 + (min - 5))) {
                        tvHour.setText("" + (hour - 1));
                        tvMin.setText("55");
                        tvPlusTime.setText(" + " + getPlusTime((hour - 1) * 60 + (min - 5)) + " 분");
                    }
                }
            }
        });
        btnPlusMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(tvHour.getText().toString());
                int min = Integer.parseInt(tvMin.getText().toString());
                if (min >= 55) {
                    if (hour >= 5)
                        Toast.makeText(context, "6시간 이상으로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    else {
                        if (getStartTime((hour + 1) * 60)) {
                            tvHour.setText("" + (hour + 1));
                            tvMin.setText("0");
                            tvPlusTime.setText(" + " + getPlusTime((hour + 1) * 60) + " 분");
                        }
                    }
                } else {
                    if (getStartTime(Integer.parseInt(tvHour.getText().toString()) * 60 + (min + 5))) {
                        tvMin.setText("" + (min + 5));
                        tvPlusTime.setText(" + " + getPlusTime(Integer.parseInt(tvHour.getText().toString()) * 60 + (min + 5)) + " 분");
                    }
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 timer_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                String time = "";
                String strHour = tvHour.getText().toString();
                String strMin = tvMin.getText().toString();

                int totalTimer = (Integer.parseInt(strHour) * 60 + Integer.parseInt(strMin));
                int plusTime = getPlusTime(totalTimer);

                Log.d("real", "" + (totalTimer + plusTime));

                if (strHour.equals("0"))
                    time = strMin + " 분";
                else if (strMin.equals("0"))
                    time = strHour + " 시간";
                else
                    time = strHour + " 시간 " + strMin + " 분";

                timer_label.setText(time);
                timer_label.setTextColor(Color.BLACK);
                upper_label.setVisibility(View.VISIBLE);

                plus_label.setText("+ " + plusTime + "분");
                plus_label.setVisibility(View.VISIBLE);

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();

            }
        });

        ((Button)dlg.findViewById(R.id.dialog_btnClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }

    public boolean getStartTime(int timer) {
        try {
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm");
            String apStr;
            String timerStr;
            String apTimeStr;
            Date apDate;
            Date timerDate;

            apTimeStr = tvApTime.getText().toString();
            if (apTimeStr.substring(0, 2).equals("PM")) {
                if (!apTimeStr.substring(3, 5).equals("12")) {
                    int m_hour = Integer.parseInt(apTimeStr.substring(3, 5)) + 12;
                    apTimeStr = String.format("%02d:%02d", m_hour, Integer.parseInt(apTimeStr.substring(8)));
                }
            } else if (apTimeStr.substring(3, 5).equals("12")) {
                apTimeStr = String.format("%02d:%02d", 0, Integer.parseInt(apTimeStr.substring(8)));
            } else {
                apTimeStr = String.format("%02d:%02d", Integer.parseInt(apTimeStr.substring(3, 5)), Integer.parseInt(apTimeStr.substring(8)));
            }

            apStr = tvApDate.getText().toString() + " " + apTimeStr;
            apDate = dataFormat.parse(apStr);

            timerStr = (String) dataFormat.format(new Timestamp(apDate.getTime() - (timer + getPlusTime(timer)) * 60000));
            timerDate = dataFormat.parse(timerStr);

            if (System.currentTimeMillis() > timerDate.getTime()) {
                Toast.makeText(context, "현재 시간 이전으로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }

            tvTmDate.setText(timerStr.substring(0, 10));
            int m_hour = Integer.parseInt(timerStr.substring(11, 13));
            if (m_hour > 12) {
                m_hour = m_hour - 12;
                tvTmTime.setText("PM " + m_hour + " : " + timerStr.substring(14, 16));
            } else if (m_hour == 12) {
                tvTmTime.setText("PM 12 : " + timerStr.substring(14, 16));
            } else if (m_hour == 0) {
                tvTmTime.setText("AM 12 : " + timerStr.substring(14, 16));
            } else {
                tvTmTime.setText("AM " + m_hour + " : " + timerStr.substring(14, 16));
            }

            tvTmDate.setText(timerStr.substring(0, 10));
            return true;
        } catch (ParseException e) {
            Log.d("exception", e.toString());
            return false;
        }
    }

    public int getPlusTime(int timer) {
        int plusTime = 10;

        if (timer % 30 == 0) {  // 0,30,60 ...
            plusTime = (timer / 30) * 10;
        } else {
            plusTime = (timer / 30 + 1) * 10;
        }
        return plusTime;
    }
}
