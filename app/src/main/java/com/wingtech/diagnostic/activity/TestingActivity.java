package com.wingtech.diagnostic.activity;

import android.content.IntentFilter;
import android.view.WindowManager;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.receiver.BluetoothReceiver;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class TestingActivity extends BaseActivity {
    TextView mTxtTitle;
    String mTitle;
    BluetoothReceiver mBluetoothReceiver;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_testing;
    }

    @Override
    protected void initViews() {
        mTxtTitle = (TextView) findViewById(R.id.testing_title);
    }

    @Override
    protected void initToolbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothReceiver != null) {
            unregisterReceiver(mBluetoothReceiver);
        }
    }

    @Override
    protected void onWork() {
        mTitle = getIntent().getStringExtra("title");
        mTxtTitle.setText(mTitle + "ing...");

        switch (mTitle) {
            case "Bluetooth Test":
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
                intentFilter.addAction("android.bluetooth.device.action.FOUND");
                intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
                mBluetoothReceiver = new BluetoothReceiver();
                registerReceiver(mBluetoothReceiver, intentFilter);
                break;
        }
    }
}
