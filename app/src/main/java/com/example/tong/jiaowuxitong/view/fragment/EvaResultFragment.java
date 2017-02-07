package com.example.tong.jiaowuxitong.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.tong.jiaowuxitong.view.views.CourseEvaluationActivity;
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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.activity_barchart)
public class EvaResultFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @ViewInject(R.id.barChart)
    private BarChart mChart;
    @ViewInject(R.id.pb)
    private ProgressBar progressBar;
    private VOStdCrs voStdCrs;

    public EvaResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EvaResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EvaResultFragment newInstance(String param1, String param2) {
        EvaResultFragment fragment = new EvaResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            voStdCrs = (VOStdCrs) getArguments().getSerializable("body");

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.INVISIBLE);
        initChart();
        loadData();
    }

    private static final int Tag = 3221;

    private void loadData() {
        if (voStdCrs != null) {
            TestUtil.log("loaddate", voStdCrs.getCourseId() + voStdCrs.getName());

            NetUtil.asyncPost(GsonUtil.toJson(voStdCrs), GlobalResource.GET_EVA_DETAIL, Tag);
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message msg) {
        if (msg != null && msg.tag == Tag && !TextUtils.isEmpty((String) msg.msg)) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> lf = GsonUtil.fromJson((String) msg.msg, type);
            if (lf != null && lf.size() == 2) {
                String detail = lf.get(0);
                VOStdCrs voStdCrs = GsonUtil.fromJson(detail, VOStdCrs.class);
                if (voStdCrs != null) this.voStdCrs = voStdCrs;
                CourseEvaluationActivity.voStdCrs = voStdCrs;
                ArrayList<Float> floatArrayList = GsonUtil.fromJson(lf.get(1), new TypeToken<ArrayList<Float>>() {
                }.getType());
                if (floatArrayList != null)
                    setData(floatArrayList);
            }

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


        BarDataSet set1 = new BarDataSet(dataSet.getLe(), String.format(getString(R.string.evaDetail), StringUtils.formatFloat(voStdCrs.getEvaDegree(), 1), StringUtils.float2PercentString(c * 1.f / lf.size(), 2)));
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

        XYMarkerView mv = new XYMarkerView(mContext, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = x.view().inject(this, inflater, container);
        return view;
    }

}
