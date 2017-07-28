package com.wingtech.diagnostic.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.wingtech.diagnostic.util.Constants;
import com.wingtech.diagnostic.util.Log;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class BluetoothReceiver extends BroadcastReceiver {
    public static final int BLUETOOTH_STATE_CHANGED = 0;
    public static final int BLUETOOTH_DISCOVERY_STARTED = 1;
    public static final int BLUETOOTH_DISCOVERY_FINISHED = 2;
    public static final int BLUETOOTH_DEVICE_FOUND = 3;
    private BluetoothAdapter mAdapter;
    private Handler mHandler;
    public BluetoothReceiver(BluetoothAdapter adapter, Handler handler) {
        mAdapter = adapter;
        mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Message msg = Message.obtain();
        switch (action) {
            case Constants.BLUETOOTH_DEVICE_FOUND:
                msg.obj = intent.<BluetoothDevice>getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                msg.what = BLUETOOTH_DEVICE_FOUND;
                break;
            case Constants.BLUETOOTH_DISCOVERY_FINISHED:
                msg.what = BLUETOOTH_DISCOVERY_FINISHED;
                msg.obj = null;
                break;
            case Constants.BLUETOOTH_DISCOVERY_STARTED:
                msg.what = BLUETOOTH_DISCOVERY_STARTED;
                msg.obj = mAdapter.isDiscovering();
                break;
            case Constants.BLUETOOTH_STATE_CHANGED:
                msg.obj = mAdapter.isEnabled();
                Log.i("enable = " + mAdapter.isEnabled());
                msg.what = BLUETOOTH_STATE_CHANGED;
                break;
        }
        mHandler.sendMessage(msg);
    }
}
