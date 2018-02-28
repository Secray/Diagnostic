package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.App;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.fragment.TestFragment;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;
import com.wingtech.diagnostic.listener.OnTestItemListener;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;
import com.wingtech.diagnostic.util.TestItem;
import com.wingtech.diagnostic.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiekui
 * @date 2017-7-25
 */

public class TestAllActivity extends BaseActivity
        implements OnTestItemListener, OnResultChangedCallback {
    Toolbar mToolbar;
    TextView mPrevious;
    TextView mNext;
    View mRlPrevious;
    View mRlNext;
    TextView mIndicator;
    ViewPager mViewPager;

    int mCurrent = 0;
    String mTitle;
    private boolean mIsFinishing;
    int mLen;
    private List<TestItem> mCaseList;
    private AlertDialog mDialog;
    private List<Fragment> mTestFragments;
    private TestAdapter mTestAdapter;

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
        mViewPager = (ViewPager) findViewById(R.id.test_viewpager);
        mCaseList = Utils.getTestAllCases(App.mItems);
        mTestFragments = new ArrayList<>();
        mTestAdapter = new TestAdapter(getSupportFragmentManager(), this, mTestFragments);
        mViewPager.setAdapter(mTestAdapter);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {
        mLen = mCaseList.size();
        if (mLen > 0)
            doTest();
        /*CommonSingleTestFragment fragment = new CommonSingleTestFragment();
        fragment.setTitleChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.test_content,
                fragment).commit();*/
    }

    @Override
    public void onBackPressed() {
        if (mCurrent < mCaseList.size() - 1) {
            mDialog = showDialog();
            mDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private AlertDialog showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        AlertDialog dlg = builder.create();
        dlg.setCancelable(false);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        TextView mTitle = (TextView) layout.findViewById(R.id.dialog_title);
        mTitle.setText(R.string.test_all_dialog_title);
        mContent.setText(R.string.test_all_dialog_content);
        Button pass = (Button) layout.findViewById(R.id.pass);
        pass.setText(R.string.btn_continue);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dlg.dismiss();
                mIsFinishing = true;
                App.isAllTest = false;
                finish();
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setText(R.string.menu_test_cancel);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doTest();
                dlg.dismiss();
            }
        });

        return dlg;
    }

    void doTest() {
        mIndicator.setText((mCurrent + 1) + "/" + mLen);
        mTitle = mCaseList.get(mCurrent).getName();
        getSupportActionBar().setTitle(mTitle);
        mPrevious.setText(mCurrent == 0 ? "" : mCaseList.get(mCurrent - 1).getName());
        mRlPrevious.setVisibility(mCurrent == 0 ? View.INVISIBLE : View.VISIBLE);
        mNext.setText(mCurrent == mLen - 1 ? "" : mCaseList.get(mCurrent + 1).getName());
        mRlNext.setVisibility(mCurrent == mLen - 1 ? View.INVISIBLE : View.VISIBLE);

        TestFragment fragment = Utils.getFragment(mTitle);
        if (fragment != null) {
            fragment.setOnResultChangedCallback(this);
            fragment.setTitle(mTitle);
            mTestFragments.clear();
            mTestFragments.add(fragment);
            mTestAdapter.update(mTestFragments);
        } else {
            CommonSingleTestFragment commonFragment = new CommonSingleTestFragment();
            commonFragment.setOnResultChangedCallback(this);
            commonFragment.setOnTestItemListener(this);
            mTestFragments.clear();
            mTestFragments.add(commonFragment);
            mTestAdapter.update(mTestFragments);
        }
    }

    @Override
    public void onChange(boolean result) {
        SharedPreferencesUtils.setParam(this, mTitle,
                result ? SharedPreferencesUtils.PASS : SharedPreferencesUtils.FAIL);
        Log.i("mCurrent = " + mCurrent + " " + mTitle + " " + result + " mIsFinishing = " + mIsFinishing);
        if (mCurrent > mCaseList.size() - 1) {
            startActivity(new Intent(this, TestResultActivity.class));
            App.isAllTest = false;
            finish();
        } else {
            if ((mDialog == null || !mDialog.isShowing()) && !mIsFinishing) {
                try {
                    mViewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCurrent++;
                            doTest();
                        }
                    }, 500);
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public TestItem getTestItem() {
        return mCaseList.get(mCurrent);
    }


    class TestAdapter extends FragmentStatePagerAdapter {
        Context mContext;
        List<Fragment> mFragments;

        public TestAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
            super(fm);
            this.mContext = context;
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE;
        }



        @Override
        public int getCount() {
            return mFragments.size();
        }

        public void update(List<Fragment> fragments) {
            this.mFragments = fragments;
            notifyDataSetChanged();
        }
    }
}
