package com.example.tong.jiaowuxitong.view.custom;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by TONG on 2017/1/20.
 * barchart数据封装
 */
public class DataSet {
    private ArrayList<BarEntry> le;

    public ArrayList<BarEntry> getLe() {
        return le;
    }

    public DataSet() {
        le = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            BarEntry barEntry = new BarEntry(i, 0);
            le.add(barEntry);
        }
    }

    public void add(float degree) {

        BarEntry barEntry;
        if (degree <= 0) {
            barEntry = le.get(0);
            barEntry.setY(barEntry.getY()+1);
            return;
        }
        int i = (int) (degree / 10);
        i = i == 0 ? 1 : i+1;
        i = i >= 10 ? 10 : i;
        barEntry = le.get(i);
        if (barEntry != null) {
            barEntry.setY(barEntry.getY() + 1);
        } else {
            barEntry = new BarEntry(i, 1);
            le.set(i, barEntry);
        }
    }
}
