package com.wingtech.diagnostic.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.wingtech.diagnostic.receiver.BluetoothReceiver;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_DEVICE_FOUND;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_DISCOVERY_FINISHED;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_DISCOVERY_STARTED;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_STATE_CHANGED;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class BluetoothTestingActivity extends TestingActivity {
    BluetoothReceiver mBluetoothReceiver;
    BluetoothDevice mDevice;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case BluetoothReceiver.BLUETOOTH_DEVICE_FOUND:
                    Log.i("BLUETOOTH DEVICE FOUND");
                    mDevice = (BluetoothDevice) msg.obj;
                    if (mDevice != null) {
                        Log.i("device name = " + mDevice.getName()
                                + " address = " + mDevice.getAddress());
                        mBluetoothAdapter.cancelDiscovery();
                    }
                    break;
                case BluetoothReceiver.BLUETOOTH_DISCOVERY_FINISHED:
                    Log.i("BLUETOOTH DISCOVERY FINISHED");
                    mResult = mDevice != null;
                    sendResult();
                    break;
                case BluetoothReceiver.BLUETOOTH_DISCOVERY_STARTED:
                    Log.i("BLUETOOTH DISCOVERY STARTED");
                    boolean isDiscovering = (boolean) msg.obj;
                    if (!isDiscovering) {
                        mResult = false;
                        sendResult();
                    }
                    break;
                case BluetoothReceiver.BLUETOOTH_STATE_CHANGED:
                    Log.i("BLUETOOTH STATE CHANGED");
                    boolean isOpen = (boolean) msg.obj;
                    if (isOpen) {
                        mBluetoothAdapter.startDiscovery();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothReceiver != null) {
            unregisterReceiver(mBluetoothReceiver);
            mBluetoothAdapter.disable();
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        sendResult();
    }

    @Override
    protected void onWork() {
        mTitle = getIntent().getStringExtra("title");
        mTxtTitle.setText(mTitle + "ing...");

        switch (mTitle) {
            case "Bluetooth Test":
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BLUETOOTH_STATE_CHANGED);
                intentFilter.addAction(BLUETOOTH_DISCOVERY_STARTED);
                intentFilter.addAction(BLUETOOTH_DEVICE_FOUND);
                intentFilter.addAction(BLUETOOTH_DISCOVERY_FINISHED);
                mBluetoothReceiver = new BluetoothReceiver(mBluetoothAdapter, mHandler);
                registerReceiver(mBluetoothReceiver, intentFilter);
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.startDiscovery();
                } else {
                    mBluetoothAdapter.enable();
                }
                break;
        }
    }
}
