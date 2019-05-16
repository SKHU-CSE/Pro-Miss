package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class LateMemberViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView time;
    public TextView place;
    public TextView member;

    public LateMemberViewHolder(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.safe_Date);
        time=itemView.findViewById(R.id.safe_Time);
        place=itemView.findViewById(R.id.safe_PromiseName);
        member=itemView.findViewById(R.id.safe_Member);
    }
}
