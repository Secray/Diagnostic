package com.wingtech.diagnostic.activity;

import android.content.res.TypedArray;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.App;
import com.wingtech.diagnostic.adapter.TestResultAdapter;
import com.wingtech.diagnostic.bean.TestCaseResult;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * @author xiekui
 * @date 2017-7-18
 */

public class TestResultActivity extends BaseActivity {
    Toolbar mToolbar;

    RecyclerView mRecyclerView;

    String[] mTestCases;

    TypedArray mIconArray;
    TestResultAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_result;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTestCases = getResources().getStringArray(R.array.test_cases);
        mRecyclerView = (RecyclerView) findViewById(R.id.result_list);
        mIconArray = getResources().obtainTypedArray(R.array.test_icons);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {
    		SharedPreferencesUtils.deleteFile();
    		SharedPreferencesUtils.outputFile(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.test_item_divider));
        mRecyclerView.addItemDecoration(itemDecoration);
        adapter = new TestResultAdapter(this, initList());
        mRecyclerView.setAdapter(adapter);
    }

    ArrayList<TestCaseResult> initList() {
        ArrayList<TestCaseResult> list = new ArrayList<>();
        int size = App.mItems.size();
        for (int i = 0; i < size; i++) {
            TestCaseResult t = new TestCaseResult();
            t.setTitle(App.mItems.get(i).getName());
            t.setIcon(getDrawable(App.mItems.get(i).getIcon()));
            list.add(t);
        }
        return list;
    }

    @Override
    protected void handleTestResult() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
