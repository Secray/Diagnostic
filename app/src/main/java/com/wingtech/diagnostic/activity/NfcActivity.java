package com.wingtech.diagnostic.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;

import static com.wingtech.diagnostic.util.Constants.NFC_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class NfcActivity extends TestingActivity {

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
    protected void initViews() {
        mNfc = (CheckBox) findViewById(R.id.box_txt_1);
        mNfcTxt = (TextView) findViewById(R.id.txt_box_1);
        mTitle = (TextView) findViewById(R.id.activity_checkbox_title);
        mTouchFailBtn = (Button) findViewById(R.id.fail_btn);
        mNfc.setVisibility(View.VISIBLE);
        mNfcTxt.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = NFC_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mTitle.setText(R.string.nfc_title);
        mNfcTxt.setText(R.string.nfc_txt);

        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }
}
