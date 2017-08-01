package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.DISPLAY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETKEY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class HeadsetKeyActivity extends BaseActivity {

    private static final String TAG = "HeadsetKeyActivity";

    private CheckBox mHeadsetKey = null;
    private TextView mHeadsetKeyTxt = null;
    private TextView mTitle = null;
    private Button mTouchFailBtn = null;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViews() {
        mHeadsetKey = (CheckBox) findViewById(R.id.box_txt_1);
        mHeadsetKeyTxt = (TextView) findViewById(R.id.txt_box_1);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);
        mTouchFailBtn = (Button) findViewById(R.id.touch_fail_btn);
        mHeadsetKey.setVisibility(View.VISIBLE);
        mHeadsetKeyTxt.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onWork() {
        mTitle.setText(R.string.headsetkey_title);
        mHeadsetKeyTxt.setText(R.string.headsetkey_txt);

        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(false);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown keyCode: " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                sendResult(false);
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_CALL:
            case KeyEvent.KEYCODE_ENDCALL:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                Log.i(TAG,"onKeyDown keyCode setChecked: " + keyCode);
                mHeadsetKey.setChecked(true);
                sendResult(true);
                return true;
            default:
                return false;
        }
    }
    private void sendResult(boolean mResult) {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("result", mResult);
        setResult(HEADSETKEY_REQUEST_CODE, intent);
        finish();
    }

}
