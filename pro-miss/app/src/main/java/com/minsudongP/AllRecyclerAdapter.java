package com.minsudongP;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.minsudongP.ViewHolder.FriendViewHolder;

import com.minsudongP.ViewHolder.SearchViewHolder;

import com.minsudongP.ViewHolder.NewAppointViewHolder;


import java.util.ArrayList;

import static com.minsudongP.PromissType.FriendLIst;
import static com.minsudongP.PromissType.New_Appoint;
import static com.minsudongP.PromissType.SearchList;

public class AllRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<PromissItem> arrayList;
    Activity activity;

    PromissClick click;

    interface PromissClick{
        public void OnClick(View view,int position);
    }


    public AllRecyclerAdapter(ArrayList<PromissItem> arrayList, Activity activity)
    {
        this.arrayList=arrayList;
        this.activity=activity;

    }

    public void SetClickListner(PromissClick click)
    {
        this.click=click;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewposition) {

        RecyclerView.ViewHolder viewHolder=null;
        View view;

        switch (arrayList.get(viewposition).getType())
        {
            case FriendLIst:
                view=activity.getLayoutInflater().inflate(R.layout.friendlist_card,viewGroup,false);

                viewHolder=new FriendViewHolder(view);
                break;
            case SearchList:
                view=activity.getLayoutInflater().inflate(R.layout.searchlist_item,viewGroup,false);
                viewHolder=new SearchViewHolder(view);
                break;


            case New_Appoint:
                view=activity.getLayoutInflater().inflate(R.layout.alert_invite,viewGroup,false);
                viewHolder=new NewAppointViewHolder(view);
                break;
            default:
                view=activity.getLayoutInflater().inflate(R.layout.friendlist_card,viewGroup,false);
                viewHolder=new FriendViewHolder(view);
                break;

        }


        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        switch (arrayList.get(i).getType())
        {
            case FriendLIst:
                BindFriendLIst(viewHolder,i);
                break;

            case SearchList:
                BindSearchList(viewHolder,i);
                break;
            case New_Appoint:
                BindNew_Appoint(viewHolder,i);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    void BindSearchList(RecyclerView.ViewHolder viewHolder, final int position){
        SearchViewHolder holder=(SearchViewHolder)viewHolder;

        holder.address.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.OnClick(v,position);
            }
        });

        holder.address.setText(arrayList.get(position).getAddress());
        holder.jibun.setText(arrayList.get(position).getJibun());
    }

    void BindNew_Appoint(RecyclerView.ViewHolder viewHolder,int position)
    {
        NewAppointViewHolder holder=(NewAppointViewHolder)viewHolder;

        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
    }

    void BindFriendLIst(RecyclerView.ViewHolder viewHolder,int position){

        FriendViewHolder holder=(FriendViewHolder)viewHolder;

        holder.friendName.setText(arrayList.get(position).getName());
        holder.friendImage.setImageResource(R.drawable.face);

    }
}
