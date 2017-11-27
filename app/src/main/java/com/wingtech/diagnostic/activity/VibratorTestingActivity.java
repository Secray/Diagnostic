package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.asus.atd.smmitest.R;

import java.util.Random;

import static com.wingtech.diagnostic.util.Constants.VIBRATOR_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-8-1
 */

public class VibratorTestingActivity extends TestingActivity implements View.OnClickListener {
    private static final long[] VIBRATE_PATTERN = new long[] {1000, 1000};
    private int mIndex;
    private Vibrator mVibrator;
    private Handler mHandler = new Handler();
    private boolean mIsStoped;

    @Override
    protected void onWork() {
        mRequestCode = VIBRATOR_REQUEST_CODE;
        if (mVibrator == null) {
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        ((TextView) findViewById(R.id.test_title)).setText(getIntent().getStringExtra("title"));

        mVibrator.vibrate(VIBRATE_PATTERN, 0);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsStoped = true;
                mVibrator.cancel();
            }
        }, (mIndex + 1) * 2000 + 1);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.action_one).setOnClickListener(this);
        findViewById(R.id.action_two).setOnClickListener(this);
        findViewById(R.id.action_three).setOnClickListener(this);
        findViewById(R.id.action_four).setOnClickListener(this);
        findViewById(R.id.action_five).setOnClickListener(this);
        findViewById(R.id.action_fail).setOnClickListener(this);
        Random r = new Random();
        mIndex = r.nextInt(5);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common;
    }

    @Override
    public void onClick(View v) {
        if (!mIsStoped) {
            return;
        }

        switch (v.getId()) {
            case R.id.action_one:
                if (mIndex == 0) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_two:
                if (mIndex == 1) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_three:
                if (mIndex == 2) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_four:
                if (mIndex == 3) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_five:
                if (mIndex == 4) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_fail:
                mResult = false;
                break;
        }
        sendResult();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
