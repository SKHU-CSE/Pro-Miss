package com.minsudongP;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.minsudongP.SaveSharedPreference.clearUserInfo;
import static com.minsudongP.SaveSharedPreference.getUserId;
import static com.minsudongP.SaveSharedPreference.getUserPw;
import static com.minsudongP.SaveSharedPreference.setUserInfo;

public class LoginActivity extends AppCompatActivity {

    EditText edit_Id;
    EditText edit_pwd;
    InputMethodManager inputMethodManager;
    RotateLoading loading;
    private SessionCallback sessionCallback;

    final String RESULT_OK = "ok";
    final int RESULT_SUCCESS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sessionCallback = new SessionCallback(); //SessionCallback 초기화
        Session.getCurrentSession().addCallback(sessionCallback); //현재 세션에 콜백 붙임
        Session.getCurrentSession().checkAndImplicitOpen(); //자동 로그인

        // 저장된 값이 있으면 자동 로그인
        if (getUserId(getApplicationContext()).length() != 0 && getUserPw(getApplicationContext()).length() != 0) {
            login(getUserId(getApplicationContext()), getUserPw(getApplicationContext()), false);
            Toast.makeText(LoginActivity.this, "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();

        }

        edit_Id = findViewById(R.id.login_Id);
        edit_pwd = findViewById(R.id.login_pwd);
        loading = findViewById(R.id.login_rotateloading);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        View view = findViewById(R.id.login_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //키보드 내리기
                inputMethodManager.hideSoftInputFromWindow(edit_Id.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
            }
        });

        // 회원가입 화면으로 이동
        ((Button) findViewById(R.id.login_gotoregister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 클릭
        ((Button) findViewById(R.id.login_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = edit_Id.getText().toString();
                final String pwd = edit_pwd.getText().toString();

                // 자동로그인이 체크되어 있다면 정보 저장하기
                if (((CheckBox) findViewById(R.id.login_autoLogin)).isChecked()) {
                    setUserInfo(getApplicationContext(), id, pwd);
                } else { // 체크되어 있지 않다면 정보 삭제하기
                    clearUserInfo(getApplicationContext());
                }

                inputMethodManager.hideSoftInputFromWindow(edit_Id.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);

                if (id.isEmpty() || pwd.isEmpty())
                    Toast.makeText(LoginActivity.this, "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                else {
                    loading.start();
                    login(edit_Id.getText().toString(), edit_pwd.getText().toString(), false);
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback); //현재 액티비티 제거 시 콜백도 같이 제거
    }

    private void checkID(final MeV2Response userinfo) {
        new Thread() {
            public void run() {
                // 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                String php = "api/userId/"+userinfo.getId();

                UrlConnection.shardUrl.GetRequest(php, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.stop();
                                Toast.makeText(LoginActivity.this, "서버 통신에 문제가 있습니다.\n 관리자에게 문의주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();

                        try {
                            JSONObject result = new JSONObject(s);

                            // 등록된 아이디가 없으면 회원 등록
                            if (result.getString("result").equals(RESULT_OK)) {
                                storeKakaoUser(userinfo);
                            } else if (result.getInt("result") == 1000) { // 있으면 로그인
                                login(String.valueOf(userinfo.getId()),String.valueOf(userinfo.getId()), true);
                            }

                        } catch (JSONException e) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clearUserInfo(getApplicationContext());
                                    loading.stop();
                                    Toast.makeText(LoginActivity.this, "서버 통신에 문제가 있습니다.\n 관리자에게 문의주세요", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }.start();
    }

    private void login(String id, String pw, final boolean kakao) {
        final HashMap<String, String> hash = new HashMap<>();
        hash.put("Id", id);
        hash.put("password", pw);
        new Thread() {
            public void run() {
                UrlConnection.shardUrl.PostRequest("api/userLogin", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.stop();
                                Toast.makeText(LoginActivity.this, "서버 통신에 문제가 있습니다.\n 관리자에게 문의주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();

                        System.out.println(s);
                        try {
                            JSONObject result = new JSONObject(s);

                            // 로그인 성공 시
                            if (result.getInt("result") == RESULT_SUCCESS ) {
                                JSONObject jsonObject = result.getJSONObject("data");

                                if(jsonObject.getInt("isKakao") != 0 && !kakao){ // 카카오 계정일 경우 일반로그인 막기
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            clearUserInfo(getApplicationContext());
                                            loading.stop();
                                            Toast.makeText(LoginActivity.this, "해당 정보와 일치하는 \n회원이 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    UserInfor infor = UserInfor.shared;
                                    infor.setAppoint_num(jsonObject.getInt("appoint_num"));
                                    infor.setID(jsonObject.getString("email"));
                                    infor.setId_num(jsonObject.getString("id"));
                                    infor.setMoney(jsonObject.getInt("money"));
                                    infor.setSuccess_appoint_num(jsonObject.getInt("success_appoint_num"));
                                    infor.setName(jsonObject.getString("name"));
                                    infor.setProfile_img(jsonObject.getString("Image"));




                                    // 정보 다 받아오면 UI 스레드에서 화면 갱신
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading.stop();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            } else { // 회원정보 없음
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        clearUserInfo(getApplicationContext());
                                        loading.stop();
                                        Toast.makeText(LoginActivity.this, "해당 정보와 일치하는 \n회원이 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clearUserInfo(getApplicationContext());
                                    loading.stop();
                                    Toast.makeText(LoginActivity.this, "서버 통신에 문제가 있습니다.\n 관리자에게 문의주세요", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, hash);

            }
        }.start();
    }

    private void storeKakaoUser (MeV2Response userinfo){
        final HashMap<String, String> hash = new HashMap<>();
        hash.put("email", String.valueOf(userinfo.getId()));
        hash.put("password", String.valueOf(userinfo.getId()));
        hash.put("name", String.valueOf(userinfo.getNickname()));
        hash.put("Image", String.valueOf(userinfo.getProfileImagePath()));
        hash.put("isKakao", String.valueOf(1));

        new Thread() {
            public void run() {
                // 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                UrlConnection.shardUrl.PostRequest("api/user", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.stop();
                                Toast.makeText(LoginActivity.this, "서버 통신에 문제가 있습니다.\n 관리자에게 문의주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();

                        Log.d("response = ",s);
                        try {
                            JSONObject result = new JSONObject(s);

                            // 회원정보 삽입 성공 시
                            if (result.getInt("result") == RESULT_SUCCESS) {
                                JSONObject jsonObject = result.getJSONObject("data");
                                Log.d("json?",jsonObject.toString());

                                // 싱글톤에 값 넣기
                                UserInfor infor = UserInfor.shared;
                                infor.setID(jsonObject.getString("email"));
                                infor.setId_num(jsonObject.getString("id"));
                                infor.setName(jsonObject.getString("name"));
                                infor.setProfile_img(jsonObject.getString("Image"));
                                infor.setMoney(jsonObject.getInt("money"));

                                // 해당 값들은 json에 없으므로 0 삽입
                                infor.setAppoint_num(0);
                                infor.setSuccess_appoint_num(0);

                            } else { // 회원정보 없음
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        clearUserInfo(getApplicationContext());
                                        loading.stop();
                                        Toast.makeText(LoginActivity.this, "현재 DB서버가 문제가 있어 관리자에게 문의를 주시길 바랍니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            Log.d("서버통신 = ","실패");
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clearUserInfo(getApplicationContext());
                                    loading.stop();
                                    Toast.makeText(LoginActivity.this, "서버 통신에 문제가 있습니다.\n 관리자에게 문의주세요", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, hash);

            }
        }.start();
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    clearUserInfo(getApplicationContext());
                    int result = errorResult.getErrorCode();


                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    clearUserInfo(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Toast.makeText(LoginActivity.this,"카카오 계정으로 로그인 되었습니다.",Toast.LENGTH_SHORT).show();


                    // 회원 정보가 있는지 없는지 확인
                    checkID(result);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            clearUserInfo(getApplicationContext());
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}

