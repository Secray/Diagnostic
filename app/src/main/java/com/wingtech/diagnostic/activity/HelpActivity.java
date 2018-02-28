package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asusodm.atd.smmitest.R;


/**
 * @author xiekui
 -* @date 2017-7-12
 */

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.txt_hotline_service).setOnClickListener(this);
        findViewById(R.id.txt_send_repair_request).setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_hotline_service:
                startActivity(new Intent(this, HotlineServiceActivity.class));
                break;
            case R.id.txt_send_repair_request:
                startActivity(new Intent(this, SendRepairRequestActivity.class));
                break;
        }
    }
}
