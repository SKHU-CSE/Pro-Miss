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
import com.minsudongP.Singletone.UserInfor;
import com.minsudongP.appointment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberFragment extends Fragment {
    private LinearLayout layout;
    final static int Request_code=1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList.add(new PromissItem(PromissType.FriendList_Grid, Integer.parseInt(UserInfor.shared.getId_num()), UserInfor.shared.getProfile_img(), UserInfor.shared.getName()));
        arrayList.add(new PromissItem(PromissType.FriendList_Grid, 0, "추가하기", "추가하기"));

    }
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


        adapter = new AllRecyclerAdapter(arrayList, getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.frg_appoint3_recycle);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        View.OnClickListener MainListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((appointment) getActivity()).setAppointment_role_3(arrayList);
            }
        };


        adapter.notifyDataSetChanged();

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
                @Override
                public void OnClick(View view, int position) {

                    try{
                        CircleImageView circleImageView=(CircleImageView)view; //remove 버튼을 눌렀을 때
                        arrayList.remove(position);
                        adapter.notifyItemRemoved(position);
                    }catch (ClassCastException e)
                    {
                        Intent intent = new Intent(getActivity(), FriendListActivity.class);
                        startActivityForResult(intent,Request_code);
                    }
                    }
        });
        ((Button) getActivity().findViewById(R.id.frg_appoint3_confirmBtn)).setOnClickListener(MainListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Request_code&&resultCode==1)
        {
            try {
                ArrayList<String> name = data.getStringArrayListExtra("name");
                ArrayList<String> Image = data.getStringArrayListExtra("Image");
                ArrayList<Integer> id = data.getIntegerArrayListExtra("id");

                for (int i = 0; i < name.size(); i++) {
                    arrayList.add(i+1, new PromissItem(PromissType.FriendList_Grid, id.get(i), Image.get(i), name.get(i)));
                    adapter.notifyItemInserted(i+1);
                }


            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
    }
}
