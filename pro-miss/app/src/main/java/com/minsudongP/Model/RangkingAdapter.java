package com.minsudongP.Model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.minsudongP.R;
import com.minsudongP.ViewHolder.RangkingViewHolder;

import java.util.ArrayList;

public class RangkingAdapter extends RecyclerView.Adapter<RangkingViewHolder> {

    ArrayList<RangkingItem> arrayList;
    Activity activity;

    public RangkingAdapter(ArrayList<RangkingItem> arrayList, Activity activity)
    {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public RangkingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RangkingViewHolder(activity.getLayoutInflater().inflate(R.layout.ranking_appoint,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RangkingViewHolder rangkingViewHolder, int i) {
        try {
            RangkingItem item = arrayList.get(i);

            rangkingViewHolder.rangking.setText((i + 1) + " 순위");
            rangkingViewHolder.name.setText(item.getName());
            rangkingViewHolder.seekBar.setProgress(item.percent);
            rangkingViewHolder.progressBar.setProgress(item.percent);

            rangkingViewHolder.seekBar.setOnTouchListener(new View.OnTouchListener() { //사용자 터치 막기
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
