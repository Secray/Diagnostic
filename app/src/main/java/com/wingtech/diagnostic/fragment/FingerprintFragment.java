package com.wingtech.diagnostic.fragment;

import com.android.helper.Helper;
import com.wingtech.diagnostic.util.Log;

/**
 * Created by xiekui on 17-8-3.
 */

public class FingerprintFragment extends TestFragment {
    @Override
    protected void onWork() {
        super.onWork();
        Log.i("FingerPrint properties "
                + Helper.getSystemProperties("goodix.fp.hardware.ready", "-1"));
        if (!"-1".equals(Helper.getSystemProperties("goodix.fp.hardware.ready", "-1"))) {
            mCallback.onChange(true);
        }
        mCallback.onChange(false);
    }
}
