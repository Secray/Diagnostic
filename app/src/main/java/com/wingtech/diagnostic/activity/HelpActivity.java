package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.wingtech.diagnostic.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xiekui
 -* @date 2017-7-12
 */

public class HelpActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onWork() {

    }

    @OnClick({R.id.txt_hotline_service, R.id.txt_send_repair_request})
    void click(TextView textView) {
        switch (textView.getId()) {
            case R.id.txt_hotline_service:
                startActivity(new Intent(this, HotlineServiceActivity.class));
                break;
            case R.id.txt_send_repair_request:
                startActivity(new Intent(this, SendRepairRequestActivity.class));
                break;
        }
    }
}
