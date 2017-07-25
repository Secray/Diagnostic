package com.wingtech.diagnostic.fragment;

import android.content.Intent;
import android.view.View;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.activity.CameraTestActivity;
import com.wingtech.diagnostic.activity.GSensorTestActivity;
import com.wingtech.diagnostic.activity.TouchTestActivity;
import com.wingtech.diagnostic.dialog.BaseDialog;
import com.wingtech.diagnostic.dialog.LoadingDialog;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;

/**
 * @author xiekui
 * @date 2017-7-20
 */

public class CommonSingleTestFragment extends BaseFragment implements View.OnClickListener {

    private OnTitleChangedListener mListener;

    public void setTitleChangedListener(OnTitleChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initViewEvents(View view) {
        view.findViewById(R.id.test_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (mListener.getChangedTitle()) {
            case "G-Sensor Test":
                startActivity(new Intent(mActivity, GSensorTestActivity.class));
                break;
            case "Touch Test":
                startActivity(new Intent(mActivity, TouchTestActivity.class));
                break;
            case "MainCam Test":
                int camId = 0;
                Intent i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("camId", camId);
                startActivity(i);
                break;
            case "VGACam Test":
                camId = 1;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("camId", camId);
                startActivity(i);
                break;
            default:
                BaseDialog dialog = new LoadingDialog(mActivity, mListener.getChangedTitle());
                dialog.show();
                break;
        }
    }
}
