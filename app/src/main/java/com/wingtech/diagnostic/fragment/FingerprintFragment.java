package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import com.android.helper.Helper;
import com.wingtech.diagnostic.util.Log;

/**
 * Created by xiekui on 17-8-3.
 */

public class FingerprintFragment extends TestFragment {
    FingerprintManager mFingerprintManager;
    @Override
    protected void onWork() {
        super.onWork();
        mFingerprintManager = (FingerprintManager) mActivity.getSystemService(Context.FINGERPRINT_SERVICE);
        Log.i("FingerPrint properties "
                + Helper.getSystemProperties("goodix.fp.hardware.ready", "-1"));
        if (!"-1".equals(Helper.getSystemProperties("goodix.fp.hardware.ready", "-1"))
                && mFingerprintManager.isHardwareDetected()) {
            mCallback.onChange(true);
        }
        mCallback.onChange(false);
    }
}
