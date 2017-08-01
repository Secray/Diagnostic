package com.wingtech.diagnostic.activity;

import android.app.Service;
import android.telephony.TelephonyManager;

import com.android.helper.Helper;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.SIMCARD_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-31
 */

public class SIMCardTestingActivity extends TestingActivity {
    private int mCurrentIndex;
    @Override
    protected void onWork() {
        super.onWork();
        mRequestCode = SIMCARD_REQUEST_CODE;
        mCurrentIndex = getIntent().getIntExtra("index", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSimNum();
        getSimStatus(mCurrentIndex);
    }

    private String getSystemProperties(String key, String defaultValue) {
        if (key == null)
            return null;
        return Helper.getSystemProperties(key, defaultValue);
    }

    private int getSimNum() {
        int simNum;
        String simConfig = getSystemProperties("persist.radio.multisim.config", null);
        if ("dsds".equals(simConfig) || "dsda".equals(simConfig)) {
            simNum = 2;
        } else if ("tsts".equals(simConfig)) {
            simNum = 3;
        } else {
            simNum = 1;
        }
        Log.d("simNum = " + simNum);
        return simNum;
    }

    private void getSimStatus(int index){
        TelephonyManager mTelephonyManager = (TelephonyManager)getSystemService(Service.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            finish();
            return;
        }

        if(mCurrentIndex == 1){
            if (mTelephonyManager.getSimState() > TelephonyManager.SIM_STATE_ABSENT
                    && mTelephonyManager.getSimState() < Helper.SIM_STATE_NOT_READY) {
                mResult = true;
            }
        } else {
            int state = Helper.getSimState(mTelephonyManager, index);
            Log.d("SIM state=" + state);
            if (state > TelephonyManager.SIM_STATE_ABSENT
                    && state < Helper.SIM_STATE_NOT_READY) {
                mResult = true;
            }
        }
        sendResult();
    }
}
