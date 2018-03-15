package com.wingtech.diagnostic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.wingtech.diagnostic.util.Log;

import java.util.List;

import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static com.wingtech.diagnostic.util.Constants.WIFI_STATE_CHANGED;

/**
 * Created by xiekui on 17-8-2.
 */

public class WiFiTestingFragment extends TestFragment {
    private WifiManager mWiFiManager;
    private int mWiFiState;
    private boolean mWiFiEnable;
    private boolean mLastState;
    private List<ScanResult> mScanResults;
    private boolean mIsRegister;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mWiFiState = mWiFiManager.getWifiState();
            Log.i("state = " + mWiFiState + " " + mWiFiEnable);
            if (mWiFiEnable) {
                if (mWiFiState == WIFI_STATE_ENABLED) {
                    mWiFiManager.startScan();
                    mTxtTitle.postDelayed(() -> {
                        mScanResults = mWiFiManager.getScanResults();

                        Log.i("WiFi Scan results = " + mScanResults.size());
                        if (isWiFi(mActivity)) {
                            Log.e("It's not WiFi");
                        }

                        mResult = mScanResults.size() > 0 && mWiFiEnable;
                        mCallback.onChange(mResult);
                    }, 2000);
                    if (mIsRegister) {
                        mActivity.unregisterReceiver(mWiFiReceiver);
                        mIsRegister = false;
                    }
                }
            } else {
                mResult = false;
                mCallback.onChange(mResult);
            }
        }
    };;

    @Override
    protected void onWork() {
        super.onWork();
        mWiFiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mLastState = mWiFiManager.isWifiEnabled();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_STATE_CHANGED);
        mActivity.registerReceiver(mWiFiReceiver, filter);
        mIsRegister = true;
        enableWiFi();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mLastState) {
            mWiFiManager.setWifiEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private boolean isWiFi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private void enableWiFi() {
        mWiFiState = mWiFiManager.getWifiState();
        if (mWiFiState == WifiManager.WIFI_STATE_DISABLED
                || mWiFiState == WifiManager.WIFI_STATE_UNKNOWN) {
            mWiFiEnable = mWiFiManager.setWifiEnabled(true);
        } else {
            mWiFiManager.setWifiEnabled(false);
            mWiFiEnable = mWiFiManager.setWifiEnabled(true);
        }
    }

    BroadcastReceiver mWiFiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mHandler.sendEmptyMessage(0);
        }
    };
}
