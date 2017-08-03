package com.wingtech.diagnostic.activity;

import android.view.View;

import com.wingtech.diagnostic.R;

import static com.wingtech.diagnostic.util.Constants.TOUCH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class TouchTestActivity extends TestingActivity implements View.OnClickListener {
    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_touch;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.touch_fail_btn).setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onWork() {
        mRequestCode = TOUCH_REQUEST_CODE;
    }

    @Override
    public void onClick(View v) {
        mResult = false;
        sendResult();
    }
}
