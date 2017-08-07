package com.wingtech.diagnostic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.wingtech.diagnostic.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by xiekui on 17-8-4.
 */

public class TemperatureService extends Service {
    private HandlerThread mTemperatureThread;
    private Handler mTemperatureHandler;

    private LinkedList<Integer> mCPUList;
    private LinkedList<Integer> mBatteryList;

    public LinkedList<Integer> getCPUList() {
        return mCPUList;
    }

    public LinkedList<Integer> getBatteryList() {
        return mBatteryList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCPUList = new LinkedList<>();
        mBatteryList = new LinkedList<>();
        initBackThread();
        mTemperatureHandler.sendEmptyMessageDelayed(0, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTemperatureThread.quit();
    }

    private void initBackThread() {
        mTemperatureThread = new HandlerThread("get-cpu-temperature");
        mTemperatureThread.start();
        mTemperatureHandler = new Handler(mTemperatureThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int cpuTemp =
                        readTemperature("cat /sys/devices/virtual/thermal/thermal_zone11/temp");
                int batteryTemp =
                        readTemperature("cat /sys/devices/virtual/thermal/thermal_zone19/temp");
                Log.i("cpu temperature = " + cpuTemp + " battery temperature = " + batteryTemp);
                if (mBatteryList.size() >= 24) {
                    mBatteryList.poll();
                    mCPUList.poll();
                }
                mBatteryList.offer(batteryTemp);
                mCPUList.offer(cpuTemp);

                mTemperatureHandler.sendEmptyMessageDelayed(0, 10000);
            }
        };
    }

    private int readTemperature(String command) {
        BufferedReader br = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = br.readLine().trim();
            int temp = Integer.parseInt(line);
            if (temp > 1000) {
                temp = temp / 1000;
            }
            if (temp > 100 && temp <= 999) {
                temp = temp / 10;
            }
            return temp;
        } catch (IOException e) {
            return -1;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException excep) {
                Log.e("can't close file" + excep);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new TemperatureBinder();
    }

    public class TemperatureBinder extends Binder {
        public TemperatureService getService() {
            return TemperatureService.this;
        }
    }
}
