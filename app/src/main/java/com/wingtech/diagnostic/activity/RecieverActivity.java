package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class RecieverActivity extends TestingActivity implements View.OnClickListener {
    private static final int[] EN_SOURCE = {R.raw.en1, R.raw.en2, R.raw.en3, R.raw.en4, R.raw.en5};
    private static final int[] CN_SOURCE = {R.raw.zh1, R.raw.zh2, R.raw.zh3, R.raw.zh4, R.raw.zh5};
    private MediaPlayer player = null;
    private int oldVolume;
    private int audio_mode = AudioManager.MODE_NORMAL;
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
        findViewById(R.id.action_one).setOnClickListener(this);
        findViewById(R.id.action_two).setOnClickListener(this);
        findViewById(R.id.action_three).setOnClickListener(this);
        findViewById(R.id.action_four).setOnClickListener(this);
        findViewById(R.id.action_five).setOnClickListener(this);
        findViewById(R.id.action_fail).setOnClickListener(this);
        Random r = new Random();
        mIndex = r.nextInt(5);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = RECIEVER_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mTxt.setText(getIntent().getStringExtra("title"));
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(localAudioManager.isSpeakerphoneOn())
        {
            Log.i(TAG, "isSpeakerphoneOn ");
            localAudioManager.setSpeakerphoneOn(false);
        }
        audio_mode = localAudioManager.getMode();
        localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        oldVolume = localAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = localAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Locale l = Locale.getDefault();
        int[] mRes;
        Log.d("country = " + l.getCountry());
        if (l.getCountry().contains("CN")) {
            mRes = CN_SOURCE;
        } else {
            mRes = EN_SOURCE;
        }

        localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        player = new MediaPlayer();
        player.reset();
        //player.setVolume(0.0f, 0.000f);/* ajayet invert to match headset */
        player.setVolume(13.0f, 13.0f);/* ajayet invert to match headset */
        playMelody(getResources(), mRes[mIndex]);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

//                localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                player = new MediaPlayer();
//                player.reset();
//
//                player.setVolume(13.0f, 13.0f);/* ajayet invert to match headset */
//                mTxt.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        playMelody(getResources(), mRes[mIndex]);
//                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                isCompleted = true;
//                            }
//                        });
//                    }
//                }, 550);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            if(player!=null)
                                player.start();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                };
               new Timer().schedule(timerTask,1000);
            }

        });
    }

    private void playMelody(Resources resources, int res) {
        AssetFileDescriptor afd = resources.openRawResourceFd(res);
        try {

            if (afd != null) {
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                        afd.getLength());
                afd.close();
            }
            //player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "can't play melody cause:" + e);
        }
    }

    public void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void onDestroy() {
        if (localAudioManager != null) {
            localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, oldVolume, 0);
            localAudioManager.setMode(audio_mode);
            localAudioManager.setSpeakerphoneOn(true);
        }

        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
//        if (!isCompleted) {
//            return;
//        }
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
