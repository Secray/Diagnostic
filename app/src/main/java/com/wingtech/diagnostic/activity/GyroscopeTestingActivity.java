package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.wingtech.diagnostic.util.Log;

/**
 * @author xiekui
 * @date 2017-7-29
 */

public class GyroscopeTestingActivity extends TestingActivity implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private boolean mX, mY, mZ;

    @Override
    protected void onWork() {
        super.onWork();
        mSensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Log.i("mSensor = " + mSensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("X = " + event.values[0] + " Y = " + event.values[1] + " Z = " + event.values[2]);
        if (Math.abs(event.values[0]) >= 9) {
            mX = true;
        }
        if (Math.abs(event.values[1]) >= 9) {
            mY = true;
        }
        if (Math.abs(event.values[2]) >= 9) {
            mZ = true;
        }

        if (mX && mY && mZ) {
            mResult = true;
        } else {
            mResult = false;
        }
        sendResult();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
