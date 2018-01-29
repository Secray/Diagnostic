package com.wingtech.diagnostic.fragment;

import com.android.helper.Helper;

/**
 * Created by xiekui on 17-8-2.
 */

public class ModemTestingFragment extends TestFragment {
    @Override
    protected void onWork() {
        super.onWork();
        mCallback.onChange(!"".equals(Helper.getSystemProperties("persist.radio.ver_info", "value")));
    }
}
