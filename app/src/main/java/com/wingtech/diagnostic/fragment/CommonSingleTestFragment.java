package com.wingtech.diagnostic.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BoardMicActivity;
import com.wingtech.diagnostic.activity.CameraFlashActivity;
import com.wingtech.diagnostic.activity.CameraTestActivity;
import com.wingtech.diagnostic.activity.DisplayActivity;
import com.wingtech.diagnostic.activity.GSensorTestActivity;
import com.wingtech.diagnostic.activity.HeadsetActivity;
import com.wingtech.diagnostic.activity.HeadsetKeyActivity;
import com.wingtech.diagnostic.activity.HeadsetMicActivity;
import com.wingtech.diagnostic.activity.KeypadActivity;
import com.wingtech.diagnostic.activity.LightSensorActivity;
import com.wingtech.diagnostic.activity.MultiTouchTestingActivity;
import com.wingtech.diagnostic.activity.NfcActivity;
import com.wingtech.diagnostic.activity.ProximityActivity;
import com.wingtech.diagnostic.activity.RecieverActivity;
import com.wingtech.diagnostic.activity.SecondaryMicActivity;
import com.wingtech.diagnostic.activity.SingleTestingActivity;
import com.wingtech.diagnostic.activity.SpeakerActivity;
import com.wingtech.diagnostic.activity.TouchTestActivity;
import com.wingtech.diagnostic.activity.VibratorTestingActivity;
import com.wingtech.diagnostic.activity.WireChargActivity;
import com.wingtech.diagnostic.dialog.LoadingDialog;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;

import static com.wingtech.diagnostic.util.Constants.BATTERY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERAFLASH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.DISPLAY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.E_COMPASS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.FINGERPRINT_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GYROSCOPE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.G_SENSOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETKEY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETMIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSET_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.KEYPAD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.LIGHTSENSOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MODEM_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MOUSE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MULTI_TOUCH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.NFC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.PROXIMITY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SDCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SECONDMIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIM2_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIMCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SPEAK_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.TOUCH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VGACAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VIBRATOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIFI_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIRECHARGKEY_REQUEST_CODE;

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
    private OnResultChangedCallback mCallback;
    private Activity activity;
    private String mTitle;
    private int mPos;
    TypedArray mImgArray;

    public CommonSingleTestFragment(int pos) {
        mPos = pos;
    }
    public void setTitleChangedListener(OnTitleChangedListener listener) {
        this.mListener = listener;
    }

    public void setOnResultChangedCallback(OnResultChangedCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected int getLayoutResId() {
        activity  = getActivity();
        return R.layout.fragment_test;
    }

    @Override
    protected void initViewEvents(View view) {
        mTestBtn = (AppCompatButton) view.findViewById(R.id.test_action);
        mTestImg = (ImageView) view.findViewById(R.id.img_test);
        mTestResultField = view.findViewById(R.id.result_field);
        mTestResultImg = (ImageView) view.findViewById(R.id.ic_test_result);
        mTestResult = (TextView) view.findViewById(R.id.txt_test_result);
        mImgArray = getResources().obtainTypedArray(R.array.test_imgs);
        mTestBtn.setOnClickListener(this);
        int id = mImgArray.getResourceId(mPos, 0);
        mTestImg.setImageResource(id);
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
            case MOUSE_REQUEST_CODE:
            case PROXIMITY_REQUEST_CODE:
            case DISPLAY_REQUEST_CODE:
            case HEADSETKEY_REQUEST_CODE:
            case LIGHTSENSOR_REQUEST_CODE:
            case NFC_REQUEST_CODE:
            case WIRECHARGKEY_REQUEST_CODE:
            case CAMERA_REQUEST_CODE:
            case VGACAMERA_REQUEST_CODE:
            case CAMERAFLASH_REQUEST_CODE:
            case RECIEVER_REQUEST_CODE:
            case HEADSET_REQUEST_CODE:
            case SPEAK_REQUEST_CODE:
            case MIC_REQUEST_CODE:
            case MODEM_REQUEST_CODE:
            case HEADSETMIC_REQUEST_CODE:
            case TOUCH_REQUEST_CODE:
            case KEYPAD_REQUEST_CODE:
            case FINGERPRINT_REQUEST_CODE:
                boolean result = data.getBooleanExtra("result", false);
                if (mCallback != null) {
                    mCallback.onChange(result);
                } else {
                    mTestResultField.setVisibility(View.VISIBLE);
                    if (result) {
                        mTestResult.setText(getResources().getString(R.string.test_pass, mTitle));
                        mTestResult.setTextColor(getResources().getColor(R.color.test_result_pass));
                        mTestResultImg.setImageResource(R.drawable.asus_diagnostic_ic_pass);
                        SharedPreferencesUtils.setParam(activity, mTitle, SharedPreferencesUtils.PASS);
                    } else {
                        mTestResult.setText(getResources().getString(R.string.test_fail, mTitle));
                        mTestResult.setTextColor(getResources().getColor(R.color.test_result_fail));
                        mTestResultImg.setImageResource(R.drawable.asus_diagnostic_ic_fail);
                        SharedPreferencesUtils.setParam(activity, mTitle, SharedPreferencesUtils.FAIL);
                    }
                    mTestBtn.setText(R.string.btn_test_again);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (mTitle) {
            case "Bluetooth Test":
            case "Modem Test":
            case "Wi-Fi Test":
            case "Battery Test":
            case "E-Compass Test":
            case "Gyroscope Test":
            case "SD Card Test":
            case "SIM Card Test":
            case "SIM2 Test":
            case "CMD Mouse Test":
            case "Fingerprint Test":
                Intent i = new Intent(mActivity, SingleTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, MOUSE_REQUEST_CODE);
                break;

            case "Vibrator Test":
                i = new Intent(mActivity, VibratorTestingActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, VIBRATOR_REQUEST_CODE);
                break;
            case "G-Sensor Test":
                i = new Intent(mActivity, GSensorTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, G_SENSOR_REQUEST_CODE);
                break;
            case "MultiTouch Test":
                i = new Intent(mActivity, MultiTouchTestingActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, MULTI_TOUCH_REQUEST_CODE);
                break;

            case "Touch Test":
                i = new Intent(mActivity, TouchTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, CAMERA_REQUEST_CODE);
                break;
            case "MainCam Test":
                int camId = 0;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("camId", camId);
                startActivityForResult(i, CAMERA_REQUEST_CODE);
                break;
            case "VGACam Test":
                camId = 1;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("camId", camId);
                startActivityForResult(i, VGACAMERA_REQUEST_CODE);
                break;
            case "Camera Flash Test":
                i = new Intent(mActivity, CameraFlashActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, CAMERAFLASH_REQUEST_CODE);
                break;
            case "Display Test":
                i = new Intent(mActivity, DisplayActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("title_dialog","Display");
                startActivityForResult(i, DISPLAY_REQUEST_CODE);
                break;
            case "Proximity Test":
                i = new Intent(mActivity, ProximityActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mListener.getChangedTitle());
                startActivityForResult(i, PROXIMITY_REQUEST_CODE);
                break;
            case "HeadsetKey Test":
                i = new Intent(mActivity, HeadsetKeyActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mListener.getChangedTitle());
                startActivityForResult(i, HEADSETKEY_REQUEST_CODE);
                break;
            case "LightSensor Test":
                i = new Intent(mActivity, LightSensorActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mListener.getChangedTitle());
                startActivityForResult(i, LIGHTSENSOR_REQUEST_CODE);
                break;
            case "Keypad Test":
                i = new Intent(mActivity, KeypadActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, KEYPAD_REQUEST_CODE);
                break;
            case "NFC Test":
                i = new Intent(mActivity, NfcActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, NFC_REQUEST_CODE);
                break;
            case "Wireless Charging Test":
                i = new Intent(mActivity, WireChargActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mListener.getChangedTitle());
                startActivityForResult(i, WIRECHARGKEY_REQUEST_CODE);
                break;
            case "Receiver Test":
                i = new Intent(mActivity, RecieverActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title_dialog","Receiver");
                i.putExtra("context","Playing");
                startActivityForResult(i, RECIEVER_REQUEST_CODE);
                break;
            case "Headset Test":
                i = new Intent(mActivity, HeadsetActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title_dialog","Headset");
                startActivityForResult(i, HEADSET_REQUEST_CODE);
                break;
            case "Speaker Test":
                i = new Intent(mActivity, SpeakerActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("title_dialog","Speaker");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, SPEAK_REQUEST_CODE);
                break;
            case "BoardMic Test":
                i = new Intent(mActivity, BoardMicActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("title_dialog","BoardMic");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, MIC_REQUEST_CODE);
                break;
            case "SecondaryMic Test":
                i = new Intent(mActivity, SecondaryMicActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("title_dialog","SecondaryMic");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, SECONDMIC_REQUEST_CODE);
                break;
            case "HeadsetMic Test":
                i = new Intent(mActivity, HeadsetMicActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                i.putExtra("title_dialog","HeadsetMic");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, HEADSETMIC_REQUEST_CODE);
                break;
            default:
                Dialog dialog = new LoadingDialog(mActivity, mTitle);
                dialog.show();
                break;
        }
    }
}
