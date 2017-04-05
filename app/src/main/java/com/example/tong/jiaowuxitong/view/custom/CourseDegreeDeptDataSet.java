package com.example.tong.jiaowuxitong.view.custom;

import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by TONG on 2017/1/26.
 * 课程成绩展示barchart 数据封装
 */

public class CourseDegreeDeptDataSet {
    private ArrayList<BarEntry> excell;
    private ArrayList<BarEntry> unpass;

    public ArrayList<BarEntry> getExcell() {
        return excell;
    }

    public ArrayList<BarEntry> getUnpass() {
        return unpass;
    }

    public CourseDegreeDeptDataSet(int s) {
        excell = new ArrayList<>(s);
        unpass = new ArrayList<>(s);
    }

    public void add(VOCourse voCourse) {
        BarEntry barEntry = new BarEntry(excell.size(), Float.valueOf(StringUtils.formatFloat(voCourse.getExcellentRate(), 2)));
        excell.add(barEntry);
        BarEntry barEntry2 = new BarEntry(unpass.size(), Float.valueOf(StringUtils.formatFloat(voCourse.getUnpassRate(), 2)));
        unpass.add(barEntry2);
    }

}
