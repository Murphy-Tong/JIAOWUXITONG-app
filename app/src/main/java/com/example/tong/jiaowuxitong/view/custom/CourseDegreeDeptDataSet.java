package com.example.tong.jiaowuxitong.view.custom;

import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by TONG on 2017/1/26.
 * 课程成绩展示barchart 数据封装
 */

public class CourseDegreeDeptDataSet {
    private ArrayList<BarEntry> le;

    public ArrayList<BarEntry> getLe() {
        return le;
    }

    public CourseDegreeDeptDataSet(int s) {
        le = new ArrayList<>(s);
    }

    public void add(VOCourse voCourse) {
        BarEntry barEntry = new BarEntry(le.size(), Float.valueOf(StringUtils.formatFloat(voCourse.getExcellentRate(), 2)));
        le.add(barEntry);
    }

}
