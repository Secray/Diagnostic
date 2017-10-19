package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wingtech.diagnostic.App;
import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.fragment.CommonSingleTestFragment;
import com.wingtech.diagnostic.fragment.TestFragment;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;
import com.wingtech.diagnostic.listener.OnTestItemListener;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;
import com.wingtech.diagnostic.util.TestItem;
import com.wingtech.diagnostic.util.Utils;

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

    int mCurrent = 0;
    String mTitle;
    int mLen;
    private List<TestItem> mCaseList;
    private AlertDialog mDialog;

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
        mCaseList = Utils.getTestAllCases(App.mItems);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {
        mLen = mCaseList.size();
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

    private AlertDialog showDialog(){
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
            getSupportFragmentManager().beginTransaction().replace(R.id.test_content,
                    fragment).commit();
        } else {
            CommonSingleTestFragment commonFragment = new CommonSingleTestFragment();
            commonFragment.setOnResultChangedCallback(this);
            commonFragment.setOnTestItemListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.test_content,
                    commonFragment).commit();
        }
    }

    @Override
    public void onChange(boolean result) {
        SharedPreferencesUtils.setParam(this, mTitle,
                result ? SharedPreferencesUtils.PASS : SharedPreferencesUtils.FAIL);
        Log.i("mCurrent = " + mCurrent + " " + mTitle + " " + result);
        mCurrent ++;
        if (mCurrent > mCaseList.size() - 1) {
            startActivity(new Intent(this, TestResultActivity.class));
            finish();
        } else {
            if (mDialog == null || !mDialog.isShowing()) {
                doTest();
            }
        }
    }

    @Override
    public TestItem getTestItem() {
        return mCaseList.get(mCurrent);
    }
}
