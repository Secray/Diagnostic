package com.wingtech.diagnostic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;

import static com.wingtech.diagnostic.util.Constants.SPEAK_REQUEST_CODE;


/**
 * Created by gaoweili on 17-7-28.
 */

public class SpeakerActivity extends TestingActivity {

    private String mContentDialog;
    private MediaPlayer player = null;
    private AudioManager localAudioManager = null;
    public static final String TAG = "SpeakerActivity";
    private TextView mTxt = null;
    private boolean isPlug = false;
    private HeadsetPlugReceiver mHPReceiver;
    AlertDialog dlg;
	private boolean isShow;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_dialog_test;
    }

    @Override
    protected void initViews() {
        mTxt = (TextView) findViewById(R.id.dialog_txt);
        mContentDialog = getIntent().getStringExtra("title_dialog");
    }

    @Override
    protected void initToolbar() {
        mRequestCode = SPEAK_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mTxt.setText(getIntent().getStringExtra("title"));
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (!localAudioManager.isWiredHeadsetOn()) {
            player = new MediaPlayer();
            player.reset();
            localAudioManager.setMode(AudioManager.MODE_IN_CALL);
            localAudioManager.setSpeakerphoneOn(true);
            localAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 15, 0);
            player.setVolume(13.0f, 13.0f);/* ajayet invert to match headset */
            playMelody(getResources(), R.raw.heyasus_left_channel);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    showTheDialog(true);
                }
            });
            isShow = true;
        }
    }

    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prepareExit();
        mResult = false;
        sendResult();
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

    public void showTheDialog(boolean flag) {
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
        if (!flag) {
            mTitle.setText(R.string.speak_context_dialog_title);
            mContent.setText(R.string.speak_context_dialog_conteent);
            ok.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
        } else {
            mTitle.setText(R.string.dialog_title);
            if (mContentDialog != null) {
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
    public void onBackPressed() {
        prepareExit();
        super.onBackPressed();
    }

    private void prepareExit() {

        if (dlg != null) {
            dlg.dismiss();
        }
        //plug out
        isPlug = false;

        if (player != null) {
            player.release();
        }
        try {
            unregisterReceiver(mHPReceiver);
        } catch (Exception e) {
            Log.e(TAG, "unregister failed " + e);
        }
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

    private class HeadsetPlugReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 1) {
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                    //plug out
                    isPlug = false;
                    isShow = false;
                    if (player != null) {
                        player.release();
                    }
                    showTheDialog(false);

                } else if (intent.getIntExtra("state", 0) == 0 && !isShow) {
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                    isPlug = true;
                    player = new MediaPlayer();
                    player.reset();
                    localAudioManager.setMode(AudioManager.MODE_IN_CALL);
                    localAudioManager.setSpeakerphoneOn(true);
                    localAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 15, 0);

                    player.setVolume(13.0f, 13.0f);/* ajayet invert to match headset */
                    playMelody(getResources(), R.raw.heyasus_left_channel);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (isPlug) {
                                showTheDialog(true);
                            }
                        }
                    });
                }

            }
        }
    }

}

