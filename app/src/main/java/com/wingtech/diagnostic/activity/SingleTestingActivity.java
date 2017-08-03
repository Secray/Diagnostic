package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.fragment.BatteryTestingFragment;
import com.wingtech.diagnostic.fragment.BlueToothFragment;
import com.wingtech.diagnostic.fragment.CMDMouseTestingFragment;
import com.wingtech.diagnostic.fragment.CameraFlashFragment;
import com.wingtech.diagnostic.fragment.CameraTestFragment;
import com.wingtech.diagnostic.fragment.DisplayFragment;
import com.wingtech.diagnostic.fragment.GSensorFragment;
import com.wingtech.diagnostic.fragment.GyroscopeTestingFragment;
import com.wingtech.diagnostic.fragment.HeadsetFragment;
import com.wingtech.diagnostic.fragment.HeadsetKeyFragment;
import com.wingtech.diagnostic.fragment.HeadsetMicFragment;
import com.wingtech.diagnostic.fragment.KeypadFragment;
import com.wingtech.diagnostic.fragment.MagneticTestingFragment;
import com.wingtech.diagnostic.fragment.ModemTestingFragment;
import com.wingtech.diagnostic.fragment.MultiTouchTestingFragment;
import com.wingtech.diagnostic.fragment.NfcFragment;
import com.wingtech.diagnostic.fragment.ProximityFragment;
import com.wingtech.diagnostic.fragment.RecieverFragment;
import com.wingtech.diagnostic.fragment.SDCardTestingFragment;
import com.wingtech.diagnostic.fragment.SIMCardTestingFragment;
import com.wingtech.diagnostic.fragment.SpeakerFragment;
import com.wingtech.diagnostic.fragment.TestFragment;
import com.wingtech.diagnostic.fragment.VibratorTestingFragment;
import com.wingtech.diagnostic.fragment.WiFiTestingFragment;
import com.wingtech.diagnostic.fragment.WireChargFragment;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;

import static com.wingtech.diagnostic.util.Constants.BATTERY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERAFLASH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.DISPLAY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.E_COMPASS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GYROSCOPE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.G_SENSOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETKEY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETMIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSET_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.KEYPAD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MODEM_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MOUSE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MULTI_TOUCH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.NFC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.PROXIMITY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SDCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIMCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SPEAK_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VGACAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VIBRATOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIFI_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIRECHARGKEY_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-8-2
 */

public class SingleTestingActivity extends BaseActivity implements OnResultChangedCallback {
    protected boolean mResult;
    protected int mRequestCode;
    TestFragment mFragment;

    protected void sendResult() {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("result", mResult);
        setResult(mRequestCode, intent);
        finish();
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onBackPressed() {
        sendResult();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_testing;
    }

    @Override
    protected void initToolbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onWork() {
        String title = getIntent().getStringExtra("title");
        switch (title) {
            case "Bluetooth Test":
                mRequestCode = BLUETOOTH_REQUEST_CODE;
                mFragment = new BlueToothFragment();
                break;
            case "G-Sensor Test":
                mRequestCode = G_SENSOR_REQUEST_CODE;
                mFragment = new GSensorFragment();
                break;
            case "Battery Test":
                mRequestCode = BATTERY_REQUEST_CODE;
                mFragment = new BatteryTestingFragment();
                break;
            case "Wi-Fi Test":
                mRequestCode = WIFI_REQUEST_CODE;
                mFragment = new WiFiTestingFragment();
                break;
            case "E-Compass Test":
                mRequestCode = E_COMPASS_REQUEST_CODE;
                mFragment = new MagneticTestingFragment();
                break;
            case "Gyroscope Test":
                mRequestCode = GYROSCOPE_REQUEST_CODE;
                mFragment = new GyroscopeTestingFragment();
                break;
            case "Modem Test":
                mRequestCode = MODEM_REQUEST_CODE;
                mFragment = new ModemTestingFragment();
                break;
            case "SD Card Test":
                mRequestCode = SDCARD_REQUEST_CODE;
                mFragment = new SDCardTestingFragment();
                break;
            case "SIM Card Test":
                mRequestCode = SIMCARD_REQUEST_CODE;
                mFragment = new SIMCardTestingFragment(1);
                break;
            case "SIM2 Test":
                mRequestCode = SIMCARD_REQUEST_CODE;
                mFragment = new SIMCardTestingFragment(2);
                break;
            case "CMD Mouse Test":
                mRequestCode = MOUSE_REQUEST_CODE;
                mFragment = new CMDMouseTestingFragment();
                break;
            case "MultiTouch Test":
                mRequestCode = MULTI_TOUCH_REQUEST_CODE;
                mFragment = new MultiTouchTestingFragment();
                break;
            case "Vibrator Test":
                mRequestCode = VIBRATOR_REQUEST_CODE;
                mFragment = new VibratorTestingFragment();
                break;
            case "Camera Flash Test":
                mRequestCode = CAMERAFLASH_REQUEST_CODE;
                mFragment = new CameraFlashFragment();
                break;
            case "MainCam Test":
                mRequestCode = CAMERA_REQUEST_CODE;
                mFragment = new CameraTestFragment();
                break;
            case "VGACam Test":
                mRequestCode = VGACAMERA_REQUEST_CODE;
                mFragment = new CameraFlashFragment();
                break;
            case "Display Test":
                mRequestCode = DISPLAY_REQUEST_CODE;
                mFragment = new DisplayFragment();
                break;
            case "Headset Test":
                mRequestCode = HEADSET_REQUEST_CODE;
                mFragment = new HeadsetFragment();
                break;
            case "HeadsetKey Test":
                mRequestCode = HEADSETKEY_REQUEST_CODE;
                mFragment = new HeadsetKeyFragment();
                break;
            case "HeadsetMic Test":
                mRequestCode = HEADSETMIC_REQUEST_CODE;
                mFragment = new HeadsetMicFragment();
                break;
            case "Keypad Test":
                mRequestCode = KEYPAD_REQUEST_CODE;
                mFragment = new KeypadFragment();
                break;
            case "Wireless Test":
                mRequestCode = WIRECHARGKEY_REQUEST_CODE;
                mFragment = new WireChargFragment();
                break;
            case "Proximity Test":
                mRequestCode = PROXIMITY_REQUEST_CODE;
                mFragment = new ProximityFragment();
                break;
            case "Receiver Test":
                mRequestCode = RECIEVER_REQUEST_CODE;
                mFragment = new RecieverFragment();
                break;
            case "Speaker Test":
                mRequestCode = SPEAK_REQUEST_CODE;
                mFragment = new SpeakerFragment();
                break;
            case "NFC Test":
                mRequestCode = NFC_REQUEST_CODE;
                mFragment = new NfcFragment();
                break;
            default:
                mFragment = new BlueToothFragment();
                break;
        }
        mFragment.setTitle(title);
        mFragment.setOnResultChangedCallback(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.single_testing_content,
                mFragment).commit();
    }

    @Override
    public void onChange(boolean result) {
        mResult = result;
        sendResult();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (mFragment instanceof KeypadFragment) {
            return ((KeypadFragment) mFragment).onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
