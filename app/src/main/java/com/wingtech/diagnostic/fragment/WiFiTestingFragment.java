package com.wingtech.diagnostic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.wingtech.diagnostic.util.Log;

import java.util.List;

import static com.wingtech.diagnostic.util.Constants.WIFI_STATE_CHANGED;

/**
 * Created by xiekui on 17-8-2.
 */

public class WiFiTestingFragment extends TestFragment {
    private WifiManager mWiFiManager;
    private int mWiFiState;
    private boolean mWiFiEnable;
    private List<ScanResult> mScanResults;

    @Override
    protected void onWork() {
        super.onWork();
        mWiFiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_STATE_CHANGED);
        mActivity.registerReceiver(mWiFiReceiver, filter);
        enableWiFi();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWiFiEnable) {
            mWiFiManager.setWifiEnabled(false);
        }
        mActivity.unregisterReceiver(mWiFiReceiver);
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
            mWiFiManager.setWifiEnabled(true);
        }
        mWiFiEnable = true;
    }

    private void refresh() {
        mWiFiState = mWiFiManager.getWifiState();
        switch (mWiFiState) {
            case WifiManager.WIFI_STATE_ENABLED:
                Log.i("WIFI_STATE_ENABLED");
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                Log.i("WIFI_STATE_ENABLING");
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                Log.i("WIFI_STATE_DISABLED");
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                Log.i("WIFI_STATE_DISABLING");
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                Log.i("WIFI_STATE_UNKNOWN");
                break;
            default:
                break;
        }
        if (mWiFiState != WifiManager.WIFI_STATE_ENABLED) {
            mResult = false;
            mCallback.onChange(mResult);
        } else {
            mWiFiManager.startScan();
            mScanResults = mWiFiManager.getScanResults();

            Log.i("WiFi Scan results = " + mScanResults.size());
            if (isWiFi(mActivity)) {
                Log.e("It's not WiFi");
            }

            if (mScanResults.size() > 0 && mWiFiEnable) {
                mResult = true;
            } else {
                mResult = false;
            }
            mCallback.onChange(mResult);
        }
    }

    BroadcastReceiver mWiFiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive(): " + intent.getAction());
            refresh();
        }
    };
}
