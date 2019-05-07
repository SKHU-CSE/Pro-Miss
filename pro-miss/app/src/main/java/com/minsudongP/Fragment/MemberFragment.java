package com.minsudongP.Fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.R;
import com.minsudongP.SettingsActivity;

public class MemberFragment extends Fragment {
    private LinearLayout layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_3,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener MainListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "약속이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        };
        ((Button)getActivity().findViewById(R.id.frg_appoint3_confirmBtn)).setOnClickListener(MainListener);


    }

}
