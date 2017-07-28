package com.wingtech.diagnostic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wingtech.diagnostic.util.Constants;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Constants.BLUETOOTH_DEVICE_FOUND:
                break;
            case Constants.BLUETOOTH_DISCOVERY_FINISHED:
                break;
            case Constants.BLUETOOTH_DISCOVERY_STARTED:
                break;
            case Constants.BLUETOOTH_STATE_CHANGED:
                break;
        }
    }
}
