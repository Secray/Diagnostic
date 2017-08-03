package com.wingtech.diagnostic.fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;

/**
 * Created by gaoweili on 17-7-28.
 */

public class KeypadFragment extends TestFragment {

    private static final String TAG = "KeypadActivity";

    private CheckBox mUp = null;
    private CheckBox mDown = null;
    private CheckBox mHome = null;
    private CheckBox mBack = null;
    private CheckBox mRecent = null;
    private TextView mUpTxt = null;
    private TextView mDownTxt = null;
    private TextView mHomeTxt = null;
    private TextView mBackTxt = null;
    private TextView mRecentTxt = null;
    private TextView mTitle = null;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViewEvents(View view) {
        mUp = (CheckBox) view.findViewById(R.id.box_txt_1);
        mDown = (CheckBox) view.findViewById(R.id.box_txt_2);
        mHome = (CheckBox) view.findViewById(R.id.box_txt_3);
        mBack = (CheckBox) view.findViewById(R.id.box_txt_4);
        mRecent = (CheckBox) view.findViewById(R.id.box_txt_5);
        mUpTxt = (TextView) view.findViewById(R.id.txt_box_1);
        mDownTxt = (TextView) view.findViewById(R.id.txt_box_2);
        mHomeTxt = (TextView) view.findViewById(R.id.txt_box_3);
        mBackTxt = (TextView) view.findViewById(R.id.txt_box_4);
        mRecentTxt = (TextView) view.findViewById(R.id.txt_box_5);
        mTitle = (TextView) view.findViewById(R.id.activity_checkbox_title);
        mUp.setVisibility(View.VISIBLE);
        mDown.setVisibility(View.VISIBLE);
        mHome.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mRecent.setVisibility(View.VISIBLE);
        mUpTxt.setVisibility(View.VISIBLE);
        mDownTxt.setVisibility(View.VISIBLE);
        mHomeTxt.setVisibility(View.VISIBLE);
        mBackTxt.setVisibility(View.VISIBLE);
        mRecentTxt.setVisibility(View.VISIBLE);
        myWork();
    }


    protected void myWork() {
        mTitle.setText(String.format(getResources().getString(R.string.pad_title), mUp.getText().toString()));
    }

    @Override
    public void onPause() {
        super.onPause();
        setEngineeringMode(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setEngineeringMode(true);
    }

    private void setEngineeringMode(boolean isEng) {
        android.provider.Settings.System.putInt(mActivity.getContentResolver(), "EngineeringMode", isEng?1:0);
    }

    //@Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown keyCode: " + keyCode);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_HOME:
                mHome.setChecked(true);
                break;
            case KeyEvent.KEYCODE_BACK:
                mBack.setChecked(true);
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                mUp.setChecked(true);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mDown.setChecked(true);
                break;
            case KeyEvent.KEYCODE_MENU:
                mRecent.setChecked(true);
                break;
            case KeyEvent.KEYCODE_SEARCH:
                mRecent.setChecked(true);
                break;
            case KeyEvent.KEYCODE_APP_SWITCH:
                mRecent.setChecked(true);
                mCallback.onChange(true);
                break;

            default:
                return mActivity.onKeyDown(keyCode, event);
        }
        return true;
    }
}
