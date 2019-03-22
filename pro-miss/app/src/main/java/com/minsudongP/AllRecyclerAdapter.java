package com.minsudongP;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.minsudongP.ViewHolder.FriendViewHolder;

import java.util.ArrayList;

public class AllRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<PromissItem> arrayList;
    Context context;

    public AllRecyclerAdapter(ArrayList<PromissItem> arrayList, Context context)
    {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewposition) {

        RecyclerView.ViewHolder viewHolder=null;
        View view=View.inflate(context,R.layout.friendlist_card,null);

        switch (arrayList.get(viewposition).getType())
        {
            case FriendLIst:
                viewHolder=new FriendViewHolder(view);
                break;
                default:
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
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    void BindFriendLIst(RecyclerView.ViewHolder viewHolder,int position){

        FriendViewHolder holder=(FriendViewHolder)viewHolder;

        holder.friendName.setText(arrayList.get(position).getName());
        holder.friendImage.setImageResource(R.drawable.face);

    }
}
