package com.wingtech.diagnostic.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.widget.TPTestView;

public class TouchDialog extends Dialog implements TPTestView.OnCallback {
    Context mContext;
    TPTestView lpwv;
    AppCompatButton mFail = null;
    ResultCallback callback;

    public void setCallback(ResultCallback callback) {
        this.callback = callback;
    }

    public TouchDialog(@NonNull Context context) {
        super(context, R.style.TouchDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpscreen);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.type = 2020;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

        lpwv = (TPTestView) findViewById(R.id.mLocusViewTP);
        mFail = (AppCompatButton) findViewById(R.id.touch_fail_btn);
        lpwv.setCallback(this);
        lpwv.setOnCompleteListenerTP(new TPTestView.OnCompleteListenerTP() {
            @Override
            public void onComplete(boolean bResult) {
                callback.onCallback(bResult);
            }
        });
        mFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCallback(false);
            }
        });

    }

    @Override
    public void show() {
        super.show();
        View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void callback(boolean callback) {
        if (callback) {
            mFail.setVisibility(View.GONE);
        } else {
            mFail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callback.onCallback(false);
    }

    public interface ResultCallback {
        void onCallback(boolean result);
    }
}
