package com.wingtech.diagnostic.fragment;

import android.app.Service;
import android.telephony.TelephonyManager;

import com.android.helper.Helper;
import com.wingtech.diagnostic.util.Log;

/**
 * Created by xiekui on 17-8-2.
 */

public class SIMCardTestingFragment extends TestFragment {
    private int mCurrentIndex;

    public SIMCardTestingFragment(int index) {
        mCurrentIndex = index;
    }

    @Override
    protected void onWork() {
        super.onWork();
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

    private void getSimStatus(int index) {
        TelephonyManager mTelephonyManager =
                (TelephonyManager) mActivity.getSystemService(Service.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            mResult = false;
        }

        int state = Helper.getSimState(mTelephonyManager, index);
        Log.d("SIM state=" + state);
        if (state > TelephonyManager.SIM_STATE_ABSENT
                && state < Helper.SIM_STATE_NOT_READY) {
            mResult = true;
        }

        mCallback.onChange(mResult);
    }
}
