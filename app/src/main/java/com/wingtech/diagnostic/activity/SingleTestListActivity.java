package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.App;
import com.wingtech.diagnostic.adapter.SingleTestAdapter;
import com.wingtech.diagnostic.bean.SingleTestCase;
import com.wingtech.diagnostic.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * @author xiekui
 * @date 2017-7-19
 */

public class SingleTestListActivity extends BaseActivity implements OnItemClickListener {
    Toolbar mToolbar;

    RecyclerView mRecyclerView;

    SingleTestAdapter adapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_test_list;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.single_list);
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
        adapter = new SingleTestAdapter(this, initList());
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    ArrayList<SingleTestCase> initList() {
        ArrayList<SingleTestCase> list = new ArrayList<>();
        int size = App.mItems.size();
        for (int i = 0; i < size; i++) {
            SingleTestCase t = new SingleTestCase();
            t.setTitle(App.mItems.get(i).getName());
            t.setIcon(getDrawable(App.mItems.get(i).getIcon()));
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

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void handleTestResult() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }

    }
}
