package com.wingtech.diagnostic.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.asus.atd.smmitest.R;


/**
 * @author xiekui
 * @date 2017-7-20
 */

public class LoadingDialog extends BaseDialog {
    TextView mTxtTitle;

    private String mTitle;
    public LoadingDialog(@NonNull Context context, String title) {
        super(context);
        this.mTitle = title;
    }

    @Override
    protected int getContentResId() {
        return R.layout.content_testing;
    }

    @Override
    protected void initView() {
        mTxtTitle = (TextView) findViewById(R.id.testing_title);
    }

    @Override
    protected void doWork() {
        mTxtTitle.setText(mTitle + "ing...");
    }
}
