package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.WIRECHARGKEY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class ChargingActivity extends TestingActivity {

    private static final String TAG = "WireChargActivity";

    private Button mTouchFailBtn = null;

    TextView mVoltage;
    TextView mMessage;

    private int pbatParam0 = 0;
    private int pbatParam1 = 0;
    private int mPlugged;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_charging;
    }

    @Override
    protected void initViews() {
        mVoltage = (TextView) findViewById(R.id.voltage);
        mMessage = (TextView) findViewById(R.id.message_charging);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBatReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(mBatReceiver, localIntentFilter);
    }

    private BroadcastReceiver mBatReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent paramIntent) {
            String strAct = paramIntent.getAction();
            String strCmp = "android.intent.action.BATTERY_CHANGED";

            int local_p = 0;

            if (strCmp.equals(strAct)) {
                pbatParam0 = paramIntent.getIntExtra(BatteryManager.EXTRA_STATUS, local_p);
                pbatParam1 = paramIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, local_p);
                int voltage = paramIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, local_p);

                switch (pbatParam0) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        Log.i(TAG, "charging");
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        Log.i(TAG, "charging full");
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    default:
                        Log.i(TAG, "battery using");
                        break;
                }

                mVoltage.setText(getResources().getString(R.string.voltage, ((float) voltage) / 1000 + ""));
                Log.i(TAG, "pbatParam1 = " +pbatParam1 +","+"mPlugged = " + mPlugged);
                if (pbatParam1 == mPlugged) {
                    Log.i(TAG, " pass pbatParam1 = " +pbatParam1 +","+"mPlugged = " + mPlugged);
                    mResult = true;
                    sendResult();
                }
            }
        }
    };
}
