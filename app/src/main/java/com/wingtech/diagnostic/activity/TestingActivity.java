package com.wingtech.diagnostic.activity;

import android.widget.TextView;

import com.wingtech.diagnostic.R;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class TestingActivity extends BaseActivity {
    TextView mTxtTitle;
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

    }

    @Override
    protected void onWork() {
        String title = getIntent().getStringExtra("title");
        mTxtTitle.setText(title + "ing...");
    }
}
