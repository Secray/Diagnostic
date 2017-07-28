package com.wingtech.diagnostic.activity;

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
 * @author xiekui
 * @date 2017-7-28
 */

public class WiFiTestingActivity extends TestingActivity {
    private WifiManager mWiFiManager;
    private int mWiFiState;
    private boolean mWiFiEnable;
    private List<ScanResult> mScanResults;

    @Override
    protected void onWork() {
        mWiFiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_STATE_CHANGED);
        registerReceiver(mWiFiReceiver, filter);
        enableWiFi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWiFiEnable) {
            mWiFiManager.setWifiEnabled(false);
        }
        unregisterReceiver(mWiFiReceiver);
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
            sendResult();
        }
        mWiFiManager.startScan();

        int size = 0;
        do {
            mScanResults = mWiFiManager.getScanResults();
            if (mScanResults != null) {
                size = mScanResults.size();
            }
        } while (mScanResults == null || size == 0);

        Log.i("WiFi Scan results = " + mScanResults.size());
        if (isWiFi(this)) {
            Log.e("It's not WiFi");
        }

        if (mScanResults.size() > 0 && mWiFiEnable) {
            mResult = true;
        } else {
            mResult = false;
        }
        sendResult();
    }

    BroadcastReceiver mWiFiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive(): " + intent.getAction());
            refresh();
        }
    };
}
