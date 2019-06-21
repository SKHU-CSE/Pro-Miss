package com.minsudongP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DialogSelectTimer {
    private Context context;

    public DialogSelectTimer(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView timer_label, final TextView upper_label, final TextView plus_label, final int least) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_select_timer);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Spinner spHour = (Spinner) dlg.findViewById(R.id.dialog_spHour);
        final Spinner spMin = (Spinner) dlg.findViewById(R.id.dialog_spMin);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_okButton);


        ArrayAdapter<String> Houradapter = new ArrayAdapter<String>(context, R.layout.spinnertext, context.getResources().getStringArray(R.array.hour));
        ArrayAdapter<String> Minadapter = new ArrayAdapter<String>(context, R.layout.spinnertext, context.getResources().getStringArray(R.array.min));

        spHour.setAdapter(Houradapter);
        spMin.setAdapter(Minadapter);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 timer_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                String time = "";
                String strhour = spHour.getSelectedItem().toString();
                String strmin = spMin.getSelectedItem().toString();

                int totalTime = (Integer.parseInt(spHour.getSelectedItem().toString()) * 60 + Integer.parseInt(spMin.getSelectedItem().toString()));
                int plusTime = 0;

                if (totalTime == 0 ){
                    plusTime = 0;
                } else if(totalTime % 30 == 0) {
                    plusTime = (totalTime / 30 - 1) * 10;
                } else {
                    plusTime = (totalTime / 30) * 10;
                }
                Log.d("real", String.valueOf(totalTime+plusTime));
                if (totalTime + plusTime > least) {
                    AlertDialog.Builder alert_time = new AlertDialog.Builder(context);
                    alert_time.setTitle("시간설정 오류");
                    alert_time.setMessage("약속이 시작하는 시간이 현재시간 이전이므로 설정할 수 없습니다.");
                    alert_time.setPositiveButton("확인", null);
                    alert_time.create().show();
                }
                else {
                    if (strhour.equals("0"))
                        time = strmin + " 분";
                    else if (strmin.equals("0"))
                        time = strhour + " 시간";
                    else
                        time = strhour + " 시간 " + strmin + " 분";

                    timer_label.setText(time);
                    timer_label.setTextColor(Color.BLACK);
                    upper_label.setVisibility(View.VISIBLE);

                    plus_label.setText("+ "+plusTime+"분");
                    plus_label.setVisibility(View.VISIBLE);
                    Log.d("timer", spHour.getSelectedItem().toString());


                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            }
        });
    }
}
