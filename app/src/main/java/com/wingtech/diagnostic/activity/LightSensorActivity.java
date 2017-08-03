package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.LIGHTSENSOR_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class LightSensorActivity extends TestingActivity implements SensorEventListener {
    private CheckBox mStrongFirst = null;
    private CheckBox mWeak = null;
    private CheckBox mNearSecond = null;
    private TextView mStrongFirstTxt = null;
    private TextView mWeakTxt = null;
    private TextView mNearSecondTxt = null;
    private TextView mTitle = null;
    private Button mTouchFailBtn = null;

    private static final String TAG = "LightSensorActivity";
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private  boolean mIsWeak = false;
    private  boolean mIsStrongSecond = false;
    private  boolean mIsStrong = false;

    private float lightWeak = 5.0f;
    private float lightStrong = 300.0f;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViews() {
        mStrongFirst = (CheckBox) findViewById(R.id.box_txt_1);
        mWeak = (CheckBox) findViewById(R.id.box_txt_2);
        mNearSecond = (CheckBox) findViewById(R.id.box_txt_3);
        mStrongFirstTxt = (TextView) findViewById(R.id.txt_box_1);
        mWeakTxt = (TextView) findViewById(R.id.txt_box_2);
        mNearSecondTxt = (TextView) findViewById(R.id.txt_box_3);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);
        mStrongFirst.setVisibility(View.VISIBLE);
        mWeak.setVisibility(View.VISIBLE);
        mNearSecond.setVisibility(View.VISIBLE);
        mWeakTxt.setVisibility(View.VISIBLE);
        mNearSecondTxt.setVisibility(View.VISIBLE);
        mStrongFirstTxt.setVisibility(View.VISIBLE);
        mTouchFailBtn = (Button) findViewById(R.id.fail_btn);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = LIGHTSENSOR_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mWeakTxt.setText(R.string.lightsensor_txt_strong);
        mNearSecondTxt.setText(R.string.lightsensor_txt_strong);
        mStrongFirstTxt.setText(R.string.lightsensor_txt_strong);
        mTitle.setText(R.string.lightsensor_title);

        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Called when there is a new sensor event.  Note that "on changed"
     * is somewhat of a misnomer, as this will also be called if we have a
     * new reading from a sensor with the exact same sensor values (but a
     * newer timestamp).
     * <p>
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float lightLevel = event.values[0];
        Log.d(TAG,"lightLevel = "+lightLevel);
        if(lightLevel > lightStrong){
            mIsStrong = true;
            mTitle.setText(R.string.lightsensor_title_weak);
            Log.d(TAG,"lightLevel = "+lightLevel);
            mStrongFirst.setChecked(mIsStrong);
        }
        if(lightLevel < lightStrong && mIsStrong == true){
            mIsWeak = true;
            mTitle.setText(R.string.lightsensor_title);
            Log.d(TAG,"lightLevel = "+lightLevel);
            mWeak.setChecked(mIsWeak);
        }
        if(lightLevel > lightStrong && mIsWeak == true &&  mIsStrong == true){
            mIsStrongSecond = true;
            Log.d(TAG,"mIsStrongSecond lightLevel = "+lightLevel);
            mNearSecond.setChecked(mIsStrongSecond);
        }

        if(mIsStrong == true && mIsWeak == true &&  mIsStrongSecond == true){
            Log.d(TAG,"finish ");
            mResult = true;
            sendResult();
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     * <p>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}