package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * @author xiekui
 * @date 2017-7-19
 */

public class SingleTestActivity extends BaseActivity implements OnTitleChangedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindArray(R.array.test_cases)
    String[] mTestCases;

    String mTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test;
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
