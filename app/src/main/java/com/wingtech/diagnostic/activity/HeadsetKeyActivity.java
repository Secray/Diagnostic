package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.HEADSETKEY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class HeadsetKeyActivity extends TestingActivity {
    private static final String TAG = "HeadsetKeyActivity";
    private boolean isShow = true;
    private CheckBox mHeadsetKey = null;
    private TextView mHeadsetKeyTxt = null;
    private TextView mTitle = null;
    private Button mTouchFailBtn = null;
    private boolean isPlug = false;
    private HeadsetPlugReceiver mHPReceiver;
    private String mContentDialog;
    private AudioManager localAudioManager = null;
    AlertDialog dlg;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViews() {
        mHeadsetKey = (CheckBox) findViewById(R.id.box_txt_1);
        mHeadsetKeyTxt = (TextView) findViewById(R.id.txt_box_1);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);
        mTouchFailBtn = (Button) findViewById(R.id.fail_btn);
        mHeadsetKeyTxt.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = HEADSETKEY_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mTitle.setText(R.string.headsetkey_title);
        mHeadsetKeyTxt.setText(R.string.headsetkey_txt);
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (!localAudioManager.isWiredHeadsetOn() && isShow) {
            isPlug = false;
            showTheDialog(false);
        }
        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        mHPReceiver = new HeadsetPlugReceiver();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(mHPReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHPReceiver);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown keyCode: " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mResult = false;
                sendResult();
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_CALL:
            case KeyEvent.KEYCODE_ENDCALL:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                Log.i(TAG,"onKeyDown keyCode setChecked: " + keyCode);
                mHeadsetKey.setChecked(true);
                mHeadsetKey.setVisibility(View.VISIBLE);
                mResult = true;
                sendResult();
                return true;
            default:
                return false;
        }
    }
    public void showTheDialog(boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        TextView mTitle = (TextView) layout.findViewById(R.id.dialog_title);
        Button ok = (Button) layout.findViewById(R.id.ok);
        Button pass = (Button) layout.findViewById(R.id.pass);
        Button fail = (Button) layout.findViewById(R.id.fail);
        LinearLayout ll = (LinearLayout) layout.findViewById(R.id.result);
        if (!flag) {
            mTitle.setText(R.string.headset_context_dialog_title);
            mContent.setText(R.string.headset_context_dialog_conteent);
            ok.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
        } else {
            mTitle.setText(R.string.dialog_title);
            if (mContentDialog != null) {
                String sFormat = getResources().getString(R.string.dialog_context);
                String s = String.format(sFormat, mContentDialog);
                mContent.setText(s);
            }
            ok.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
        }


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dlg != null) {
                    dlg.dismiss();
                }
                mResult = false;
                sendResult();
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = true;
                sendResult();
            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = false;
                sendResult();
            }
        });
        dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }
    private class HeadsetPlugReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                    isShow = false;
                    showTheDialog(false);
                } else if (intent.getIntExtra("state", 0) == 1) {
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                }
            }
        }
    }

}


