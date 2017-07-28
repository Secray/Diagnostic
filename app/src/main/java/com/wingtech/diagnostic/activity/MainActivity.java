package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
import com.wingtech.diagnostic.widget.PercentView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    LineChart mLineChart;
    Toolbar mToolbar;
    PercentView mCPUPercent;
    PercentView mBatteryPercent;

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
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLineChart = (LineChart) findViewById(R.id.chart);
        mCPUPercent = (PercentView) findViewById(R.id.cpu_percent);
        mBatteryPercent = (PercentView) findViewById(R.id.battery_percent);
        findViewById(R.id.test_all).setOnClickListener(this);
        findViewById(R.id.single_test).setOnClickListener(this);
        findViewById(R.id.repair).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onWork() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, filter);
        buildChart();
        mCPUAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatteryReceiver);
        mCPUAsyncTask.cancel(true);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 0);
            int color;
            int bgColor;
            if (level >= 50 && level <= 100) {
                color = getColor(R.color.batter_50_100);
                bgColor = getColor(R.color.batter_50_100_bg);
            } else if (level < 50 && level >=20) {
                color = getColor(R.color.battery_20_50);
                bgColor = getColor(R.color.battery_20_50_bg);
            } else {
                color = getColor(R.color.battery_10_20);
                bgColor = getColor(R.color.battery_10_20_bg);
            }
            mBatteryPercent.setFirstColor(color);
            mBatteryPercent.setSecondColor(bgColor);
            mBatteryPercent.setPercent((float) level / scale);
        }
    };


    private int getProcessCpuRate() {
        int rate = 0;

        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUUsr = Result.split("%");
                    String[] CPUUsage = CPUUsr[0].split("User");
                    String[] SYSUsage = CPUUsr[1].split("System");

                    rate = Integer.parseInt(CPUUsage[1].trim()) + Integer.parseInt(SYSUsage[1].trim());
                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rate;
    }

    AsyncTask<Void, Void, Integer> mCPUAsyncTask = new AsyncTask<Void, Void, Integer>() {
        @Override
        protected Integer doInBackground(Void... params) {
            return getProcessCpuRate();
        }

        @Override
        protected void onPostExecute(Integer value) {
            int color;
            int bgColor;
            if (value >= 80 && value <= 100) {
                color = getColor(R.color.cpu_80_100);
                bgColor = getColor(R.color.cpu_80_100_bg);
            } else if (value < 80 && value >= 50) {
                color = getColor(R.color.cpu_50_80);
                bgColor = getColor(R.color.cpu_50_80_bg);
            } else {
                color = getColor(R.color.cpu_0_50);
                bgColor = getColor(R.color.cpu_0_50_bg);
            }
            mCPUPercent.setFirstColor(bgColor);
            mCPUPercent.setSecondColor(color);
            mCPUPercent.setPercent((float) value / 100);
        }
    };
}
