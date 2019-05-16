package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class FollowViewHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public FollowViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.follow_Name);
    }
}
