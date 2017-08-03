package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.SingleTestActivity;
import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class RecieverFragment extends TestFragment {

    private String mContentDialog;
    private MediaPlayer player = null;
    private AudioManager localAudioManager = null;
    private int audio_mode = AudioManager.MODE_NORMAL;
    private int oldVolume;
    public static final String TAG = "RecieverActivity";
    private boolean isPlayerStoped  = false;
    private TextView mTxt = null;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_dialog_test;
    }

    @Override
    protected void initViewEvents(View view) {
        mTxt = (TextView) view.findViewById(R.id.dialog_txt);
        localAudioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);//"audio";
        mContentDialog = mActivity.getIntent().getStringExtra("title_dialog");
    }


    @Override
    protected void onWork() {
        mTxt.setText(mActivity.getIntent().getStringExtra("context"));
        audio_mode = localAudioManager.getMode();
        localAudioManager.setMode(AudioManager.MODE_IN_CALL);
        localAudioManager.setSpeakerphoneOn(false);
        Log.i(TAG, "isSpeakerphoneOn :" +  localAudioManager.isSpeakerphoneOn());
        int maxVolume = localAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        oldVolume = localAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,0);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(player.isPlaying())
        {
            player.stop();
            player.release();
            player = null;
        }
        if(localAudioManager!=null){
            localAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, oldVolume,0);
        }
        localAudioManager.setMode(audio_mode);
    }

    @Override
    public void onDestroy()
    {
        if(player != null)
        {
            player.release();
            player = null;
        }
        isPlayerStoped = false;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if(isPlayerStoped == true){
            return;
        }
        if(player == null){
            player = MediaPlayer.create(mActivity, R.raw.bootaudio);
        }else{
            player.stop();
            player.release();
            player = null;
            player = MediaPlayer.create(mActivity, R.raw.bootaudio);
        }
        //player.setLooping(true);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                showTheDialog();
            }
        });
    }


    public void showTheDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
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
                mCallback.onChange(true);
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.onChange(false);
            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();
    }

}
