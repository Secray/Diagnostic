package com.wingtech.diagnostic.activity;

import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.widget.TPTestView;

import static com.wingtech.diagnostic.util.Constants.TOUCH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class TouchTestActivity extends TestingActivity {

    private TPTestView lpwv;
    public static AppCompatButton mFail = null;
    public static boolean mBtnVisiable;

    @Override
    protected int getLayoutResId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.tpscreen;
    }

    @Override
    protected void initViews() {
        //findViewById(R.id.touch_fail_btn).setOnClickListener(this);
        lpwv = (TPTestView) findViewById(R.id.mLocusViewTP);
        mFail = (AppCompatButton) findViewById(R.id.touch_fail_btn);
        hideVirtulKey(View.GONE);
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onWork() {
        mRequestCode = TOUCH_REQUEST_CODE;
        lpwv.setOnCompleteListenerTP(new TPTestView.OnCompleteListenerTP() {
            @Override
            public void onComplete(boolean bResult) {
                if (bResult) {
                    mResult = true;
                    sendResult();
                } else {
                    mResult = false;
                    sendResult();
                }
            }
        });
        mFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideVirtulKey(View.VISIBLE);
    }

    private void hideVirtulKey(int visibility) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(visibility);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
