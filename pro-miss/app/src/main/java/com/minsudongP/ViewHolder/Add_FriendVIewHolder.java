package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.minsudongP.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_FriendVIewHolder extends RecyclerView.ViewHolder {
    public CircleImageView imageView;
    public TextView name;


    public Add_FriendVIewHolder(@NonNull View itemView) {
        super(itemView);
        imageView =itemView.findViewById(R.id.add_friend_image);
        name=itemView.findViewById(R.id.add_freiend_name);
    }
}
