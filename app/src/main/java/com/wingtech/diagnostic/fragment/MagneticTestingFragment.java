package com.wingtech.diagnostic.fragment;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.wingtech.diagnostic.util.Log;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by xiekui on 17-8-2.
 */

public class MagneticTestingFragment extends TestFragment {
    private SensorManager mSensorManager;
    private Sensor mMagnetic;
    private boolean mX, mY, mZ;

    @Override
    protected void onWork() {
        super.onWork();
        mSensorManager = (SensorManager) mActivity.getApplicationContext().getSystemService(SENSOR_SERVICE);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Log.i("mSensor = " + mMagnetic);
        if (mMagnetic != null) {
            mCallback.onChange(true);
        } else {
            mCallback.onChange(false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
