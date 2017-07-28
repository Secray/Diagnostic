package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;

/**
 * @author xiekui
 * @date 2017-7-19
 */

public class SingleTestActivity extends BaseActivity implements OnTitleChangedListener {
    Toolbar mToolbar;
    String[] mTestCases;

    String mTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTestCases = getResources().getStringArray(R.array.test_cases);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onWork() {
        CommonSingleTestFragment fragment = new CommonSingleTestFragment();
        fragment.setTitleChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.test_content,
                fragment).commit();
    }

    @Override
    protected void handleIntent(Intent intent) {
        int pos = intent.getIntExtra("position", 0);
        mTitle = mTestCases[pos];
    }

    @Override
    public String getChangedTitle() {
        return mTitle;
    }
}
