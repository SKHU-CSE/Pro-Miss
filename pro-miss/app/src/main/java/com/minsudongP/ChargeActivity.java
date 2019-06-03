package com.minsudongP;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChargeActivity extends AppCompatActivity {
    ArrayList<String> moneytext = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        final UserInfor infor = UserInfor.shared;

        findViewById(R.id.charge_backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.charge_chargeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moneytext.isEmpty()) {
                    Toast.makeText(ChargeActivity.this, "금액을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String val = "";
                for (String s : moneytext) {
                    val += s;
                }
                Intent intent = new Intent();
                intent.putExtra("money", val);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final LinearLayout layout = findViewById(R.id.charge_addmoney);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.charge_cancelbtn:
                        layout.removeAllViews();
                        moneytext.removeAll(moneytext);
                        break;
                    case R.id.charge_deletebtn:

                        try {
                            final TextView money = (TextView) layout.getChildAt(layout.getChildCount() - 1);
                            money.animate()
                                    .alpha(0.1f)
                                    .setDuration(200)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            layout.removeView(money);
                                            moneytext.remove(moneytext.size() - 1);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });

                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:

                        if (layout.getChildCount() < 9) {
                            TextView money2 = (TextView) LayoutInflater.from(ChargeActivity.this).inflate(R.layout.addmoneytextview, null, false);
                            String text = ((Button) view).getText().toString();
                            money2.setText(text);
                            AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
                            animation.setDuration(400);
                            money2.startAnimation(animation);
                            layout.addView(money2);
                            moneytext.add(text);
                        }
                        break;
                }
            }
        };

        ((Button) findViewById(R.id.charge_onebtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_twobtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_threebtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_fourbtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_fivebtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_sixbtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_sevenbtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_eightbtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_ninebtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_zerobtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_deletebtn)).setOnClickListener(listener);
        ((Button) findViewById(R.id.charge_cancelbtn)).setOnClickListener(listener);
    }
}