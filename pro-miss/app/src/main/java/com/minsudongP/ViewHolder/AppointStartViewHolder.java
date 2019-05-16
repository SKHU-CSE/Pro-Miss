package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class AppointStartViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView time;
    public TextView place;

    public AppointStartViewHolder(@NonNull View itemView) {
        super(itemView);

        date=itemView.findViewById(R.id.timer_Date);
        time=itemView.findViewById(R.id.timer_Time);
        place=itemView.findViewById(R.id.timer_PromiseName);

    }
}
