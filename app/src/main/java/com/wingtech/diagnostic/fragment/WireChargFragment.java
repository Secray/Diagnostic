package com.wingtech.diagnostic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.SingleTestActivity;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.WIRECHARGKEY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class WireChargFragment extends TestFragment {

    private static final String TAG = "WireChargActivity";

    private CheckBox mWireChargKey = null;
    private TextView mWireChargKeyTxt = null;
    private TextView mTitle = null;
    private Button mTouchFailBtn = null;

    private int pbatParam0 = 0;
    private int pbatParam1 = 0;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViewEvents(View view) {
        mWireChargKey = (CheckBox) view.findViewById(R.id.box_txt_1);
        mWireChargKeyTxt = (TextView) view.findViewById(R.id.txt_box_1);
        mTitle = (TextView) view.findViewById(R.id.activity_checkbox_title);
        mTouchFailBtn = (Button) view.findViewById(R.id.touch_fail_btn);
        mWireChargKey.setVisibility(View.VISIBLE);
        mWireChargKeyTxt.setVisibility(View.VISIBLE);
        myWork();
    }


    private void myWork() {
        mTitle.setText(R.string.wirechargkey_title);
        mWireChargKeyTxt.setText(R.string.wirechargkey_txt);

        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onChange(false);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mActivity.unregisterReceiver(mBatReceiver);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        mActivity.registerReceiver(mBatReceiver, localIntentFilter);
    }
    private BroadcastReceiver mBatReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent paramIntent) {
            String strAct = paramIntent.getAction();
            String strCmp = "android.intent.action.BATTERY_CHANGED";

            int local_p = 0;

            if(strAct.equals(strCmp)){
                pbatParam0 = paramIntent.getIntExtra(BatteryManager.EXTRA_STATUS, local_p);
                pbatParam1 = paramIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, local_p);

                switch(pbatParam0){
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        Log.i(TAG,"charging");
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        Log.i(TAG,"charging full");
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    default:
                        Log.i(TAG, "battery using");
                        break;
                }
                if(pbatParam1==BatteryManager.BATTERY_PLUGGED_USB){
                    Log.i(TAG, "USB connectting");
                    mWireChargKey.setChecked(true);
                    mCallback.onChange(true);
                }
                else if(pbatParam1==BatteryManager.BATTERY_PLUGGED_AC){
                    Log.i(TAG, "charger connectting");
                    mWireChargKey.setChecked(true);
                    mCallback.onChange(true);
                }

                if(pbatParam0 == BatteryManager.BATTERY_STATUS_CHARGING ||
                        pbatParam0 == BatteryManager.BATTERY_STATUS_FULL){
                    mWireChargKey.setChecked(true);
                    mCallback.onChange(true);

                }
            }
        }
    };
}
