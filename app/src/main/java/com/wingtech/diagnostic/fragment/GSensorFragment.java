package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.widget.GSensorView;

/**
 * Created by xiekui on 17-8-2.
 */

public class GSensorFragment extends TestFragment implements SensorEventListener, View.OnClickListener {
    GSensorView mGSensorView;
    AppCompatButton mBtn;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSum = 0;
    private boolean mXPlusPass, mXMinusPass, mYPlusPass, mYMinusPass, mZPlusPass, mZMinusPass;

    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_gsensor;
    }

    @Override
    protected void initViewEvents(View view) {
        mGSensorView = (GSensorView) view.findViewById(R.id.gsensor_view);
        mBtn = (AppCompatButton) view.findViewById(R.id.gsensor_fail);
        mBtn.setOnClickListener(this);
        mSensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onWork() {
        // cancel title
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mSum = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] >= 9 && !mXPlusPass){
            mGSensorView.setXPlusPass(true);
            mSum ++;
            mXPlusPass = true;
        }

        if (event.values[0] <= -9 && !mXMinusPass) {
            mGSensorView.setXMinusPass(true);
            mSum ++;
            mXMinusPass = true;
        }

        if(event.values[1] >= 9 && !mYPlusPass){
            mGSensorView.setYPlusPass(true);
            mSum ++;
            mYPlusPass = true;
        }

        if(event.values[1] <= -9 && !mYMinusPass){
            mGSensorView.setYMinusPass(true);
            mSum ++;
            mYMinusPass = true;
        }

        if(event.values[2] >= 9 && !mZPlusPass){
            mGSensorView.setZPlusPass(true);
            mSum ++;
            mZPlusPass = true;
        }

        if(event.values[2] <= -9 && !mZMinusPass){
            mGSensorView.setZMinusPass(true);
            mSum ++;
            mZMinusPass = true;
        }

        if (mSum == 6) {
            mResult = true;
            mCallback.onChange(mResult);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        mResult = false;
        mCallback.onChange(mResult);
    }
}
