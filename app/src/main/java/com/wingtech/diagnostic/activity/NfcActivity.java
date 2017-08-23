package com.wingtech.diagnostic.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingtech.diagnostic.R;

import java.nio.charset.Charset;
import java.util.Locale;

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

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;

    private boolean isReaded;;
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
        resolveIntent(getIntent());
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});

        mTouchFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {

            }
            mAdapter.enableForegroundDispatch(NfcActivity.this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(NfcActivity.this, mNdefPushMessage);
        }
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
            startActivity(getIntent());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            isReaded = isReadCard(tag);
            if(isReaded){
                mNfc.setChecked(true);
                mNfc.setVisibility(View.VISIBLE);
                mResult = true;
                sendResult();
            }
        }
    }



    private boolean isReadCard(Parcelable p) {
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        getHex(id);
        getDec(id);
        getReversed(id);
        String[] strList = tag.getTechList();
        if(getHex(id).length()>0||getDec(id)||getReversed(id)||strList.length>0)
            return true;
        return false;
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private boolean getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        if(String.valueOf(result).length()>0)
            return true;
        return false;
    }

    private boolean getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        if(String.valueOf(result).length()>0)
            return true;
        return false;
    }

}
