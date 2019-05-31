package com.minsudongP.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.FriendListActivity;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.R;
import com.minsudongP.Model.appointment;

import java.util.ArrayList;

public class MemberFragment extends Fragment {
    private LinearLayout layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_3,null);
    }


    ArrayList<PromissItem> arrayList=new ArrayList<>();
    AllRecyclerAdapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter=new AllRecyclerAdapter(arrayList,getActivity());

        RecyclerView recyclerView= view.findViewById(R.id.frg_appoint3_recycle);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);
        View.OnClickListener MainListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((appointment)getActivity()).setAppointment_role_3(arrayList);
            }
        };

        arrayList.add(new PromissItem(PromissType.FriendList_Grid,13,"","임수현"));
        arrayList.add(new PromissItem(PromissType.FriendList_Grid,0,"추가하기","추가하기"));
        adapter.notifyDataSetChanged();

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent = new Intent(getActivity(), FriendListActivity.class);
                startActivity(intent);
            }
        });
        ((Button)getActivity().findViewById(R.id.frg_appoint3_confirmBtn)).setOnClickListener(MainListener);

    }

}
