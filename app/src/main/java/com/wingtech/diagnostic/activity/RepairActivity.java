package com.wingtech.diagnostic.activity;

import android.support.v7.widget.Toolbar;

import com.asus.atd.smmitest.R;

/**
 * @author xiekui
 * @date 2017-7-12
 */

public class RepairActivity extends BaseActivity {
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_repair;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {

    }
}
