package com.wingtech.diagnostic.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.CameraTestActivity;
import com.wingtech.diagnostic.activity.GSensorTestActivity;
import com.wingtech.diagnostic.activity.TestingActivity;
import com.wingtech.diagnostic.activity.TouchTestActivity;
import com.wingtech.diagnostic.listener.OnTitleChangedListener;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-20
 */

public class CommonSingleTestFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mTestImg;
    private View mTestResultField;
    private ImageView mTestResultImg;
    private TextView mTestResult;
    AppCompatButton mTestBtn;
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
        mTestBtn = (AppCompatButton) view.findViewById(R.id.test_action);
        mTestImg = (ImageView) view.findViewById(R.id.img_test);
        mTestResultField = view.findViewById(R.id.result_field);
        mTestResultImg = (ImageView) view.findViewById(R.id.ic_test_result);
        mTestResult = (TextView) view.findViewById(R.id.txt_test_result);
        mTestBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case BLUETOOTH_REQUEST_CODE:
                boolean result = data.getBooleanExtra("result", false);
                mTestResultField.setVisibility(View.VISIBLE);
                if (result) {
                    mTestResult.setText(getResources().getString(R.string.test_pass, "bluetooth"));
                    mTestResultImg.setImageResource(R.drawable.ic_test_pass);
                } else {
                    mTestResult.setText(getResources().getString(R.string.test_fail, "bluetooth"));
                    mTestResultImg.setImageResource(R.drawable.ic_test_fail);
                }
                mTestBtn.setText(R.string.btn_test_again);
                break;
        }
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
                i = new Intent(mActivity, TestingActivity.class);
                i.putExtra("title", mListener.getChangedTitle());
                startActivityForResult(i, BLUETOOTH_REQUEST_CODE);
                break;
        }
    }
}
