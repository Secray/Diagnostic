package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.fragment.CellularNetworkTestFragment;
import com.wingtech.diagnostic.fragment.CellularNetworkTestingFragment;
import com.wingtech.diagnostic.listener.OnResultListener;
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
            SignalListener s1 = new SignalListener(mSim1.getSubscriptionId());
            SignalListener s2 = new SignalListener(mSim2.getSubscriptionId());
            telephonyManager.listen(s1,
                    LISTEN_SIGNAL_STRENGTHS | LISTEN_DATA_CONNECTION_STATE);
            telephonyManager.listen(s2,
                    LISTEN_SIGNAL_STRENGTHS | LISTEN_DATA_CONNECTION_STATE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CellularNetworkTestFragment fragment = new CellularNetworkTestFragment();
                    fragment.setSim1(mSim1);
                    fragment.setSim2(mSim2);
                    fragment.setSim1Level(s1.getLevel());
                    fragment.setSim2Level(s2.getLevel());
                    fragment.setSim1State(s1.getState());
                    fragment.setSim2State(s2.getState());
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                }
            }, 1000);
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
        public SignalListener(int subId) {
            super();
            ReflectUtil.setFieldValue(this, "mSubId", subId);
        }


        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            mLevel = signalStrength.getLevel();
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
    }
}