package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.wingtech.diagnostic.R;

/**
 * Created by xiekui on 17-8-2.
 */

public class VibratorTestingFragment extends TestFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final long[] VIBRATE_PATTERN = new long[] {1000, 2000};
    private Switch mVibratorSwitch;

    private Vibrator mVibrator;

    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_vibrator;
    }

    @Override
    protected void initViewEvents(View view) {
        mVibratorSwitch = (Switch) view.findViewById(R.id.vibrator_switch);
        AppCompatButton failButton = (AppCompatButton) view.findViewById(R.id.fail_btn);
        AppCompatButton passButton = (AppCompatButton) view.findViewById(R.id.pass_btn);
        failButton.setOnClickListener(this);
        passButton.setOnClickListener(this);
        mVibratorSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onWork() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mVibratorSwitch.setChecked(false);
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
        mCallback.onChange(mResult);
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
