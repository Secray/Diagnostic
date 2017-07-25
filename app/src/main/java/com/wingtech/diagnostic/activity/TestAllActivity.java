package com.wingtech.diagnostic.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xiekui
 * @date 2017-7-25
 */

public class TestAllActivity extends BaseActivity implements OnTitleChangedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindArray(R.array.test_cases)
    String[] mTestCases;
    @BindView(R.id.text_previous)
    TextView mPrevious;
    @BindView(R.id.text_next)
    TextView mNext;
    @BindView(R.id.rl_previous)
    View mRlPrevious;
    @BindView(R.id.rl_next)
    View mRlNext;
    @BindView(R.id.text_indicator)
    TextView mIndicator;

    int mCurrent = 0;
    String mTitle;
    int mLen;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_all;
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

    @OnClick({R.id.rl_previous, R.id.rl_next})
    void onClick(View view) {
        switch (view.getId()) {
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
}
