package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.asus.atd.smmitest.R;

import static com.wingtech.diagnostic.util.Constants.VIBRATOR_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-8-1
 */

public class VibratorTestingActivity extends TestingActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private static final long[] VIBRATE_PATTERN = new long[] {1000, 2000};
    private Switch mVibratorSwitch;

    private Vibrator mVibrator;

    @Override
    protected void onWork() {
        mRequestCode = VIBRATOR_REQUEST_CODE;
        if (mVibrator == null) {
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibratorSwitch.setChecked(true);
    }

    @Override
    protected void initViews() {
        mVibratorSwitch = (Switch) findViewById(R.id.vibrator_switch);
        AppCompatButton failButton = (AppCompatButton) findViewById(R.id.fail_btn);
        AppCompatButton passButton = (AppCompatButton) findViewById(R.id.pass_btn);
        failButton.setOnClickListener(this);
        passButton.setOnClickListener(this);
        mVibratorSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_vibrator;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fail_btn:
                mResult = false;
                break;
            case R.id.pass_btn:
                mResult = true;
                break;
        }
        mVibrator.cancel();
        sendResult();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVibratorSwitch.setChecked(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mVibrator.vibrate(VIBRATE_PATTERN, 0);
        } else {
            mVibrator.cancel();
        }
    }
}
