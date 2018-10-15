package com.wingtech.diagnostic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

import com.wingtech.diagnostic.util.Log;

public class MusicService extends Service {
    public MusicService() {
    }

    private AudioManager mAm;
    private static boolean vIsActive=false;
    private MyOnAudioFocusChangeListener mListener;
    public class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            // TODO Auto-generated method stub
        }
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAm = (AudioManager) getApplicationContext().getSystemService(
                Context.AUDIO_SERVICE);
        vIsActive=mAm.isMusicActive();
        mListener = new MyOnAudioFocusChangeListener();
        if(vIsActive)
        {
            int result = mAm.requestAudioFocus(mListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            {
                Log.d("requestAudioFocus successfully.");
            }
            else
            {
                Log.d("requestAudioFocus failed.");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(vIsActive)
        {
            mAm.abandonAudioFocus(mListener);
        }
        Log.d("myservice onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
