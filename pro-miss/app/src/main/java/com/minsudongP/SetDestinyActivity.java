package com.minsudongP;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.minsudongP.Singletone.UrlConnection;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetDestinyActivity extends AppCompatActivity implements OnMapReadyCallback {


    ArrayList<PromissItem> arrayList=new ArrayList<>();
    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    EditText editText;
    NaverMap mMap;
    InputMethodManager imm;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destiny);


        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mapFragment.getMapAsync(this);
        recyclerView=findViewById(R.id.set_destiny_recle);
        adapter=new AllRecyclerAdapter(arrayList,SetDestinyActivity.this);

        recyclerView.setAdapter(adapter);
        linearLayout=findViewById(R.id.set_destiny_search_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editText=findViewById(R.id.set_destiny_edit);


        findViewById(R.id.set_destiny_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {

                imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                Double longitude=Double.parseDouble(arrayList.get(position).getPositionX());
                Double latitude=Double.parseDouble(arrayList.get(position).getPositionY());

                arrayList.clear();
                editText.setText("");
                adapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.GONE);
                LatLng latLng=new LatLng(latitude,longitude);
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing);
                mMap.moveCamera(cameraUpdate);

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Thread()
                {
                    @Override
                    public void run() {
                        if(!editText.getText().toString().equals(""))
                        GetSearchPlace();
                    }
                }.run();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                linearLayout.setVisibility(View.GONE);
                return true;
            }
        });

    }


    public void GetSearchPlace(){
        arrayList.clear();
        String address=editText.getText().toString();

        UrlConnection connection=UrlConnection.shardUrl;

        LatLng position=mMap.getCameraPosition().target;

        connection.MapSearch(address,position.longitude+","+position.latitude,callback);

    }

    Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            SetDestinyActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SetDestinyActivity.this,"네트워크를 확인해주세요",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body=response.body().string();

            Log.d("url",body);
            try {
                JSONObject jsonObject=new JSONObject(body);

                if(jsonObject.getString("status").equals("OK"))
                {
                    JSONArray data=jsonObject.getJSONArray("places");

                    for(int i=0;i<data.length();i++)
                    {
                        jsonObject=data.getJSONObject(i);
                        arrayList.add(new PromissItem(PromissType.SearchList,jsonObject.getString("name"),jsonObject.getString("jibun_address"),jsonObject.getString("x"),jsonObject.getString("y")));
                    }
                    SetDestinyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if(linearLayout.getVisibility()== View.GONE)
                                linearLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }else
                {
                    SetDestinyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SetDestinyActivity.this,"현재 장소를 불러올 수 없습니다.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mMap=naverMap;

        LatLng coord = new LatLng(37.5662952,126.97794509999994);


        mMap.setCameraPosition(new CameraPosition(coord, 17.0)); // 카메라 위치 셋팅


//        val uiSettings = mMap!!.uiSettings
//        uiSettings.isZoomControlEnabled = false // 줌 버튼 지우기
//        mMap!!.setContentPadding(0, 0, 0, 1410) // 로고 상단으로 올리기
    }
}
