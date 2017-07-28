package com.wingtech.diagnostic.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.wingtech.diagnostic.R;


/**
 * @author xiekui
 * @date 2017-7-20
 */

public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Test_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        initView();
        doWork();
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.getDecorView().setPadding(0, 0, 0, 0);
    }

    protected abstract @LayoutRes int getContentResId();
    protected abstract void initView();
    protected abstract void doWork();
}
