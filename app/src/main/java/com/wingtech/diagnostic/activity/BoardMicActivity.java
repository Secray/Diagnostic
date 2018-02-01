package com.wingtech.diagnostic.activity;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;

import static com.wingtech.diagnostic.util.Constants.MIC_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class BoardMicActivity extends TestingActivity {
    private String mContentDialog;
    public static final String TAG = "BoardMicActivity";
    private TextView mTxt = null;
    private String path = null;
    CountDownTimer mTimer;
    CountDownTimer mtimer;
    private AudioManager localAudioManager = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.content_dialog_test;
    }

    @Override
    protected void initViews() {
        mTxt = (TextView) findViewById(R.id.dialog_txt);
        mContentDialog = getIntent().getStringExtra("title_dialog");
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pcm";
    }

    @Override
    protected void initToolbar() {
        mRequestCode = MIC_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mTxt.setText(R.string.mic_context_dialog_record);
        startRecorder(path);
        mTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFinish() {
                mTxt.setText(R.string.mic_context_dialog_play);

                stopRecorder();
                startPlayer(path);
                mtimer = new CountDownTimer(5000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onFinish() {
                        stopPlayer();
                        showTheDialog();
                    }
                }.start();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mtimer != null) {
            mtimer.cancel();
            mtimer = null;
        }
        stopPlayer();
        stopRecorder();
        mResult = false;
        sendResult();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showTheDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        if (mContentDialog != null) {
            String sFormat = getResources().getString(R.string.dialog_context);
            String s = String.format(sFormat, mContentDialog);
            mContent.setText(s);
        }
        Button pass = (Button) layout.findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = true;
                sendResult();
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = false;
                sendResult();
            }
        });
        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }

    private MediaRecorder mRecorder;

    public boolean startRecorder(String path) {
        //设置音源为Micphone
        mRecorder = new MediaRecorder();
        //mRecorder.setAudioChannels(1);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置封装格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(path);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        //录音
        mRecorder.start();
        return false;
    }

    public boolean stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        return false;

    }

    private MediaPlayer mPlayer;

    public boolean startPlayer(String path) {
//        try {
//            //设置要播放的文件
//            if(localAudioManager == null){
//                localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            }
//            localAudioManager.setMode(AudioManager.STREAM_MUSIC);
//            localAudioManager.setSpeakerphoneOn(true);
//            localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
//            mPlayer = new MediaPlayer();
//            mPlayer.reset();
//            mPlayer.setVolume(13.0f, 13.0f);
//            mPlayer.setDataSource(path);
//            mPlayer.prepare();
//            //播放
//            mPlayer.start();
//        } catch (Exception e) {
//            Log.e(TAG, "prepare() failed");
//        }
        if (mPlayer != null) {
            Log.e(TAG, "mPlayer = " + mPlayer);
            stopPlayer();
        }
        synchronized (this) {
            Log.d(TAG, "synchronized mPlayer start");
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(path);
                Log.d(TAG, "startplay mPlayer.prepare() start");
                mPlayer.prepare();
                mPlayer.start();
                Log.d(TAG, "startplay mPlayer.start() end");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "startplay mPlayer.prepare() fail");
            }
        }

        return false;
    }

    public boolean stopPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        return false;
    }


}

