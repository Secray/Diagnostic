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

public class GyroscopeTestingFragment extends TestFragment {
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;

    @Override
    protected void onWork() {
        super.onWork();
        mSensorManager = (SensorManager) mActivity.getApplicationContext().getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Log.i("mSensor = " + mSensor);
        if (mSensor != null) {
            mCallback.onChange(true);
        } else {
            mCallback.onChange(false);
        }
    }
}
