package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.HelpActivity;
import com.wingtech.diagnostic.activity.RepairActivity;
import com.wingtech.diagnostic.adapter.TestResultAdapter;
import com.wingtech.diagnostic.bean.TestCaseResult;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xiekui
 * @date 2017-7-18
 */

public class TestResultActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.result_list)
    RecyclerView mRecyclerView;

    @BindArray(R.array.test_cases)
    String[] mTestCases;

    @BindArray(R.array.test_icons)
    TypedArray mIconArray;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_result;
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.test_item_divider));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(new TestResultAdapter(this, initList()));
    }

    ArrayList<TestCaseResult> initList() {
        int len = mIconArray.length();
        int[] testIcons = new int[len];
        for (int j = 0; j < len; j ++) {
            testIcons[j] = mIconArray.getResourceId(j, 0);
        }
        ArrayList<TestCaseResult> list = new ArrayList<>();
        for (int i = 0; i < mTestCases.length; i ++) {
            TestCaseResult t = new TestCaseResult();
            t.setResult(i % 2 == 0);
            t.setIcon(getDrawable(testIcons[i]));
            t.setTitle(mTestCases[i]);
            list.add(t);
        }
        return list;
    }

    @OnClick({R.id.btn_test_repair, R.id.btn_test_help})
    void onClick(TextView textView) {
        switch (textView.getId()) {
            case R.id.btn_test_help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.btn_test_repair:
                startActivity(new Intent(this, RepairActivity.class));
                break;
        }
    }
}
