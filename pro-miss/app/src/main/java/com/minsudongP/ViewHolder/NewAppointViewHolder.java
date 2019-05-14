package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class NewAppointViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView time;
    public TextView place;

    public NewAppointViewHolder(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.invite_Date);
                time=itemView.findViewById(R.id.invite_AlertTime);
                        place=itemView.findViewById(R.id.invite_PromiseName);
    }
}
