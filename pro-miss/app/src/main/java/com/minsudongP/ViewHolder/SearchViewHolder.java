package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.minsudongP.R;

public class SearchViewHolder extends RecyclerView.ViewHolder {
    public TextView address;
    public TextView jibun;


    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        address= itemView.findViewById(R.id.searchList_address);
        jibun=itemView.findViewById(R.id.searchList_jibun);
    }
}
