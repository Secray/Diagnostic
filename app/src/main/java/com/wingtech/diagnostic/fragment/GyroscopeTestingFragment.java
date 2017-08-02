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

public class GyroscopeTestingFragment extends TestFragment implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private boolean mX, mY, mZ;

    @Override
    protected void onWork() {
        super.onWork();
        mSensorManager = (SensorManager) mActivity.getApplicationContext().getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        Log.i("mSensor = " + mSensor);
    }

    @Override
    public void onPause() {
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
            mCallback.onChange(mResult);
        } else {
            mResult = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
