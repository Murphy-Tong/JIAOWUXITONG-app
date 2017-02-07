package com.example.tong.jiaowuxitong.view.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.TestUtil;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.custom.DataSet;
import com.example.tong.jiaowuxitong.view.custom.DayAxisValueFormatter;
import com.example.tong.jiaowuxitong.view.custom.MyAxisValueFormatter;
import com.example.tong.jiaowuxitong.view.custom.StringUtils;
import com.example.tong.jiaowuxitong.view.custom.XYMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/1/22.
 */
public class CourseDegreeChartDiolagFragment extends DialogFragment {

    public  View mView;
    private VOStdCrs voStdCrs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            voStdCrs = (VOStdCrs) getArguments().getSerializable("stdcrs");
        }
    }

    private BarChart mChart;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setWindowAnimations(R.style.dialogAnim);

        mView = inflater.inflate(R.layout.activity_barchart, container, false);
        mChart = (BarChart) mView.findViewById(R.id.barChart);
        progressBar = (ProgressBar) mView.findViewById(R.id.pb);
        initChart();
        return mView;
    }


/*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        return dialog;
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        com.example.tong.jiaowuxitong.TestUtil.log("resume","res");
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        com.example.tong.jiaowuxitong.TestUtil.log("stop","stop");
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    private final int STD_DEGREE_TAG = 1022;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.example.tong.jiaowuxitong.TestUtil.log("onViewCreated","onViewCreated");
        progressBar.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.INVISIBLE);
        EventBus.getDefault().register(this);
        NetUtil.asyncPost(GsonUtil.toJson(voStdCrs), GlobalResource.DEGREE_DETAIL, STD_DEGREE_TAG);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onPost(Message msg) {
        if (msg != null && msg.tag == STD_DEGREE_TAG) {
            ArrayList<Float> floatArrayList = GsonUtil.fromJson((String) msg.msg, new TypeToken<ArrayList<Float>>() {
            }.getType());
            if (floatArrayList != null)
                setData(floatArrayList);
        }
    }


    private void setData(ArrayList<Float> lf) {
        if (mChart == null) return;

        progressBar.setVisibility(View.GONE);
        mChart.setVisibility(View.VISIBLE);

        DataSet dataSet = new DataSet();
        int c = 0;
        for (Float f : lf
                ) {
            if (f < voStdCrs.getEvaDegree()) c++;
            dataSet.add(f);
        }


        BarDataSet set1 = new BarDataSet(dataSet.getLe(), String.format(getString(R.string.degreeDetail), StringUtils.formatFloat(voStdCrs.getDegree(), 1), StringUtils.float2PercentString(c * 1.f / lf.size(), 2)));
        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
//        data.setValueTypeface(mTfLight);
        data.setBarWidth(0.9f);

        mChart.setData(data);

    }

    private void initChart() {

        if (mChart == null) return;
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(true);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(10);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mChart.setDrawValueAboveBar(true);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setDrawTopYLabelEntry(true);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
    }

}
