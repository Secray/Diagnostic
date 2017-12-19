package com.wingtech.diagnostic.activity;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import com.asus.atd.smmitest.R;

import static com.wingtech.diagnostic.util.Constants.KEYPAD_REQUEST_CODE;

/**
 * Created by xiekui on 17-11-23.
 */

public class VirtualKeyActivity extends TestingActivity implements View.OnClickListener {

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

    private boolean is_backkey_pressed = false;
    private boolean is_homekey_pressed = false;
    private boolean is_upkey_pressed = false;
    private boolean is_downkey_pressed = false;
    private boolean is_recentkey_pressed = true;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViews() {
        mUp = (CheckBox) findViewById(R.id.box_txt_1);
        mDown = (CheckBox) findViewById(R.id.box_txt_2);
        mBack = (CheckBox) findViewById(R.id.box_txt_3);
        mHome = (CheckBox) findViewById(R.id.box_txt_4);
        mRecent = (CheckBox) findViewById(R.id.box_txt_5);
        mUpTxt = (TextView) findViewById(R.id.txt_box_1);
        mDownTxt = (TextView) findViewById(R.id.txt_box_2);
        mBackTxt = (TextView) findViewById(R.id.txt_box_3);
        mHomeTxt = (TextView) findViewById(R.id.txt_box_4);
        mRecentTxt = (TextView) findViewById(R.id.txt_box_5);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);

        mUpTxt.setText(R.string.volume_up);
        mDownTxt.setText(R.string.volume_down);
        mHomeTxt.setText(R.string.home);
        mBackTxt.setText(R.string.back);
        mRecentTxt.setText(R.string.recent);
        mRecentTxt.setVisibility(View.VISIBLE);
        mUpTxt.setVisibility(View.VISIBLE);
        mDownTxt.setVisibility(View.VISIBLE);
        mHomeTxt.setVisibility(View.VISIBLE);
        mBackTxt.setVisibility(View.VISIBLE);
        findViewById(R.id.fail_btn).setOnClickListener(this);
    }

    @Override
    protected void onWork() {
        mRequestCode = KEYPAD_REQUEST_CODE;
        mTitle.setText(getResources().getString(R.string.pad_title, mUpTxt.getText().toString()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        setEngineeringMode(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEngineeringMode(true);
    }

    private void setEngineeringMode(boolean isEng) {
        android.provider.Settings.System.putInt(getContentResolver(), "EngineeringMode", isEng?1:0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown keyCode: " + keyCode);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_HOME:
                if (is_backkey_pressed) {
                    is_homekey_pressed = true;
                    mHome.setChecked(true);
                    mHome.setVisibility(View.VISIBLE);
                    mTitle.setText(getResources().getString(R.string.pad_title, mRecentTxt.getText().toString()));
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if(is_downkey_pressed) {
                    is_backkey_pressed = true;
                    mBack.setChecked(true);
                    mBack.setVisibility(View.VISIBLE);
                    mTitle.setText(getResources().getString(R.string.pad_title, mHomeTxt.getText().toString()));
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                is_upkey_pressed = true;
                mUp.setChecked(true);
                mUp.setVisibility(View.VISIBLE);
                mTitle.setText(getResources().getString(R.string.pad_title, mDownTxt.getText().toString()));
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (is_upkey_pressed) {
                    is_downkey_pressed = true;
                    mDown.setChecked(true);
                    mDown.setVisibility(View.VISIBLE);
                    mTitle.setText(getResources().getString(R.string.pad_title, mBackTxt.getText().toString()));
                }
                break;
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_SEARCH:
            case KeyEvent.KEYCODE_APP_SWITCH:
                if (is_homekey_pressed) {
                    is_recentkey_pressed = true;
                    mRecent.setChecked(true);
                    mRecent.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mResult = true;
                            sendResult();
                        }
                    }, 500);
                }
                break;

            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        mResult = false;
        sendResult();
    }
}
