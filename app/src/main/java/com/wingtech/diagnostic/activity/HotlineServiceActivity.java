package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asusodm.atd.smmitest.R;


/**
 * @author xiekui
 * @date 2017-7-17
 */

public class HotlineServiceActivity extends BaseActivity implements View.OnClickListener {
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hotline;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.img_vip_phone).setOnClickListener(this);
        findViewById(R.id.img_customer_phone).setOnClickListener(this);
    }

    @Override
    protected void onWork() {

    }

    @Override
    public void onClick(View v) {
        String number = "";
        switch (v.getId()) {
            case R.id.img_customer_phone:
                number = getString(R.string.hotline_service_customer_support_number_value);
                break;
            case R.id.img_vip_phone:
                number = getString(R.string.hotline_service_vip_support_number_value);
                break;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
