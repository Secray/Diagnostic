package com.wingtech.diagnostic.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.listener.OnPointsChangedListener;
import com.wingtech.diagnostic.util.Utils;
import com.wingtech.diagnostic.widget.MultiTouchView;

import static com.wingtech.diagnostic.util.Constants.MULTI_TOUCH_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-31
 */

public class MultiTouchTestingActivity extends TestingActivity implements
        OnPointsChangedListener, View.OnClickListener {
    private static final int MESSAGE_WHAT = 0;
    private FrameLayout mTouchLayout;
    private static final int MARGIN_TOP = 16;
    private TextView[] mTextViewArray;
    private Handler mHandler;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_multi_touch;
    }

    @Override
    protected void initViews() {
        mTouchLayout = (FrameLayout) findViewById(R.id.touch_layout);
        MultiTouchView multiTouchView = (MultiTouchView) findViewById(R.id.multi_touch_view);
        AppCompatButton appCompatButton = (AppCompatButton) findViewById(R.id.touch_fail_btn);
        appCompatButton.setOnClickListener(this);
        multiTouchView.setOnPointsChangedListener(this);
        TextView txtFingerOne = new TextView(this);
        TextView txtFingerTwo = new TextView(this);
        TextView txtFingerThree = new TextView(this);
        TextView txtFingerFour = new TextView(this);
        TextView txtFingerFive = new TextView(this);
        mTextViewArray = new TextView[]{txtFingerOne, txtFingerTwo,
                txtFingerThree, txtFingerFour, txtFingerFive};
        initTextView(mTextViewArray);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mResult = true;
                sendResult();
            }
        };
    }

    void initTextView(TextView[] txts) {
        for (int i = 0; i < txts.length; i++) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int)(Utils.dp2px(this, MARGIN_TOP) + txts[i].getHeight()) * i, 0, 0);
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            txts[i].setLayoutParams(lp);
            txts[i].setVisibility(View.GONE);
            mTouchLayout.addView(txts[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onWork() {
        mRequestCode = MULTI_TOUCH_REQUEST_CODE;
    }

    @Override
    public void onChange(ArrayMap<Integer, MultiTouchView.Point> points) {
        int size = points.size();
        for (int i = 0; i < size; i++) {
            if (i < MultiTouchView.COLORS.length) {
                mTextViewArray[i].setText("Finger" + i +"          x = " +
                        "" + points.get(points.keyAt(i)).x + "              " +
                        "y = " + points.get(points.keyAt(i)).y);
                mTextViewArray[i].setTextColor(MultiTouchView.COLORS[i]);
                mTextViewArray[i].setVisibility(View.VISIBLE);
            }
        }
        if (size < mTextViewArray.length) {
            for (int i = size; i < mTextViewArray.length; i++) {
                mTextViewArray[i].setVisibility(View.GONE);
            }
        }
        if (size == 5) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        mResult = false;
        sendResult();
    }
}
