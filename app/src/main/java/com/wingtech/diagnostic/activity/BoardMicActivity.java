package com.wingtech.diagnostic.activity;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static com.wingtech.diagnostic.util.Constants.MIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class BoardMicActivity extends BaseActivity {
    private String mContentDialog;
    public static final String TAG = "BoardMicActivity";
    private TextView mTxt = null;
    private String path = null;
    CountDownTimer mTimer;
    CountDownTimer mtimer;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_dialog_test;
    }

    @Override
    protected void initViews() {
        mTxt = (TextView) findViewById(R.id.dialog_txt);
        mContentDialog = getIntent().getStringExtra("title_dialog");
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.pcm";
    }

    @Override
    protected void initToolbar() {

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
        sendResult(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void sendResult(boolean mResult) {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("result", mResult);
        setResult(MIC_REQUEST_CODE, intent);
        finish();
    }

    public void showTheDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        if (mContentDialog != null){
            String sFormat = getResources().getString(R.string.dialog_context);
            String s = String.format(sFormat, mContentDialog);
            mContent.setText(s);
        }
        Button pass = (Button) layout.findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendResult(true);
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendResult(false);
            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();
    }

    @Override
    public void onBackPressed() {
        sendResult(false);
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
            mPlayer = new MediaPlayer();
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

