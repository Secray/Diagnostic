package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.view.WindowManager;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.fragment.BatteryTestingFragment;
import com.wingtech.diagnostic.fragment.BlueToothFragment;
import com.wingtech.diagnostic.fragment.CMDMouseTestingFragment;
import com.wingtech.diagnostic.fragment.FingerprintFragment;
import com.wingtech.diagnostic.fragment.GpsTestingFragment;
import com.wingtech.diagnostic.fragment.GyroscopeTestingFragment;
import com.wingtech.diagnostic.fragment.MagneticTestingFragment;
import com.wingtech.diagnostic.fragment.ModemTestingFragment;
import com.wingtech.diagnostic.fragment.SDCardTestingFragment;
import com.wingtech.diagnostic.fragment.SIMCardTestingFragment;
import com.wingtech.diagnostic.fragment.TestFragment;
import com.wingtech.diagnostic.fragment.WiFiTestingFragment;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;

import static com.wingtech.diagnostic.util.Constants.BATTERY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.E_COMPASS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.FINGERPRINT_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GPS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GYROSCOPE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MODEM_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MOUSE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SDCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIMCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIFI_REQUEST_CODE;
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
            case "SIM Card1 Test":
                mRequestCode = SIMCARD_REQUEST_CODE;
                mFragment = new SIMCardTestingFragment(1);
                break;
            case "SIM Card2 Test":
                mRequestCode = SIMCARD_REQUEST_CODE;
                mFragment = new SIMCardTestingFragment(2);
                break;
            case "CMD Mouse Test":
                mRequestCode = MOUSE_REQUEST_CODE;
                mFragment = new CMDMouseTestingFragment();
                break;
            case "Fingerprint Test":
                mRequestCode = FINGERPRINT_REQUEST_CODE;
                mFragment = new FingerprintFragment();
                break;
            case "GPS Test":
                mRequestCode = GPS_REQUEST_CODE;
                mFragment = new GpsTestingFragment();
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
}
