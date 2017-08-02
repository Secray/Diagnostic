package com.wingtech.diagnostic.fragment;

import android.view.View;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;

/**
 * @author xiekui
 * @date 2017-8-2
 */

public abstract class TestFragment extends BaseFragment {
    protected boolean mResult;
    protected TextView mTxtTitle;
    protected String mTitle;

    protected OnResultChangedCallback mCallback;

    @Override
    public void onResume() {
        super.onResume();
        onWork();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.content_testing;
    }

    @Override
    protected void initViewEvents(View view) {
        mTxtTitle = (TextView) view.findViewById(R.id.testing_title);
    }

    protected void onWork() {
        mTxtTitle.setText(mTitle + "ing...");
    }

    public void setOnResultChangedCallback(OnResultChangedCallback changedCallback) {
        this.mCallback = changedCallback;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
