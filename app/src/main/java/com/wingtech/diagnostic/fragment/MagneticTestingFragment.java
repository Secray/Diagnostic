package com.wingtech.diagnostic.fragment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.wingtech.diagnostic.util.Log;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by xiekui on 17-8-2.
 */

public class MagneticTestingFragment extends TestFragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mMagnetic;
    private boolean mX, mY, mZ;

    @Override
    protected void onWork() {
        super.onWork();
        mSensorManager = (SensorManager) mActivity.getApplicationContext().getSystemService(SENSOR_SERVICE);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("X = " + event.values[0] + " Y = " + event.values[1] + " Z = " + event.values[2]);
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
        mCallback.onChange(mResult);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
