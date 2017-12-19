package com.wingtech.diagnostic.activity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.widget.GSensorView;

import static com.wingtech.diagnostic.util.Constants.G_SENSOR_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class GSensorTestActivity extends TestingActivity implements
        SensorEventListener, View.OnClickListener {
    GSensorView mGSensorView;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSum = 0;
    private boolean mXPlusPass, mXMinusPass, mYPlusPass, mYMinusPass, mZPlusPass, mZMinusPass;

    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_gsensor;
    }

    @Override
    protected void initViews() {
        mGSensorView = (GSensorView) findViewById(R.id.gsensor_view);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        findViewById(R.id.gsensor_fail).setOnClickListener(this);
    }

    @Override
    protected void onWork() {
        mRequestCode = G_SENSOR_REQUEST_CODE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
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

        if(event.values[1] <= -9 && !mYPlusPass){
            mGSensorView.setYPlusPass(true);
            mSum ++;
            mYPlusPass = true;
        }

        if(event.values[1] >= 9 && !mYMinusPass){
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
            sendResult();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        mResult = false;
        sendResult();
    }
}
