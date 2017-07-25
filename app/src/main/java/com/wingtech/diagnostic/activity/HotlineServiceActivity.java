package com.wingtech.diagnostic.activity;

import android.support.v7.widget.Toolbar;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;

import butterknife.BindView;

/**
 * @author xiekui
 * @date 2017-7-17
 */

public class HotlineServiceActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hotline;
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
