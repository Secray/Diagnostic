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

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.Log;

import java.io.IOException;

import static com.wingtech.diagnostic.util.Constants.SPEAK_REQUEST_CODE;


/**
 * Created by gaoweili on 17-7-28.
 */

public class SpeakerActivity extends BaseActivity {

    private String mContentDialog;
    private MediaPlayer player = null;
    private AudioManager localAudioManager = null;
    public static final String TAG = "SpeakerActivity";
    private TextView mTxt = null;
    private boolean isPlug = false;
    private HeadsetPlugReceiver mHPReceiver;
    AlertDialog dlg;
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

    }

    @Override
    protected void onWork() {

    }

    public void onPause()
    {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        prepareExit();
        sendResult(false);
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

    private void sendResult(boolean mResult) {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("result", mResult);
        setResult(SPEAK_REQUEST_CODE, intent);
        finish();
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
            mTitle.setText(R.string.speak_context_dialog_title);
            mContent.setText(R.string.speak_context_dialog_conteent);
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
                sendResult(false);
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendResult(true);
            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendResult(false);
            }
        });
        dlg = builder.create();
        dlg.show();
    }

    @Override
    public void onBackPressed() {
        prepareExit();
        sendResult(false);
    }

    private void prepareExit() {

        if(player != null) {
            player.release();
        }
        try{
            unregisterReceiver(mHPReceiver);
        }catch ( Exception e ) {
            Log.e(TAG, "unregister failed "+ e );
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
            Log.e(TAG,"can't play melody cause:"+e);
        }
    }

    private class HeadsetPlugReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.hasExtra("state"))
            {
                if (intent.getIntExtra("state", 0) == 1)
                {
                    //plug out
                    isPlug = false;
                    showTheDialog(false);
                    if(player != null){
                       player.release();
                    }

                }
                else if (intent.getIntExtra("state", 0) == 0) {
                    if(dlg!=null) {
                        dlg.dismiss();
                    }
                    isPlug = true;
                    mTxt.setText(R.string.headset_context_left);
                    localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    player = new MediaPlayer();
                    player.reset();
                    localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,15,0);
                    localAudioManager.setStreamVolume(AudioManager.STREAM_RING,15,0);

                    player.setVolume(1.0f,0.000f);/* ajayet invert to match headset */
                    playMelody(getResources(), R.raw.bootaudio);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if(isPlug){
                                mTxt.setText(R.string.headset_context_right);
                                localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                player = new MediaPlayer();
                                player.reset();
                                localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,15,0);
                                localAudioManager.setStreamVolume(AudioManager.STREAM_RING,15,0);

                                player.setVolume(0.000f,1.0f);/* ajayet invert to match headset */
                                playMelody(getResources(), R.raw.bootaudio);
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
                    });
                }

            }
        }
    }

}

