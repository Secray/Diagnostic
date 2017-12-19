package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.view.WindowManager;
import android.widget.TextView;

import com.asusodm.atd.smmitest.R;


/**
 * @author xiekui
 * @date 2017-7-28
 */

public abstract class TestingActivity extends BaseActivity {
    protected boolean mResult;
    protected TextView mTxtTitle;
    protected String mTitle;
    protected int mRequestCode;
    private boolean mIsTestAll;

    protected void sendResult() {
        Intent intent = new Intent(this, mIsTestAll ? TestAllActivity.class : SingleTestActivity.class);
        intent.putExtra("result", mResult);
        setResult(mRequestCode, intent);
        finish();
    }

    @Override
    protected void handleIntent(Intent intent) {
        mIsTestAll = intent.getBooleanExtra("isTestAll", false);
    }

    @Override
    protected void onWork() {
        mTitle = getIntent().getStringExtra("title");
        mTxtTitle.setText(mTitle + "ing...");
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
