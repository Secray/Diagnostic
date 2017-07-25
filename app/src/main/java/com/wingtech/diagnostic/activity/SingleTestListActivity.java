package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wingtech.diagnostic.listener.OnItemClickListener;
import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.adapter.SingleTestAdapter;
import com.wingtech.diagnostic.bean.SingleTestCase;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * @author xiekui
 * @date 2017-7-19
 */

public class SingleTestListActivity extends BaseActivity implements OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.single_list)
    RecyclerView mRecyclerView;

    @BindArray(R.array.test_cases)
    String[] mTestCases;

    @BindArray(R.array.test_icons)
    TypedArray mIconArray;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_test_list;
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
        SingleTestAdapter adapter = new SingleTestAdapter(this, initList());
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    ArrayList<SingleTestCase> initList() {
        int len = mIconArray.length();
        int[] testIcons = new int[len];
        for (int j = 0; j < len; j ++) {
            testIcons[j] = mIconArray.getResourceId(j, 0);
        }
        ArrayList<SingleTestCase> list = new ArrayList<>();
        for (int i = 0; i < mTestCases.length; i ++) {
            SingleTestCase t = new SingleTestCase();
            t.setPassed(i % 2 == 0);
            t.setIcon(getDrawable(testIcons[i]));
            t.setTitle(mTestCases[i]);
            list.add(t);
        }
        return list;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
