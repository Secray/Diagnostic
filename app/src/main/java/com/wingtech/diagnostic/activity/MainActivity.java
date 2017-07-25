package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.TemperatureFormatter;
import com.wingtech.diagnostic.util.TimeValueFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.chart)
    LineChart mLineChart;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private void buildChart() {
        mLineChart.setDrawGridBackground(false);

        // no description text
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(false);
        mLineChart.setNoDataText("No Data");
        mLineChart.setExtraBottomOffset(10);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(5, true);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(14);
        xAxis.setValueFormatter(new TimeValueFormatter());
        xAxis.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
        xAxis.setTextColor(getColor(R.color.main_text_color));
        xAxis.setGranularity(1f);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setAxisMaximum(100);
        leftAxis.setAxisMinimum(0);
        leftAxis.setLabelCount(5, true);
        leftAxis.setTextSize(14);
        leftAxis.setTextColor(getColor(R.color.main_text_color));
        leftAxis.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
        leftAxis.setValueFormatter(new TemperatureFormatter());
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawAxisLine(true);

        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);

        setData(240, 100);

        mLineChart.animateX(1000);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onWork() {
        buildChart();
    }

    private void setData(int count, float range) {
        ArrayList<Entry> values1 = new ArrayList<>();
        for (int i = 0; i <= count; i += 10) {
            float val = (float) (Math.random() * range) + 3;
            values1.add(new Entry(i, val));
        }

        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 0; i <= count; i += 10) {
            float val = (float) (Math.random() * range) + 3;
            values2.add(new Entry(i, val));
        }

        LineDataSet set1;
        LineDataSet set2;

        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mLineChart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values1, "CPU");
            set2 = new LineDataSet(values2, "Battery");

            set1.setColor(getColor(R.color.main_cpu_line_color));
            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            set2.setColor(getColor(R.color.main_battery_line_color));
            set2.setDrawValues(false);
            set2.setDrawCircles(false);
            set2.setFormLineWidth(1f);
            set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set2.setFormSize(15.f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mLineChart.setData(data);
        }
    }

    @OnClick({R.id.test_all, R.id.single_test, R.id.repair, R.id.help})
    void actionClick(TextView txt) {
        switch (txt.getId()) {
            case R.id.test_all:
                startActivity(new Intent(MainActivity.this, TestAllActivity.class));
                break;
            case R.id.single_test:
                startActivity(new Intent(MainActivity.this, SingleTestListActivity.class));
                break;
            case R.id.repair:
                startActivity(new Intent(MainActivity.this, RepairActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
        }
    }
}
