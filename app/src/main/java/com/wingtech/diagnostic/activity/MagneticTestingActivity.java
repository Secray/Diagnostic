package com.wingtech.diagnostic.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @author xiekui
 * @date 2017-7-29
 */

public class MagneticTestingActivity extends TestingActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mMagnetic;
    private boolean mX, mY, mZ;

    @Override
    protected void onWork() {
        super.onWork();
        mSensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (Math.abs(event.values[0]) >= 1) {
            mX = true;
        }
        if (Math.abs(event.values[1]) >= 1) {
            mY = true;
        }
        if (Math.abs(event.values[2]) >= 1) {
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
