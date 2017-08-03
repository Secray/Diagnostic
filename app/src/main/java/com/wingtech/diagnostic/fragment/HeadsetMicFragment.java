package com.wingtech.diagnostic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.SingleTestActivity;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;

import static com.wingtech.diagnostic.util.Constants.HEADSETMIC_REQUEST_CODE;


/**
 * Created by gaoweili on 17-7-28.
 */

public class HeadsetMicFragment extends TestFragment {

    private String mContentDialog;
    public static final String TAG = "HeadsetMicActivity";
    private TextView mTxt = null;
    private boolean isPlug = false;
    private HeadsetPlugReceiver mHPReceiver;
    AlertDialog dlg;

    private String path = null;
    CountDownTimer mTimer;
    CountDownTimer mtimer;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_dialog_test;
    }

    @Override
    protected void initViewEvents(View view) {
        mTxt = (TextView) view.findViewById(R.id.dialog_txt);
        mContentDialog = mActivity.getIntent().getStringExtra("title_dialog");
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/heassettest.pcm";
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        mHPReceiver = new HeadsetPlugReceiver();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        mActivity.registerReceiver(mHPReceiver, intentFilter);

    }


    public void showTheDialog(boolean flag){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
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
                mCallback.onChange(false);

            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.onChange(true);

            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.onChange(false);

            }
        });
        dlg = builder.create();
        dlg.show();
    }


    @Override
    public void onStop() {
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
        mCallback.onChange(false);

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

