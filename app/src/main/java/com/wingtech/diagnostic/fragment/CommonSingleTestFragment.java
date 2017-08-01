package com.wingtech.diagnostic.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BatteryTestingActivity;
import com.wingtech.diagnostic.activity.BluetoothTestingActivity;
import com.wingtech.diagnostic.activity.CameraTestActivity;
import com.wingtech.diagnostic.activity.GSensorTestActivity;
import com.wingtech.diagnostic.activity.GyroscopeTestingActivity;
import com.wingtech.diagnostic.activity.MagneticTestingActivity;
import com.wingtech.diagnostic.activity.MultiTouchTestingActivity;
import com.wingtech.diagnostic.activity.SDCardTestingActivity;
import com.wingtech.diagnostic.activity.SIMCardTestingActivity;
import com.wingtech.diagnostic.activity.TouchTestActivity;
import com.wingtech.diagnostic.activity.VibratorTestingActivity;
import com.wingtech.diagnostic.activity.WiFiTestingActivity;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;

import static com.wingtech.diagnostic.util.Constants.BATTERY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.E_COMPASS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GYROSCOPE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.G_SENSOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MULTI_TOUCH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SDCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIM2_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIMCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VIBRATOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIFI_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-20
 */

public class CommonSingleTestFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mTestImg;
    private View mTestResultField;
    private ImageView mTestResultImg;
    private TextView mTestResult;
    AppCompatButton mTestBtn;
    private OnTitleChangedListener mListener;

    private String mTitle;


    public void setTitleChangedListener(OnTitleChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initViewEvents(View view) {
        mTestBtn = (AppCompatButton) view.findViewById(R.id.test_action);
        mTestImg = (ImageView) view.findViewById(R.id.img_test);
        mTestResultField = view.findViewById(R.id.result_field);
        mTestResultImg = (ImageView) view.findViewById(R.id.ic_test_result);
        mTestResult = (TextView) view.findViewById(R.id.txt_test_result);
        mTestBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTitle = mListener.getChangedTitle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case BLUETOOTH_REQUEST_CODE:
            case BATTERY_REQUEST_CODE:
            case E_COMPASS_REQUEST_CODE:
            case GYROSCOPE_REQUEST_CODE:
            case WIFI_REQUEST_CODE:
            case SDCARD_REQUEST_CODE:
            case SIMCARD_REQUEST_CODE:
            case SIM2_REQUEST_CODE:
            case G_SENSOR_REQUEST_CODE:
            case MULTI_TOUCH_REQUEST_CODE:
            case VIBRATOR_REQUEST_CODE:
                boolean result = data.getBooleanExtra("result", false);
                mTestResultField.setVisibility(View.VISIBLE);
                if (result) {
                    mTestResult.setText(getResources().getString(R.string.test_pass, mTitle));
                    mTestResultImg.setImageResource(R.drawable.ic_test_pass);
                } else {
                    mTestResult.setText(getResources().getString(R.string.test_fail, mTitle));
                    mTestResultImg.setImageResource(R.drawable.ic_test_fail);
                }
                mTestBtn.setText(R.string.btn_test_again);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (mTitle) {
            case "G-Sensor Test":
                Intent i = new Intent(mActivity, GSensorTestActivity.class);
                startActivityForResult(i, G_SENSOR_REQUEST_CODE);
                break;
            case "Touch Test":
                startActivity(new Intent(mActivity, TouchTestActivity.class));
                break;
            case "MainCam Test":
                int camId = 0;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("camId", camId);
                startActivity(i);
                break;
            case "VGACam Test":
                camId = 1;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("camId", camId);
                startActivity(i);
                break;
            case "Bluetooth Test":
                i = new Intent(mActivity, BluetoothTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, BLUETOOTH_REQUEST_CODE);
                break;

            case "Wi-Fi Test":
                i = new Intent(mActivity, WiFiTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, WIFI_REQUEST_CODE);
                break;
            case "Battery Test":
                i = new Intent(mActivity, BatteryTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, BATTERY_REQUEST_CODE);
                break;
            case "E-Compass Test":
                i = new Intent(mActivity, MagneticTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, E_COMPASS_REQUEST_CODE);
                break;
            case "Gyroscope Test":
                i = new Intent(mActivity, GyroscopeTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, GYROSCOPE_REQUEST_CODE);
                break;
            case "SD Card Test":
                i = new Intent(mActivity, SDCardTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, SDCARD_REQUEST_CODE);
                break;
            case "SIM Card Test":
                i = new Intent(mActivity, SIMCardTestingActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("index", 1);
                startActivityForResult(i, SIMCARD_REQUEST_CODE);
                break;
            case "SIM2 Test":
                i = new Intent(mActivity, SIMCardTestingActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("index", 2);
                startActivityForResult(i, SIM2_REQUEST_CODE);
                break;
            case "MultiTouch Test":
                i = new Intent(mActivity, MultiTouchTestingActivity.class);
                startActivityForResult(i, MULTI_TOUCH_REQUEST_CODE);
                break;
            case "Vibrator Test":
                i = new Intent(mActivity, VibratorTestingActivity.class);
                startActivityForResult(i, VIBRATOR_REQUEST_CODE);
                break;
        }
    }
}
