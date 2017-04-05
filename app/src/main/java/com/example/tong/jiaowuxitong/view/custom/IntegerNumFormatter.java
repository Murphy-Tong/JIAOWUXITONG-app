package com.example.tong.jiaowuxitong.view.custom;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/4/5.
 */
public class IntegerNumFormatter implements IValueFormatter {
    private DecimalFormat mFormat;
    public IntegerNumFormatter(){
        mFormat = new DecimalFormat("###");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}
