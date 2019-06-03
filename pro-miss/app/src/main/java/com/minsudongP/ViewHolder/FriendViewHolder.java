package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minsudongP.R;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    public ImageView friendImage;
    public TextView friendName;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);

        friendImage = itemView.findViewById(R.id.friend_image);
        friendName = itemView.findViewById(R.id.friend_name);
    }

}
