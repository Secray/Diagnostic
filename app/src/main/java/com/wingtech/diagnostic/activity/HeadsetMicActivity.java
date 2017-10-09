package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;

import static com.wingtech.diagnostic.util.Constants.HEADSETMIC_REQUEST_CODE;


/**
 * Created by gaoweili on 17-7-28.
 */

public class HeadsetMicActivity extends TestingActivity {

    private String mContentDialog;
    public static final String TAG = "HeadsetMicActivity";
    private TextView mTxt = null;
    private boolean isPlug = false;
    private HeadsetPlugReceiver mHPReceiver;
    AlertDialog dlg;
    private AudioManager localAudioManager = null;
    private String path = null;
    CountDownTimer mTimer;
    CountDownTimer mtimer;
    private boolean isShow = true;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_dialog_test;
    }

    @Override
    protected void initViews() {
        mTxt = (TextView) findViewById(R.id.dialog_txt);
        mContentDialog = getIntent().getStringExtra("title_dialog");
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/heassettest.pcm";
    }

    @Override
    protected void initToolbar() {
        mRequestCode = HEADSETMIC_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (!localAudioManager.isWiredHeadsetOn() && isShow) {
            isPlug = false;
            showTheDialog(false);
        }
    }

    public void onPause()
    {
        super.onPause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        mHPReceiver = new HeadsetPlugReceiver();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(mHPReceiver, intentFilter);

    }

    public void showTheDialog(boolean flag){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        TextView mTitle = (TextView) layout.findViewById(R.id.dialog_title);
        Button ok = (Button) layout.findViewById(R.id.ok);
        Button pass = (Button) layout.findViewById(R.id.pass);
        Button fail = (Button) layout.findViewById(R.id.fail);
        LinearLayout ll = (LinearLayout) layout.findViewById(R.id.result);
        if (!flag){
            mTitle.setText(R.string.headset_context_dialog_title);
            mContent.setText(R.string.headset_context_dialog_conteent);
            ok.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
        }else{
            mTitle.setText(R.string.dialog_title);
            if (mContentDialog != null){
                String sFormat = getResources().getString(R.string.dialog_context);
                String s = String.format(sFormat, mContentDialog);
                mContent.setText(s);
            }
            ok.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
        }



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dlg != null) {
                    dlg.dismiss();
                }
                mResult = false;
                sendResult();
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = true;
                sendResult();
            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = false;
                sendResult();
            }
        });
        dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimer != null){
            mTimer.cancel();
            mTimer=null;
        }
        if (mtimer != null){
            mtimer.cancel();
            mtimer=null;
        }
        stopPlayer();
        stopRecorder();
        mResult = false;
        sendResult();
    }

    private class HeadsetPlugReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.hasExtra("state"))
            {
                if (intent.getIntExtra("state", 0) == 0)
                {
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                    isShow = false;
                    //plug out
                    isPlug = false;
                    showTheDialog(false);
                }
                else if (intent.getIntExtra("state", 0) == 1) {
                    if(dlg!=null) {
                        dlg.dismiss();
                    }
                    isPlug = true;
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
                            if(isPlug) {
                                startPlayer(path);
                                mtimer = new CountDownTimer(5000, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onFinish() {
                                        stopPlayer();
                                        if(isPlug){
                                            showTheDialog(true);
                                        }
                                    }
                                }.start();
                            }
                        }
                    }.start();

                }

            }
        }
    }

    private MediaRecorder mRecorder;
    public boolean startRecorder(String path) {
        //设置音源为Micphone

        mRecorder = new MediaRecorder();
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
        try {
            //设置要播放的文件
            if (localAudioManager == null) {
                localAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            }
            mPlayer = new MediaPlayer();
            mPlayer.reset();
            localAudioManager.setMode(AudioManager.MODE_IN_CALL);
            localAudioManager.setSpeakerphoneOn(false);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

            int max = localAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            Log.i(TAG, "max=" + max);
            localAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,max,0);
            mPlayer.setVolume(13.0f, 13.0f);
            mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            //播放
            mPlayer.start();
        }catch(Exception e){
            Log.e(TAG, "prepare() failed");
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

