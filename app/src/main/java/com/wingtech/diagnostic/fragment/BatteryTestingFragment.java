package com.wingtech.diagnostic.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.wingtech.diagnostic.receiver.BatteryBroadcastReceiver;

import static com.wingtech.diagnostic.receiver.BatteryBroadcastReceiver.BATTERY_FAILED;
import static com.wingtech.diagnostic.receiver.BatteryBroadcastReceiver.BATTERY_PASSED;

/**
 * Created by xiekui on 17-8-2.
 */

public class BatteryTestingFragment extends TestFragment {
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
            mCallback.onChange(mResult);
        }
    };

    @Override
    protected void onWork() {
        super.onWork();
        mBatteryBroadcastReceiver = new BatteryBroadcastReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mActivity.registerReceiver(mBatteryBroadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBatteryBroadcastReceiver != null) {
            mActivity.unregisterReceiver(mBatteryBroadcastReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
