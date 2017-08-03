package com.wingtech.diagnostic.fragment;

/**
 * Created by xiekui on 17-8-3.
 */

public class FingerprintFragment extends TestFragment {
    @Override
    protected void onWork() {
        super.onWork();
        mCallback.onChange(false);
    }
}
