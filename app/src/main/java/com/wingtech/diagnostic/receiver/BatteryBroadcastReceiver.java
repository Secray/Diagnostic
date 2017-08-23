package com.wingtech.diagnostic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;

import com.wingtech.diagnostic.util.Log;

/**
 * @author xiekui
 * @date 2017-7-29
 */

public class BatteryBroadcastReceiver extends BroadcastReceiver {
    public static final int BATTERY_PASSED = 0;
    public static final int BATTERY_FAILED = 1;
    int mStatus;
    int mLevel;
    int mScale;
    int mHealth;
    int mVoltage;
    int mTemperature;

    Handler mHandler;

    public BatteryBroadcastReceiver(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
        mLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        mScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        mHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        mTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 1000);

        if (mLevel == 0 || mScale == 0 || mHealth == 0
                || mVoltage == 0 || mTemperature == 1000) {
            mHandler.sendEmptyMessage(BATTERY_FAILED);
        } else {
            mHandler.sendEmptyMessage(BATTERY_PASSED);
        }
    }
}
