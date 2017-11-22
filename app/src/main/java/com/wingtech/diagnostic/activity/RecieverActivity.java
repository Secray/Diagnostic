package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class RecieverActivity extends TestingActivity implements View.OnClickListener {
    private static final int []EN_SOURCE = {R.raw.en1, R.raw.en2, R.raw.en3, R.raw.en4, R.raw.en5};
    private static final int []CN_SOURCE = {R.raw.zh1, R.raw.zh2, R.raw.zh3, R.raw.zh4, R.raw.zh5};
    private String mContentDialog;
    private MediaPlayer player = null;
    private int oldVolume;
    public static final String TAG = "RecieverActivity";
    private boolean isCompleted;
    private int mIndex;
    private TextView mTxt = null;
    private AudioManager localAudioManager = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common;
    }

    @Override
    protected void initViews() {
        mTxt = (TextView) findViewById(R.id.test_title);
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);//"audio";
        mContentDialog = getIntent().getStringExtra("title_dialog");
        findViewById(R.id.action_one).setOnClickListener(this);
        findViewById(R.id.action_two).setOnClickListener(this);
        findViewById(R.id.action_three).setOnClickListener(this);
        findViewById(R.id.action_four).setOnClickListener(this);
        findViewById(R.id.action_five).setOnClickListener(this);
        findViewById(R.id.action_fail).setOnClickListener(this);
        Random r = new Random();
        mIndex = r.nextInt(4);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = RECIEVER_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mTxt.setText(getIntent().getStringExtra("title"));
        //localAudioManager.setMode(AudioManager.MODE_IN_CALL);
        //localAudioManager.setSpeakerphoneOn(false);
        //Log.i(TAG, "isSpeakerphoneOn :" + localAudioManager.isSpeakerphoneOn());
        oldVolume = localAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);

        Locale l = Locale.getDefault();
        int[] mRes;
        Log.d("country = " + l.getCountry());
        if (l.getCountry().contains("zh")) {
            Log.d("zh");
            mRes = CN_SOURCE;
        } else {
            Log.d("en");
            mRes = EN_SOURCE;
        }

        localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        if (player == null) {
            player = MediaPlayer.create(this, mRes[mIndex]);
        } else {
            player.stop();
            player.release();
            player = null;
            player = MediaPlayer.create(this, mRes[mIndex]);
        }
        //player.setLooping(true);
        player.setOnPreparedListener(mp -> {
            Log.d("onPrepared");
            mp.start();
        });
        player.setOnCompletionListener(mp -> {
            //showTheDialog();
            isCompleted = true;
        });
    }

    public void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
        if (localAudioManager != null) {
            localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, oldVolume, 0);
            localAudioManager.setMode(AudioManager.STREAM_MUSIC);
            localAudioManager.setSpeakerphoneOn(true);
        }

    }

    public void onDestroy() {
        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (!isCompleted) {
            return;
        }
        switch (v.getId()) {
            case R.id.action_one:
                if (mIndex == 0) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_two:
                if (mIndex == 1) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_three:
                if (mIndex == 2) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_four:
                if (mIndex == 3) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_five:
                if (mIndex == 4) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_fail:
                mResult = false;
                break;
        }
        sendResult();
    }
}
