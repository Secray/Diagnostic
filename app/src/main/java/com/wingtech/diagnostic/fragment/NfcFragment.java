package com.wingtech.diagnostic.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.SingleTestActivity;

import static com.wingtech.diagnostic.util.Constants.NFC_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class NfcFragment extends TestFragment {

    private static final String TAG = "NfcActivity";

    private CheckBox mNfc = null;
    private TextView mNfcTxt = null;
    private TextView mTitle = null;
    private Button mTouchFailBtn = null;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkbox;
    }

    @Override
    protected void initViewEvents(View view) {
        mNfc = (CheckBox) view.findViewById(R.id.box_txt_1);
        mNfcTxt = (TextView) view.findViewById(R.id.txt_box_1);
        mTitle = (TextView) view.findViewById(R.id.activity_checkbox_title);
        mTouchFailBtn = (Button) view.findViewById(R.id.touch_fail_btn);
        mNfc.setVisibility(View.VISIBLE);
        mNfcTxt.setVisibility(View.VISIBLE);
        myWork();
    }

    protected void myWork() {
        mTitle.setText(R.string.nfc_title);
        mNfcTxt.setText(R.string.nfc_txt);

        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onChange(false);
            }
        });
    }




}
