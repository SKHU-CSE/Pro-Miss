package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.minsudongP.R;

public class GPSViewHolder extends RecyclerView.ViewHolder {

    public Button button;

    public GPSViewHolder(@NonNull View itemView) {
        super(itemView);
        button=itemView.findViewById(R.id.alert_gps_btn);

    }
}
