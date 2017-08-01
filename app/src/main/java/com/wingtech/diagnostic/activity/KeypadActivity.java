package com.wingtech.diagnostic.activity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.widget.Toast;

/**
 * Created by gaoweili on 17-7-28.
 */

public class KeypadActivity extends BaseActivity {

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

    private int tag = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViews() {
        mUp = (CheckBox) findViewById(R.id.box_txt_1);
        mDown = (CheckBox) findViewById(R.id.box_txt_2);
        mHome = (CheckBox) findViewById(R.id.box_txt_3);
        mBack = (CheckBox) findViewById(R.id.box_txt_4);
        mRecent = (CheckBox) findViewById(R.id.box_txt_5);
        mUpTxt = (TextView) findViewById(R.id.txt_box_1);
        mDownTxt = (TextView) findViewById(R.id.txt_box_2);
        mHomeTxt = (TextView) findViewById(R.id.txt_box_3);
        mBackTxt = (TextView) findViewById(R.id.txt_box_4);
        mRecentTxt = (TextView) findViewById(R.id.txt_box_5);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);
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
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onWork() {
        mTitle.setText(String.format(getResources().getString(R.string.pad_title), mUp.getText().toString()));
    }

    private void changeBtn(int sum)
    {
        switch(sum)
        {
            case 0:

                tag++;
                break;
            case 1:
                tag++;
                break;
            case 2:
                tag++;
                break;
            case 3:
                tag++;
                break;
            case 4:
                tag++;
                break;

            default:

                break;
        }
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
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown keyCode: " + keyCode);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_HOME:
                Log.v(TAG,"KEYCODE_HOME");
                Toast.makeText(this,"KEYCODE_HOME",Toast.LENGTH_SHORT).show();
                is_homekey_pressed = true;
                break;
            case KeyEvent.KEYCODE_BACK:
                is_backkey_pressed = true;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(this,"KEYCODE_BACK",Toast.LENGTH_SHORT).show();
                is_upkey_pressed = true;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(this,"KEYCODE_VOLUME_DOWN",Toast.LENGTH_SHORT).show();
                is_downkey_pressed = true;
                break;
            case KeyEvent.KEYCODE_MENU:
                Toast.makeText(this,"KEYCODE_MENU",Toast.LENGTH_SHORT).show();
                is_recentkey_pressed = true;
                Log.d(TAG, "keyevent:menu");
                break;
            case KeyEvent.KEYCODE_SEARCH:
                Toast.makeText(this,"KEYCODE_SEARCH",Toast.LENGTH_SHORT).show();
                is_recentkey_pressed = true;
                break;
            case KeyEvent.KEYCODE_APP_SWITCH:
                Toast.makeText(this,"KEYCODE_APP_SWITCH",Toast.LENGTH_SHORT).show();
                is_recentkey_pressed = true;
                Log.d(TAG, "keyevent:switch");
                break;

            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }
}
