package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

import org.w3c.dom.Text;

public class CancelViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView date;
    public TextView time;
    public TextView place;

    public CancelViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.cancel_Name);
        date=itemView.findViewById(R.id.cancel_Date);
        time=itemView.findViewById(R.id.cancel_Time);
        place=itemView.findViewById(R.id.cancel_PromiseName);
    }
}
