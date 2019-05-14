package com.minsudongP;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.minsudongP.ViewHolder.FriendViewHolder;
import com.minsudongP.ViewHolder.SearchViewHolder;

import java.util.ArrayList;

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
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    void BindSearchList(RecyclerView.ViewHolder viewHolder,int position){
        SearchViewHolder holder=(SearchViewHolder)viewHolder;

        holder.address.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.address.setText(arrayList.get(position).getAddress());
        holder.jibun.setText(arrayList.get(position).getJibun());
    }

    void BindFriendLIst(RecyclerView.ViewHolder viewHolder,int position){

        FriendViewHolder holder=(FriendViewHolder)viewHolder;

        holder.friendName.setText(arrayList.get(position).getName());
        holder.friendImage.setImageResource(R.drawable.face);

    }
}
