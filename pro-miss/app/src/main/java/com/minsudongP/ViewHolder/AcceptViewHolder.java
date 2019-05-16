package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class AcceptViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView date;
    public TextView time;
    public TextView place;

    public AcceptViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.accept_Name);
        date=itemView.findViewById(R.id.accept_Date);
        time=itemView.findViewById(R.id.accept_Time);
        place=itemView.findViewById(R.id.accept_PromiseName);

    }
}
