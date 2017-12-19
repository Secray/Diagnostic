package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
import static com.wingtech.diagnostic.util.Constants.WIRECHARGKEY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class ChargingActivity extends TestingActivity {

    private static final String TAG = "WireChargActivity";

    private Button mTouchFailBtn = null;

    TextView mVoltage;
    TextView mMessage;
    CheckBox mMessageBox;
    CheckBox mNotChargingBox;

    private int mPlugged;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_charging;
    }

    @Override
    protected void initViews() {
        getWindow().setType(TYPE_SYSTEM_ERROR);
        mVoltage = (TextView) findViewById(R.id.voltage);
        mMessage = (TextView) findViewById(R.id.message_charging);
        mMessageBox = (CheckBox) findViewById(R.id.box_txt_1);
        mNotChargingBox = (CheckBox) findViewById(R.id.box_txt_2);
        mTouchFailBtn = (AppCompatButton) findViewById(R.id.fail_btn);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = WIRECHARGKEY_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mPlugged = getIntent().getIntExtra("plugged", 1);
        Log.i(TAG, "mPlugged = " + mPlugged);
        String type = "AC";
        switch (mPlugged) {
            case 1:
                type = getResources().getString(R.string.ac);
                break;
            case 2:
                type = getResources().getString(R.string.usb);
                break;
            case 4:
                type = getResources().getString(R.string.wireless);
                break;
        }
        mMessage.setText(getResources().getString(R.string.message_charger, type));
        mVoltage.setText(getResources().getString(R.string.voltage, "4.033"));
        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        localIntentFilter.addAction("android.hardware.usb.action.USB_STATE");
        registerReceiver(mBatReceiver, localIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "isActive");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "not Active");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatReceiver);

    }

    private BroadcastReceiver mBatReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent paramIntent) {
            String strAct = paramIntent.getAction();
            String strCmp = "android.intent.action.BATTERY_CHANGED";

            int local_p = 0;

            if (strCmp.equals(strAct)) {
                int pbatParam1 = paramIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, local_p);
                int voltage = paramIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, local_p);

                mVoltage.setText(getResources().getString(R.string.voltage, ((float) voltage) / 1000 + ""));
                Log.i(TAG, "pbatParam1 = " + pbatParam1 + "," + "mPlugged = " + mPlugged);
                if (pbatParam1 == mPlugged) {
                    Log.i(TAG, " pass pbatParam1 = " + pbatParam1 + "," + "mPlugged = " + mPlugged);
                    mMessageBox.setVisibility(View.VISIBLE);
                    mMessageBox.setChecked(true);
                }

                if (pbatParam1 == 0 && mPlugged != 2) {
                    if (mMessageBox.isChecked()) {
                        mNotChargingBox.setVisibility(View.VISIBLE);
                        mNotChargingBox.setChecked(true);
                        mResult = true;
                        sendResult();
                    }
                }
            }

            // 由于usb插拔会导致弹出是否传输文件的对话框，点击确认会影响充电结果，所有这里通过usb连接来判断是否断开
            if (mPlugged == 2 && "android.hardware.usb.action.USB_STATE".equals(strAct)) {
                boolean connected = paramIntent.getBooleanExtra("connected", false);
                Log.i(TAG, "usb connected = " + connected);
                if (!connected && mMessageBox.isChecked()) {
                    mNotChargingBox.setVisibility(View.VISIBLE);
                    mNotChargingBox.setChecked(true);
                    mResult = true;
                    sendResult();
                }
            }
        }
    };
}
