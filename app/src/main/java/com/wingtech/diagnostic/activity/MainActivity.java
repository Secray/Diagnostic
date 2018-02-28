package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.asusodm.atd.smmitest.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.wingtech.diagnostic.App;
import com.wingtech.diagnostic.service.TemperatureService;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;
import com.wingtech.diagnostic.util.TemperatureFormatter;
import com.wingtech.diagnostic.util.TimeValueFormatter;
import com.wingtech.diagnostic.widget.LoadingView;
import com.wingtech.diagnostic.widget.PercentView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity
        implements View.OnClickListener {
    private static final int MAX_SIZE = 24;
    LineChart mLineChart;
    LineDataSet mCPUDataSet;
    LineDataSet mBatterySet;

    Toolbar mToolbar;
    PercentView mCPUPercent;
    PercentView mBatteryPercent;
    LoadingView mLoadingView;

    TemperatureService mService;

    private HandlerThread mLineChartThread;
    private Handler mLineChartHandler;
    private Handler mHandler = new Handler();

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

        //setData(240, 100);

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
        mLoadingView = (LoadingView) findViewById(R.id.cpu_loading);
        findViewById(R.id.test_all).setOnClickListener(this);
        findViewById(R.id.single_test).setOnClickListener(this);
        //findViewById(R.id.repair).setOnClickListener(this);
        //findViewById(R.id.help).setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onWork() {
        bindService(new Intent(this, TemperatureService.class), mServiceConn, BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, filter);
        buildChart();
        mLineChartThread = new HandlerThread("line-chart-thread");
        mLineChartThread.start();
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "SMMI_TestResult.txt");
        mLineChartHandler = new Handler(mLineChartThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int cpuRate = getCPURateDesc();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initCPUPercent(cpuRate);
                        if (!file.exists() && !App.isAllTest) {
                            SharedPreferencesUtils.setNull(MainActivity.this);
                        }
                        if (mService != null) {
                            setData(mService.getCPUList(), mService.getBatteryList());
                        }
                    }
                });
                if (mLineChartThread.isAlive()) {
                    mLineChartHandler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        };
        mLineChartHandler.sendEmptyMessageDelayed(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
        unregisterReceiver(mBatteryReceiver);
        mLineChartHandler.removeMessages(0);
        mLineChartThread.quit();
    }

    private void setData(LinkedList<Integer> cpuTemps, LinkedList<Integer> batteryTemps) {
        int cpuLen = cpuTemps.size();
        int batteryLen = batteryTemps.size();
        Log.i("cpuLen = " + cpuLen + " batteryLen " + batteryLen);
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        int remainLen = MAX_SIZE - cpuLen;
        for (int i = 0; i < remainLen; i++) {
            values1.add(new Entry(i, 0));
            values2.add(new Entry(i, 0));
        }

        for (int i = remainLen; i < MAX_SIZE; i++) {
            values1.add(new Entry(i, cpuTemps.get(i - remainLen)));
            values2.add(new Entry(i, batteryTemps.get(i - remainLen)));
        }

        Collections.sort(values1, new EntryXComparator());
        Collections.sort(values2, new EntryXComparator());
        // create a dataset and give it a type
        mCPUDataSet = new LineDataSet(values1, "CPU");
        mBatterySet = new LineDataSet(values2, "Battery");

        mCPUDataSet.setColor(getColor(R.color.main_cpu_line_color));
        mCPUDataSet.setDrawValues(false);
        mCPUDataSet.setDrawCircles(false);
        mCPUDataSet.setFormLineWidth(1f);
        mCPUDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        mCPUDataSet.setFormSize(15.f);

        mBatterySet.setColor(getColor(R.color.main_battery_line_color));
        mBatterySet.setDrawValues(false);
        mBatterySet.setDrawCircles(false);
        mBatterySet.setFormLineWidth(1f);
        mBatterySet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        mBatterySet.setFormSize(15.f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(mCPUDataSet); // add the datasets
        dataSets.add(mBatterySet);

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mLineChart.setData(data);
        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.animateX(1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_all:
                App.isAllTest = true;
                startActivity(new Intent(MainActivity.this, TestAllActivity.class));
                break;
            case R.id.single_test:
                startActivity(new Intent(MainActivity.this, SingleTestListActivity.class));
                break;
            /*case R.id.repair:
                startActivity(new Intent(MainActivity.this, RepairActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;*/
        }
    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 0);
            int color;
            int secColor;
            int bgColor;
            if (level >= 50 && level <= 100) {
                color = getColor(R.color.batter_50_100);
                secColor = getColor(R.color.batter_50_100_2);
                bgColor = getColor(R.color.batter_50_100_bg);
            } else if (level < 50 && level >= 20) {
                color = getColor(R.color.battery_20_50);
                secColor = getColor(R.color.battery_20_50_2);
                bgColor = getColor(R.color.battery_20_50_bg);
            } else {
                color = getColor(R.color.battery_10_20);
                secColor = getColor(R.color.battery_10_20_2);
                bgColor = getColor(R.color.battery_10_20_bg);
            }
            mBatteryPercent.setFirstColor(color);
            mBatteryPercent.setSecondColor(secColor);
            mBatteryPercent.setBackgroundColor(bgColor);
            mBatteryPercent.setPercent((float) level / 100);
        }
    };

    private void initCPUPercent(int cpuRate) {
        int color;
        int secColor;
        int bgColor;
        if (cpuRate >= 80 && cpuRate <= 100) {
            color = getColor(R.color.cpu_80_100);
            secColor = getColor(R.color.cpu_80_100_2);
            bgColor = getColor(R.color.cpu_80_100_bg);
        } else if (cpuRate < 80 && cpuRate >= 50) {
            color = getColor(R.color.cpu_50_80);
            secColor = getColor(R.color.cpu_50_80_2);
            bgColor = getColor(R.color.cpu_50_80_bg);
        } else {
            color = getColor(R.color.cpu_0_50);
            secColor = getColor(R.color.cpu_0_50_2);
            bgColor = getColor(R.color.cpu_0_50_bg);
        }
        mCPUPercent.setFirstColor(color);
        mCPUPercent.setSecondColor(secColor);
        mCPUPercent.setBackgroundColor(bgColor);
        mCPUPercent.setPercent((float) cpuRate / 100);

        mLoadingView.setVisibility(View.GONE);
        mCPUPercent.setVisibility(View.VISIBLE);
    }

    private int getCPURateDesc() {
        String path = "/proc/stat";
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;
        FileReader fileReader;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++) {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null
                        && (i == 0 || currentCPUNum < firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu ")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0) {
                        firstCPUNum = currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        //Toast.makeText(this, totalJiffies[0] + " " + totalJiffies[1] + " " + totalIdle[0] + " " + totalIdle[1], Toast.LENGTH_LONG).show();
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) -
                    (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]);
        }

        return (int) (rate * 100);
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService =
                    ((TemperatureService.TemperatureBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("service disconnect");
        }
    };
}
