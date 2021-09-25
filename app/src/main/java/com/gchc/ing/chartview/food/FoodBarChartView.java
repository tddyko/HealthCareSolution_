
package com.gchc.ing.chartview.food;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.gchc.ing.R;
import com.gchc.ing.charting.charts.BarChart;
import com.gchc.ing.charting.components.Legend;
import com.gchc.ing.charting.components.XAxis;
import com.gchc.ing.charting.components.YAxis;
import com.gchc.ing.charting.data.BarData;
import com.gchc.ing.charting.data.BarDataSet;
import com.gchc.ing.charting.data.BarEntry;
import com.gchc.ing.charting.data.CEntry;
import com.gchc.ing.charting.formatter.IAxisValueFormatter;
import com.gchc.ing.charting.highlight.Highlight;
import com.gchc.ing.charting.interfaces.datasets.IBarDataSet;
import com.gchc.ing.charting.listener.OnChartValueSelectedListener;
import com.gchc.ing.charting.utils.ColorTemplate;
import com.gchc.ing.charting.utils.MPPointF;
import com.gchc.ing.chartview.valueFormat.AxisValueFormatter;
import com.gchc.ing.chartview.valueFormat.BarDataFormatter;
import com.gchc.ing.chartview.valueFormat.MyAxisValueFormatter;
import com.gchc.ing.chartview.valueFormat.XYMarkerView;
import com.gchc.ing.base.value.TypeDataSet;

import java.util.ArrayList;
import java.util.List;


public class FoodBarChartView implements OnChartValueSelectedListener {

    protected BarChart mChart;
    private Context mContext;

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    public FoodBarChartView(Context context, View v) {
        mContext = context;

        mTfRegular = Typeface.createFromAsset(mContext.getAssets(), "Kelson-Sans-Regular.otf");
        mTfLight = Typeface.createFromAsset(mContext.getAssets(), "Kelson-Sans-Light.otf");

        mChart = (BarChart) v.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setTouchEnabled(false);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        mChart.setDoubleTapToZoomEnabled(false);

        mChart.setDrawGridBackground(false);

        AxisValueFormatter xAxisFormatter = new AxisValueFormatter(TypeDataSet.Period.PERIOD_DAY);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(15);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        // 하단 설명 문구 (The Year 2017)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        // 차트 클릭시 나오는 마커
        XYMarkerView mv = new XYMarkerView(mContext, xAxisFormatter);
        mv.setEnabled(false);

        // XXX DummyData
        // setting data
    }

    public void setXValueFormat(IAxisValueFormatter f) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(f);
    }

    public void invalidate() {
        if (mChart != null)
            mChart.invalidate();
    }


    public void animateXY() {
        mChart.animateXY(500, 500);
    }

    public void animateY() {
        mChart.animateY(500);
    }

    public void setData(List<BarEntry> yVals1) {
        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

            // 그래프 색상 설정
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColor(ColorTemplate.rgb("#F48130"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            // 그래프 두께 및 상단 값 세팅
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            data.setValueFormatter(new BarDataFormatter());

            mChart.setData(data);
        }
    }


    public void setTestData(int count, float range) {

        float start = 0f;   // 시작 위치

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, ContextCompat.getDrawable(mContext, android.R.drawable.btn_star)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");
            set1.setDrawIcons(false);

            // 그래프 색상 설정
            set1.setColor(ColorTemplate.rgb("#1da8d0"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            // 그래프 두께 및 상단 값 세팅
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.RED);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            data.setValueFormatter(new BarDataFormatter());

            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @Override
    public void onValueSelected(CEntry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }


    @Override
    public void onNothingSelected() { }
}
