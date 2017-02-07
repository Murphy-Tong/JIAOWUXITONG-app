package com.example.tong.jiaowuxitong.view.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by TONG on 2017/1/26.
 */
public class CourseDegreeIYAxisValueFormat implements IAxisValueFormatter {


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return StringUtils.formatFloat(value,0);
    }
}
