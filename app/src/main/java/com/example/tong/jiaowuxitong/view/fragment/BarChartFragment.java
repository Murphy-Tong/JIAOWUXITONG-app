package com.example.tong.jiaowuxitong.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;


/**
 * 通用的barchart fragment
 */
public class BarChartFragment extends Fragment {

    private IAxisValueFormatter xAxisFormatter;
    private int count;
    private MarkerView mv;
    private IAxisValueFormatter custom;

    public BarChartFragment() {
    }


    private VOCourse voCourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BarChart mChart;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_barchart, container, false);
        mChart = (BarChart) view.findViewById(R.id.barChart);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        }

        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = 1000;
        view.setLayoutParams(layoutParams);
        return view;
    }


    /**
     * 初始化一下状态
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.INVISIBLE);
        if (!isinited) {
            initChart();
        }
    }


    /**
     * 设置barchart中要显示的数据
     * @param dataSets
     */
    public void setData(ArrayList<IBarDataSet> dataSets) {
        if (mChart == null) return;

        progressBar.setVisibility(View.GONE);
        mChart.setVisibility(View.VISIBLE);

//        DataSet dataSet = new DataSet();
//        int c = 0;
//        for (Float f : lf
//                ) {
//            dataSet.add(f);
//        }
//
//
//        BarDataSet set1 = new BarDataSet(dataSet.getLe(), String.format(getString(R.string.degreeDetail), StringUtils.formatFloat(22, 1), StringUtils.float2PercentString(c * 1.f / lf.size(), 2)));
//        set1.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(set1);
//
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
//        data.setValueTypeface(mTfLight);

        data.setBarWidth(0.9f);

        mChart.setData(data);

    }

    private String describe;

    public void initChart() {

        if (mChart == null) return;
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(true);

        if (!TextUtils.isEmpty(describe))
            mChart.getDescription().setText(describe);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        mChart.setMaxVisibleValueCount(maxVisit);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mChart.setDrawValueAboveBar(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(count);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
//        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);/*
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setDrawTopYLabelEntry(true);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);*/ // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(20f);
        l.setTextSize(11f);
        l.setXEntrySpace(1f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        if (mv != null) {
            mv.setChartView(mChart); // For bounds control
            mChart.setMarker(mv); // Set the marker to the ch   }
        }
    }

    private boolean isinited = false;

    public void initChart(IAxisValueFormatter xAxisFormatter, IAxisValueFormatter custom, MarkerView mv, int count, String describe) {
        this.xAxisFormatter = xAxisFormatter;
        this.custom = custom;
        this.describe = describe;
        this.mv = mv;
        this.count = count;
        initChart();
    }
}
