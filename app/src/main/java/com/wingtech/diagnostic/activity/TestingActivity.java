package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.view.WindowManager;
import android.widget.TextView;

import com.wingtech.diagnostic.R;

import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public abstract class TestingActivity extends BaseActivity {
    protected boolean mResult;
    protected TextView mTxtTitle;
    protected String mTitle;

    protected void sendResult() {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("result", mResult);
        setResult(BLUETOOTH_REQUEST_CODE, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        sendResult();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.content_testing;
    }

    @Override
    protected void initViews() {
        mTxtTitle = (TextView) findViewById(R.id.testing_title);
    }

    @Override
    protected void initToolbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
