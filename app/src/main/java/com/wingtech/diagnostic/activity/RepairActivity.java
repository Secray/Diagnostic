package com.wingtech.diagnostic.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;

import butterknife.BindView;

/**
 * @author xiekui
 * @date 2017-7-12
 */

public class RepairActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_repair;
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
