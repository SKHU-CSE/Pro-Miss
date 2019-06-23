package com.minsudongP.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.minsudongP.R;

public class RangkingViewHolder extends RecyclerView.ViewHolder {

    public SeekBar seekBar;
    public ProgressBar progressBar;
    public TextView rangking;
    public TextView name;

    public RangkingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar=itemView.findViewById(R.id.rangking_progressbar);
        seekBar=itemView.findViewById(R.id.rangking_seekbar);
        rangking=itemView.findViewById(R.id.rangking_num);
        name=itemView.findViewById(R.id.rangking_name);
    }
}
