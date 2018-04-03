package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.android.helper.Helper;
import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.fragment.CellularNetworkTestFragment;
import com.wingtech.diagnostic.fragment.CellularNetworkTestingFragment;
import com.wingtech.diagnostic.listener.OnResultListener;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.ReflectUtil;

import static android.telephony.PhoneStateListener.LISTEN_DATA_CONNECTION_STATE;
import static android.telephony.PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;
import static com.wingtech.diagnostic.util.Constants.CELLULAR_NETWORK_REQUEST_CODE;

/**
 * Created by xiekui on 2017/9/29 0029.
 */

public class CellularNetworkActivity extends TestingActivity implements OnResultListener {
    private SubscriptionManager mSubscriptionManager;
    private SubscriptionInfo mSim1;
    private SubscriptionInfo mSim2;
    private Handler mHandler;

    @Override
    protected void onWork() {
        mRequestCode = CELLULAR_NETWORK_REQUEST_CODE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String id = telephonyManager.getSubscriberId();
        mSubscriptionManager = SubscriptionManager.from(this);
        mSim1 = mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0);
        mSim2 = mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1);
        if (mSim1 == null && mSim2 == null) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.no_sim_find_title)
                    .setMessage(R.string.sim_not_found_msg)
                    .setNegativeButton(android.R.string.ok, (dialog, which) -> {
                        mResult = false;
                        sendResult();
                    }).create().show();
        } else {
            if (!isNetworkAvailable()) {
                Log.i("Mobile Network is not available");
                new AlertDialog.Builder(this)
                        .setMessage(R.string.no_network_find_title)
                        .setNegativeButton(android.R.string.ok, (dialog, which) -> {
                            mResult = false;
                            sendResult();
                        }).create().show();
            } else {
                SignalListener s1;
                if (mSim1 != null) {
                    s1 = new SignalListener(mSim1.getSubscriptionId());
                } else {
                    s1 = null;
                }
                SignalListener s2;

                if (mSim2 != null) {
                    s2 = new SignalListener(mSim2.getSubscriptionId());
                } else {
                    s2 = null;
                }

                if (s1 != null) {
                    telephonyManager.listen(s1,
                            LISTEN_SIGNAL_STRENGTHS | LISTEN_DATA_CONNECTION_STATE);
                }
                if (s2 != null) {
                    telephonyManager.listen(s2,
                            LISTEN_SIGNAL_STRENGTHS | LISTEN_DATA_CONNECTION_STATE);
                }
                mHandler.postDelayed(() -> {
                    CellularNetworkTestFragment fragment = new CellularNetworkTestFragment();
                    fragment.setListener(CellularNetworkActivity.this);
                    fragment.setSim1(mSim1);
                    fragment.setSim2(mSim2);
                    fragment.setSim1Level(s1 != null ? s1.getLevel() : 0);
                    fragment.setSim2Level(s2 != null ? s2.getLevel() : 0);
                    fragment.setSim1State(s1 != null ? s1.getState() : 0);
                    fragment.setSim2State(s2 != null ? s2.getState() : 0);
                    fragment.setSim1AsuLevel(s1 != null ? s1.getAsu() : 0);
                    fragment.setSim2AsuLevel(s2 != null ? s2.getAsu() : 0);
                    fragment.setSim1Dbm(s1 != null ? s1.getDbm() : 0);
                    fragment.setSim2Dbm(s2 != null ? s2.getDbm() : 0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                }, 1000);
            }
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cellular_network;
    }

    @Override
    protected void initViews() {
        mHandler = new Handler();
        mTitle = getIntent().getStringExtra("title");
        CellularNetworkTestingFragment fragment = new CellularNetworkTestingFragment();
        fragment.setTitle(mTitle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void setResult(boolean result) {
        mResult = result;
        sendResult();
    }

    class SignalListener extends PhoneStateListener {
        private int mLevel;
        private int mState;
        private int mAsu;
        private int mDbm;

        public SignalListener(int subId) {
            super();
            ReflectUtil.setFieldValue(this, "mSubId", subId);
        }


        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            mLevel = signalStrength.getLevel();
            mAsu = Helper.getAsuLevel(signalStrength);
            mDbm = Helper.getDbm(signalStrength);
            Log.i("asu = " + mAsu + " dbm = " + mDbm);
        }

        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
            mState = state;
        }

        public int getState() {
            return mState;
        }

        public int getLevel() {
            return mLevel;
        }

        public int getAsu() {
            return mAsu;
        }

        public int getDbm() {
            return mDbm;
        }
    }


    private String getSystemProperties(String key, String defaultValue) {
        if (key == null)
            return null;
        return Helper.getSystemProperties(key, defaultValue);
    }

    private boolean isNetworkAvailable() {
        String emergency = getText(Helper.getEmergencyResource()).toString();
        String noService = getText(Helper.getNoServiceResource()).toString();
        Log.i(emergency + " " + noService);
        boolean sim1State = mSim1 != null && (mSim1.getCarrierName()
                .equals(emergency) || mSim1.getCarrierName().equals(noService));
        boolean sim2State = mSim2 != null && (mSim2.getCarrierName().equals(emergency)
                || mSim2.getCarrierName().equals(noService));
        if (mSim2 == null && sim1State) {
            return false;
        }

        if (mSim1 == null && sim2State) {
            return false;
        }

        if (sim1State && sim2State) {
            return false;
        }

        if (Settings.System.getInt(getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
            return false;
        }
        return true;
    }
}
