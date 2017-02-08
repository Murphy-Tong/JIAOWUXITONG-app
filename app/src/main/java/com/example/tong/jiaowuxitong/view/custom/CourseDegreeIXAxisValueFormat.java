package com.example.tong.jiaowuxitong.view.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by TONG on 2017/1/26.
 * barchart x坐标数据
 */
public class CourseDegreeIXAxisValueFormat implements IAxisValueFormatter {

    private String[] xs = { "不及格", "及格", "中等", "良好","优秀"};

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return xs[((int) value) % xs.length];
    }
}
