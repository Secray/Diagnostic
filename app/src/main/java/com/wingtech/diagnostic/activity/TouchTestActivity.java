package com.wingtech.diagnostic.activity;

import android.app.Dialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.dialog.TouchDialog;
import com.wingtech.diagnostic.widget.TPTestView;

import static com.wingtech.diagnostic.util.Constants.TOUCH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class TouchTestActivity extends TestingActivity implements TouchDialog.ResultCallback {
    TouchDialog mDialog;

    @Override
    protected void onWork() {
        super.onWork();
        mRequestCode = TOUCH_REQUEST_CODE;
        mDialog = new TouchDialog(this);
        mDialog.setCallback(this);
        mDialog.show();
    }

    @Override
    public void onCallback(boolean result) {
        mResult = result;
        sendResult();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
