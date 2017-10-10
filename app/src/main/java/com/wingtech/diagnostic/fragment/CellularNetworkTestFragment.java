package com.wingtech.diagnostic.fragment;

import android.support.v7.widget.AppCompatButton;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.activity.CellularNetworkActivity;
import com.wingtech.diagnostic.listener.OnResultListener;

/**
 * Created by xiekui on 2017/9/29 0029.
 */

public class CellularNetworkTestFragment extends TestFragment implements View.OnClickListener {
    private TextView mSimId, mSimMsg, mNetWork, mCellularState, mSignalLevel;
    private TableRow mInfo, mInfo1, mNetworkInfo1, mCellularInfo1, mSignalInfo1
            , mNetworkInfo2, mCellularInfo2, mSignalInfo2;
    private TableLayout mSim1Content;
    private TableLayout mSim2Content;
    private TextView mSimId1, mSimMsg1, mNetWork1, mCellularState1, mSignalLevel1;
    private AppCompatButton mFail, mSuccess;
    private int mSim1Level, mSim2Level, mSim1State, mSim2State;
    private SubscriptionInfo mSim1, mSim2;
    private OnResultListener mListener;
    public void setListener(OnResultListener listener) {
        mListener = listener;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_cellular_network;
    }

    @Override
    protected void initViewEvents(View view) {
        mSimId = (TextView) view.findViewById(R.id.sim_id1);
        mSimMsg = (TextView) view.findViewById(R.id.sim_msg1);
        mNetWork = (TextView) view.findViewById(R.id.network1);
        mCellularState = (TextView) view.findViewById(R.id.cellular_network_state1);
        mSignalLevel = (TextView) view.findViewById(R.id.signal_level1);
        mInfo = (TableRow) view.findViewById(R.id.sim1_info);

        mSimId1 = (TextView) view.findViewById(R.id.sim_id2);
        mSimMsg1 = (TextView) view.findViewById(R.id.sim_msg2);
        mNetWork1 = (TextView) view.findViewById(R.id.network2);
        mCellularState1 = (TextView) view.findViewById(R.id.cellular_network_state2);
        mSignalLevel1 = (TextView) view.findViewById(R.id.signal_level2);
        mInfo1 = (TableRow) view.findViewById(R.id.sim2_info);
        mSim1Content = (TableLayout) view.findViewById(R.id.sim1_content);
        mSim2Content = (TableLayout) view.findViewById(R.id.sim2_content);

        mNetworkInfo1 = (TableRow) view.findViewById(R.id.network_info1);
        mNetworkInfo2 = (TableRow) view.findViewById(R.id.network_info2);

        mCellularInfo1 = (TableRow) view.findViewById(R.id.cellular_info1);
        mCellularInfo2 = (TableRow) view.findViewById(R.id.cellular_info2);

        mSignalInfo1 = (TableRow) view.findViewById(R.id.signal_info1);
        mSignalInfo2 = (TableRow) view.findViewById(R.id.signal_info2);

        mSuccess = (AppCompatButton) view.findViewById(R.id.pass);
        mFail = (AppCompatButton) view.findViewById(R.id.fail);
        mSuccess.setOnClickListener(this);
        mFail.setOnClickListener(this);
    }

    @Override
    protected void onWork() {
        if (mSim1 == null) {
            mSignalInfo1.setVisibility(View.GONE);
            mCellularInfo1.setVisibility(View.GONE);
            mNetworkInfo1.setVisibility(View.GONE);
            mInfo.setVisibility(View.GONE);
        } else {
            mSimMsg.setVisibility(View.GONE);
            mNetWork.setText(mSim1.getCarrierName());
            mCellularState.setText(getState(mSim1State));
            mSignalLevel.setText(getSignalLevel(mSim1Level));

            if ("Disconnected".equals(getState(mSim1State))) {
                mSignalInfo1.setVisibility(View.GONE);
            } else {
                mInfo.setVisibility(View.GONE);
            }
        }

        if (mSim2 == null) {
            mSignalInfo2.setVisibility(View.GONE);
            mCellularInfo2.setVisibility(View.GONE);
            mNetworkInfo2.setVisibility(View.GONE);
            mInfo1.setVisibility(View.GONE);
        } else {
            mSimMsg1.setVisibility(View.GONE);
            mNetWork1.setText(mSim2.getCarrierName());
            mCellularState1.setText(getState(mSim2State));
            mSignalLevel1.setText(getSignalLevel(mSim2Level));

            if ("Disconnected".equals(getState(mSim2State))) {
                mSignalInfo2.setVisibility(View.GONE);
            } else {
                mInfo1.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pass:
                mListener.setResult(true);
                break;
            case R.id.fail:
                mListener.setResult(false);
                break;
        }
    }

    public void setSim1Level(int level) {
        mSim1Level = level;
    }

    public void setSim2Level(int level) {
        mSim2Level = level;
    }

    public void setSim1State(int state) {
        mSim1State = state;
    }

    public void setSim2State(int state) {
        mSim2State = state;
    }

    public void setSim1(SubscriptionInfo s) {
        mSim1 = s;
    }

    public void setSim2(SubscriptionInfo s) {
        mSim2 = s;
    }


    private String getState(int state) {
        if (state != TelephonyManager.DATA_CONNECTED) {
            return "Disconnected";
        }
        return "Connected";
    }

    private String getSignalLevel(int state) {
        if (state < 0) {
            return "No signal";
        } else if (state >= 0 && state < 2) {
            return "Poor signal";
        } else if(state >= 2 && state < 3) {
            return "Normal signal";
        } else {
            return "Strong signal";
        }
    }
}
