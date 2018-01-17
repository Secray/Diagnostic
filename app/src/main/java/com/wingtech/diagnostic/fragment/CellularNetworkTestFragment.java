package com.wingtech.diagnostic.fragment;

import android.support.v7.widget.AppCompatButton;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.listener.OnResultListener;

/**
 * Created by xiekui on 2017/9/29 0029.
 */

public class CellularNetworkTestFragment extends TestFragment implements View.OnClickListener {
    private TextView mSimId, mSimMsg, mNetWork, mCellularState, mSignalLevel;
    private TableRow mInfo, mInfo1, mNetworkInfo1, mCellularInfo1, mSignalInfo1, mAsuInfo1, mDbmInfo1
            , mNetworkInfo2, mCellularInfo2, mSignalInfo2, mAsuInfo2, mDbmInfo2;
    private TableLayout mSim1Content;
    private TableLayout mSim2Content;
    private TextView mSimId1, mSimMsg1, mNetWork1, mCellularState1, mSignalLevel1, mAsuLevel1, mAsuLevel2, mDbm1, mDbm2;
    private AppCompatButton mFail, mSuccess;
    private int mSim1Level, mSim2Level, mSim1State, mSim2State, mSim1AsuLevel, mSim1Dbm, mSim2AsuLevel, mSim2Dbm;
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
        mDbm1 = (TextView) view.findViewById(R.id.dbm_level1);
        mDbm2 = (TextView) view.findViewById(R.id.dbm_level2);
        mAsuLevel1 = (TextView) view.findViewById(R.id.asu_level1);
        mAsuLevel2 = (TextView) view.findViewById(R.id.asu_level2);
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

        mAsuInfo1 = (TableRow) view.findViewById(R.id.asu_info1);
        mDbmInfo1 = (TableRow) view.findViewById(R.id.dbm_info1);

        mAsuInfo2 = (TableRow) view.findViewById(R.id.asu_info2);
        mDbmInfo2 = (TableRow) view.findViewById(R.id.dbm_info2);

        mSuccess = (AppCompatButton) view.findViewById(R.id.pass);
        mFail = (AppCompatButton) view.findViewById(R.id.fail);
        mSuccess.setOnClickListener(this);
        mFail.setOnClickListener(this);
    }

    @Override
    protected void onWork() {
        if (mSim1 == null) {
            mAsuInfo1.setVisibility(View.GONE);
            mDbmInfo1.setVisibility(View.GONE);
            mSignalInfo1.setVisibility(View.GONE);
            mCellularInfo1.setVisibility(View.GONE);
            mNetworkInfo1.setVisibility(View.GONE);
            mInfo.setVisibility(View.GONE);
        } else {
            mSimMsg.setVisibility(View.GONE);
            mNetWork.setText(mSim1.getCarrierName());
            mCellularState.setText(getState(mSim1State));
            mSignalLevel.setText(getSignalLevel(mSim1Level));
            mAsuLevel1.setText(mSim1AsuLevel + "");
            mDbm1.setText(mSim1Dbm + "");
            if ("Disconnected".equals(getState(mSim1State))) {
                mSignalInfo1.setVisibility(View.GONE);
            } else {
                mInfo.setVisibility(View.GONE);
            }
        }

        if (mSim2 == null) {
            mAsuInfo2.setVisibility(View.GONE);
            mDbmInfo2.setVisibility(View.GONE);
            mSignalInfo2.setVisibility(View.GONE);
            mCellularInfo2.setVisibility(View.GONE);
            mNetworkInfo2.setVisibility(View.GONE);
            mInfo1.setVisibility(View.GONE);
        } else {
            mSimMsg1.setVisibility(View.GONE);
            mNetWork1.setText(mSim2.getCarrierName());
            mCellularState1.setText(getState(mSim2State));
            mSignalLevel1.setText(getSignalLevel(mSim2Level));
            mAsuLevel2.setText(mSim2AsuLevel + "");
            mDbm2.setText(mSim2Dbm + "");
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


    public void setSim1AsuLevel(int sim1AsuLevel) {
        this.mSim1AsuLevel = sim1AsuLevel;
    }

    public void setSim1Dbm(int sim1Dbm) {
        this.mSim1Dbm = sim1Dbm;
    }

    public void setSim2AsuLevel(int sim2AsuLevel) {
        this.mSim2AsuLevel = sim2AsuLevel;
    }

    public void setSim2Dbm(int sim2Dbm) {
        this.mSim2Dbm = sim2Dbm;
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
