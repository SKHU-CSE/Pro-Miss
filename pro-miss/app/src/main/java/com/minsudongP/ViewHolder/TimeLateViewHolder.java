package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class TimeLateViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView time;
    public TextView place;
    public TextView money;

    public TimeLateViewHolder(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.late_Date);
        time=itemView.findViewById(R.id.late_Time);
        place=itemView.findViewById(R.id.late_PromiseName);
        money=itemView.findViewById(R.id.late_Money);
    }
}
