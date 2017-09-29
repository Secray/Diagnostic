package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.wingtech.diagnostic.App;
import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.listener.OnTestItemListener;
import com.wingtech.diagnostic.util.TestItem;

/**
 * @author xiekui
 * @date 2017-7-19
 */

public class SingleTestActivity extends BaseActivity implements OnTestItemListener {
    Toolbar mToolbar;
    String[] mTestCases;
    int mPos = -1;
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
        fragment.setOnTestItemListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.test_content,
                fragment).commit();
    }

    @Override
    protected void handleIntent(Intent intent) {
        mPos = intent.getIntExtra("position", 0);
        mTitle = App.mItems.get(mPos).getName();
    }

    @Override
    public TestItem getTestItem() {
        return App.mItems.get(mPos);
    }
}
