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

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.PROXIMITY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class ProximityActivity extends TestingActivity implements SensorEventListener {
    private CheckBox mNearFirst = null;
    private CheckBox mFast = null;
    private CheckBox mNearSecond = null;
    private TextView mNearFirstTxt = null;
    private TextView mFastTxt = null;
    private TextView mNearSecondTxt = null;
    private TextView mTitle = null;
    private Button mTouchFailBtn = null;

    private static final String TAG = "ProximityActivity";
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private static final float DISTANCE_PROXIMITY = 0.5f;
    private  boolean mIsNear = false;
    private  boolean mIsNearSecond = false;
    private	 boolean mIsFar = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViews() {
        mNearFirst = (CheckBox) findViewById(R.id.box_txt_1);
        mFast = (CheckBox) findViewById(R.id.box_txt_2);
        mNearSecond = (CheckBox) findViewById(R.id.box_txt_3);
        mNearFirstTxt = (TextView) findViewById(R.id.txt_box_1);
        mFastTxt = (TextView) findViewById(R.id.txt_box_2);
        mNearSecondTxt = (TextView) findViewById(R.id.txt_box_3);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);
        mFastTxt.setVisibility(View.VISIBLE);
        mNearSecondTxt.setVisibility(View.VISIBLE);
        mNearFirstTxt.setVisibility(View.VISIBLE);
        mTouchFailBtn = (Button) findViewById(R.id.fail_btn);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = PROXIMITY_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mFastTxt.setText(R.string.proximity_txt_far);
        mNearSecondTxt.setText(R.string.proximity_txt_near);
        mNearFirstTxt.setText(R.string.proximity_txt_near);
        mTitle.setText(R.string.proximity_title);

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
        mSensorManager.unregisterListener(ProximityActivity.this);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(ProximityActivity.this, mSensor, SensorManager.SENSOR_DELAY_UI);
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
        float distance = event.values[0];
        Log.d(TAG,"distance = "+distance);
        if(distance < DISTANCE_PROXIMITY){
            mIsNear = true;
            Log.d(TAG,"mIsNear = "+mIsNear);
            mNearFirst.setChecked(mIsNear);
            mNearFirst.setVisibility(View.VISIBLE);
            mTitle.setText(R.string.proximity_title_away);

        }
        if(distance > DISTANCE_PROXIMITY && mIsNear == true){
            mIsFar = true;
            Log.d(TAG,"mIsFar = "+mIsFar);
            mFast.setChecked(mIsFar);
            mFast.setVisibility(View.VISIBLE);
            mTitle.setText(R.string.proximity_title);

        }
        if(distance < DISTANCE_PROXIMITY && mIsNear == true &&  mIsFar == true){
            mIsNearSecond = true;
            Log.d(TAG,"mIsNearSecond = "+mIsNearSecond);
            mNearSecond.setChecked(mIsNearSecond);
            mNearSecond.setVisibility(View.VISIBLE);

        }

        if(mIsNear == true && mIsNearSecond == true &&  mIsFar == true){
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
