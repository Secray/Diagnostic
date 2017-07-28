package com.wingtech.diagnostic.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;

/**
 * @author xiekui
 * @date 2017-7-25
 */

public class TestAllActivity extends BaseActivity implements OnTitleChangedListener, View.OnClickListener {
    Toolbar mToolbar;
    String[] mTestCases;
    TextView mPrevious;
    TextView mNext;
    View mRlPrevious;
    View mRlNext;
    TextView mIndicator;

    int mCurrent = 0;
    String mTitle;
    int mLen;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_all;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPrevious = (TextView) findViewById(R.id.text_previous);
        mNext = (TextView) findViewById(R.id.text_next);
        mRlPrevious = findViewById(R.id.rl_previous);
        mRlNext = findViewById(R.id.rl_next);
        mIndicator = (TextView) findViewById(R.id.text_indicator);
        mTestCases = getResources().getStringArray(R.array.test_cases);

        mRlNext.setOnClickListener(this);
        mRlPrevious.setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {
        mLen = mTestCases.length;
        doTest();
        CommonSingleTestFragment fragment = new CommonSingleTestFragment();
        fragment.setTitleChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.test_content,
                fragment).commit();
    }

    void doTest() {
        mIndicator.setText((mCurrent + 1) + "/" + mLen);
        mTitle = mTestCases[mCurrent];
        getSupportActionBar().setTitle(mTitle);
        mPrevious.setText(mCurrent == 0 ? "": mTestCases[mCurrent - 1]);
        mRlPrevious.setVisibility(mCurrent == 0 ? View.INVISIBLE : View.VISIBLE);
        mNext.setText(mCurrent == mLen - 1 ? "": mTestCases[mCurrent + 1]);
        mRlNext.setVisibility(mCurrent == mTestCases.length - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public String getChangedTitle() {
        return mTitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_previous:
                mCurrent --;
                doTest();
                break;
            case R.id.rl_next:
                mCurrent ++;
                doTest();
                break;
        }
    }
}
