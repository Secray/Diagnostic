package com.wingtech.diagnostic.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.wingtech.diagnostic.R;

import butterknife.BindView;

/**
 * @author xiekui
 * @date 2017-7-20
 */

public class LoadingDialog extends BaseDialog {
    @BindView(R.id.testing_title)
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
    protected void doWork() {
        mTxtTitle.setText(mTitle + "ing...");
    }
}
