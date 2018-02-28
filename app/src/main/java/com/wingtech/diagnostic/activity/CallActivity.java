package com.wingtech.diagnostic.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.CALL_REQUEST_CODE;

/**
 * Created by xiekui on 2017/9/29 0029.
 */

public class CallActivity extends TestingActivity implements View.OnClickListener {
    private TextInputLayout mPhoneWrapper;
    private EditText mPhoneNumber;
    private AppCompatButton mCallAction;
    private boolean mIsCalled;
    private AlertDialog dlg;
    @Override
    protected void onWork() {
        mTitle = getIntent().getStringExtra("title");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_call;
    }

    @Override
    protected void initToolbar() {
        mRequestCode = CALL_REQUEST_CODE;
    }

    @Override
    protected void initViews() {
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mPhoneWrapper = (TextInputLayout) findViewById(R.id.phone_number_wrapper);
        mPhoneWrapper.setHint(getString(R.string.phone_number));
        mCallAction = (AppCompatButton) findViewById(R.id.call_action);
        mCallAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String number = mPhoneNumber.getText().toString();
        if (!"".equals(number)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }

    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i("state idle");
                    if (mIsCalled) {
                        try {
                            showTheDialog();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    mIsCalled = false;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("CustomPhoneStateListener onCallStateChanged endCall");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mIsCalled = true;
                    Log.i("state offhook");
                    break;
            }
        }
    };

    public void showTheDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        if (mTitle != null) {
            String sFormat = getResources().getString(R.string.dialog_context);
            String s = String.format(sFormat, mTitle);
            mContent.setText(s);
        }
        Button pass = (Button) layout.findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = true;
                sendResult();
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = false;
                sendResult();
            }
        });
        dlg = builder.create();
        dlg.show();
    }

}
