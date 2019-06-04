package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class AttendViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView time;
    public TextView place;

    public AttendViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.attend_CardDate);
        time = itemView.findViewById(R.id.attend_CardTime);
        place = itemView.findViewById(R.id.attend_CardTitle);
    }
}