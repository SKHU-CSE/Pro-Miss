package com.minsudongP.Model;

import android.support.annotation.NonNull;

public class RangkingItem implements Comparable  {
    int percent;
    String name;

    public RangkingItem(int percent,String name)
    {
        this.percent=percent;
        this.name=name;
    }
    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String namae) {
        this.name = namae;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        RangkingItem item=(RangkingItem)o;

        return ((RangkingItem) o).percent-percent;
    }
}
