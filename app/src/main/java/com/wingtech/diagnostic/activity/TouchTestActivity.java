package com.wingtech.diagnostic.activity;

import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.widget.TPTestView;

import static com.wingtech.diagnostic.util.Constants.TOUCH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class TouchTestActivity extends TestingActivity implements TPTestView.OnCallback {
    TPTestView lpwv;
    AppCompatButton mFail = null;

    @Override
    protected void onWork() {
        mRequestCode = TOUCH_REQUEST_CODE;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.tpscreen;
    }

    @Override
    protected void initViews() {
        View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.type = 2020;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

        lpwv = (TPTestView) findViewById(R.id.mLocusViewTP);
        mFail = (AppCompatButton) findViewById(R.id.touch_fail_btn);
        lpwv.setCallback(this);
        lpwv.setOnCompleteListenerTP(new TPTestView.OnCompleteListenerTP() {
            @Override
            public void onComplete(boolean bResult) {
                mResult = bResult;
                sendResult();
            }
        });
        mFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }

    @Override
    public void callback(boolean callback) {
        if (callback) {
            mFail.setVisibility(View.GONE);
        } else {
            mFail.setVisibility(View.VISIBLE);
        }
    }
}
