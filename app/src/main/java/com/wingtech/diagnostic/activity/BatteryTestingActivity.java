package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.wingtech.diagnostic.receiver.BatteryBroadcastReceiver;

import static com.wingtech.diagnostic.receiver.BatteryBroadcastReceiver.BATTERY_FAILED;
import static com.wingtech.diagnostic.receiver.BatteryBroadcastReceiver.BATTERY_PASSED;

/**
 * @author xiekui
 * @date 2017-7-29
 */

public class BatteryTestingActivity extends TestingActivity {
    private BatteryBroadcastReceiver mBatteryBroadcastReceiver;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case BATTERY_PASSED:
                    mResult = true;
                    break;
                case BATTERY_FAILED:
                    mResult = false;
                    break;
            }
            sendResult();
        }
    };

    @Override
    protected void onWork() {
        super.onWork();
        mBatteryBroadcastReceiver = new BatteryBroadcastReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBatteryBroadcastReceiver != null) {
            unregisterReceiver(mBatteryBroadcastReceiver);
        }
        mHandler.removeCallbacksAndMessages(null);
    }
}
