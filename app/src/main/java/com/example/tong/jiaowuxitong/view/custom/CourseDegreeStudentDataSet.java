package com.example.tong.jiaowuxitong.view.custom;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by TONG on 2017/1/26.
 * 学生成绩barchart数据封装
 */
public class CourseDegreeStudentDataSet {
    private ArrayList<BarEntry> le;

    public ArrayList<BarEntry> getLe() {
        return le;
    }

    public CourseDegreeStudentDataSet() {
        le = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            BarEntry barEntry = new BarEntry(i, 0);
            le.add(barEntry);
        }
    }

    public void add(float degree) {

        if (degree >= 90) {
            //great
            le.get(4).setY(le.get(4).getY() + 1);

        } else if (degree >= 80) {
            // good
            le.get(3).setY(le.get(3).getY() + 1);
        } else if (degree >= 70) {
            //middle
            le.get(2).setY(le.get(2).getY() + 1);
        } else if (degree >= 60) {
            //pass
            le.get(1).setY(le.get(1).getY() + 1);
        } else {
            le.get(0).setY(le.get(0).getY() + 1);

        }
    }

}
