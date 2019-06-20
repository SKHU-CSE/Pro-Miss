package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.minsudongP.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Member_ListViewHolder extends RecyclerView.ViewHolder{
    public CircleImageView proflie;
    public Member_ListViewHolder(@NonNull View itemView) {
        super(itemView);

        proflie=itemView.findViewById(R.id.member_list_image);
    }
}
