package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

import org.w3c.dom.Text;

public class PastViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView place;
    public TextView ptcmember;
    public TextView scsmember;
    public TextView mymoney;
    public TextView allmoney;

    public PastViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.past_CardDate);
        place = itemView.findViewById(R.id.past_CardTitle);
        ptcmember = itemView.findViewById(R.id.past_participation_count);
        scsmember = itemView.findViewById(R.id.past_success_count);
        mymoney = itemView.findViewById(R.id.past_MyMoney);
        allmoney = itemView.findViewById(R.id.past_AllMoney);

    }
}
