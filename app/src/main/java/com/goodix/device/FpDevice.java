package com.goodix.device;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class FpDevice implements IDevice {

    private static final String TAG = "FpSetting";
    private long mNativeContext;
    private EventHandler mEventHandler = null;
    private static FpDevice mFpDevice = null;

    public static FpDevice open() throws RuntimeException {
        if (mFpDevice == null) {
            try {
                mFpDevice = new FpDevice();
            } catch (RuntimeException e) {
                Log.w(TAG, "FpDeviceExt Init failed");
                //throw e;
                mFpDevice = null;
            }
        }
        return mFpDevice;
    };

    private WeakReference<Handler> dispatchHandlerRef = null;

    public void setDispathcMessageHandler(Handler handler) {
        if (null != handler) {
            dispatchHandlerRef = new WeakReference<Handler>(handler);
        }
    }

    private FpDevice() throws RuntimeException {
        Looper looper = null;
        if ((looper = Looper.myLooper()) != null) {
            mEventHandler = new EventHandler(this, looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            mEventHandler = new EventHandler(this, looper);
        } else {
            mEventHandler = null;
        }
        WeakReference<FpDevice> ref = new WeakReference<FpDevice>(this);
        try {
            Log.d(TAG, "native_setup");
            native_setup(ref);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public byte[] sendCmd(int cmd, byte[] data) {
        return SendCmd(cmd, data);
    }

    private class EventHandler extends Handler {
        public EventHandler(FpDevice fp, Looper looper) {
            super(looper);
            mFpDevice = fp;
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    Log.e(TAG, "Unknown message type: " + msg.what);
                    break;
            }
        }
    }

    private static void postEventFromNative(Object fpdevice_ref, int what, int arg1, int arg2, Object obj) {
        FpDevice fp = (FpDevice) ((WeakReference<FpDevice>) fpdevice_ref).get();
        if (fp == null) {
            return;
        }

        if (fp.mEventHandler != null) {
            Message m = fp.mEventHandler.obtainMessage(what, arg1, arg2, obj);

            if (fp.dispatchHandlerRef != null && fp.dispatchHandlerRef.get() != null) {
                fp.dispatchHandlerRef.get().sendMessage(m);
            } else {
                fp.mEventHandler.sendMessage(m);
            }
        }

    }


    private native final void native_setup(Object fpdevice);

    private native final void native_release();

    public native int query();

    public native int register();

    public native int cancelRegister();

    public native int resetRegister();

    public native int saveRegister(int index);

    public native int setMode(int cmd);

    public native int recognize();

    public native int delete(int index);

    public native String getInfo();

    public native int checkPassword(String password);

    @Override
    public int enableGsc(int index) {
        return 0;
    }

    @Override
    public int enableHbRetrieve() {
        return 0;
    }

    @Override
    public int disableHbRetrieve() {
        return 0;
    }

    @Override
    public int enableKeyMode(int enable, int keyType) {
        return 0;
    }

    public native int getPermission(String password);

    public native int changePassword(String oldPwd, String newPwd);

    public native int cancelRecognize();

    public native byte[] SendCmd(int cmd, byte[] data);

    public native int sendScreenState(int state);

    static {
        System.loadLibrary("fp_gf_mp");
    }

}
