package com.goodix.device;

import android.content.Context;
import android.hardware.FingerPrint;
import android.hardware.FingerPrintClient;
import android.hardware.IFpService;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class FpDeviceExt implements IDevice {

    private static final String TAG = "FpDeviceExt";
    private static final int FP_CLIENT_TYPE_SYSTEM_FP = 0; // for Android system
                                                           // fingerpeintd
    private static final int FP_CLIENT_TYPE_MP_TOOL_APP = 1; // for "MP_Tool"
    private static final int FP_CLIENT_TYPE_FARFRR_TEST_APP = 2; // for
                                                                 // "GF_FAR_FRR_Tool"
    private static final int FP_CLIENT_TYPE_GX_FP_DEMO = 3; // for Goodix demo
                                                            // "GF_Demo_Tool"
    private static final int FP_CLIENT_TYPE_INTE_TOOL_APP = 4; // for
                                                               // Integrated_Test_Tool
    private static final int FP_CLIENT_TYPE_FP_NAV = 5; // for navigation demo
    private static final int FP_CLIENT_TYPE_FP_CMD = 6;
    private static FpDeviceExt mFpDevice = null;
    private static final String SERVICE_NAME = "goodix.fp";
    private IFpService daemon;
    private FingerPrint mFingerPrint;
    private Context mContext;
    private EventHandler mEventHandler = null;

    public static FpDeviceExt open() throws RuntimeException {
        if (mFpDevice == null) {
            mFpDevice = new FpDeviceExt();
        }
        return mFpDevice;
    };

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    private WeakReference<Handler> dispatchHandlerRef = null;

    // public DispatchMessageHandler getDispatchMessageHandler() {
    // return dispatchHandlerRef;
    // }

    public void setDispathcMessageHandler(Handler handler) {
        if (null != handler) {
            dispatchHandlerRef = new WeakReference<Handler>(handler);
        }
    }

    private FpDeviceExt() throws RuntimeException {
        getService();
    }

    public int query() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.query();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int register() {
        checkService();
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.regiest();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int cancelRegister() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.cancelRegist();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int resetRegister() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.resetRegist();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int saveRegister(int index) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.saveRegist(index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int setMode(int mode) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.setMode(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int recognize() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.recognize();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int delete(int index) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.unRegist(index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getInfo() {
        checkService();
        if (mFingerPrint != null) {
            try {
                return new String(mFingerPrint.getInfo());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int checkPassword(String password) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.checkPasswd(password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /* add more func */
    public int enableGsc(int index) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.enableGsc(index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    public int enableHbRetrieve() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.enableHbRetrieve();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    public int disableHbRetrieve() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.disableHbRetrieve();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    public int enableKeyMode(int enable, int keyType) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.enableKeyMode(enable, keyType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    /* end add more func */

    public int getPermission(String password) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.requestPermission(password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int changePassword(String oldPwd, String newPwd) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.setPasswd(oldPwd, newPwd);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int cancelRecognize() {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.cancelRecognize();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public byte[] SendCmd(int cmd, byte[] data) {
        checkService();
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.sendCmd(cmd, data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int sendScreenState(int state) {
        if (mFingerPrint != null) {
            try {
                return mFingerPrint.sendScreenState(state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void getService() {
        Log.d(TAG, "milan---- mService.getService()");
        IBinder mIBinder = null;
        Looper looper = null;
        try {
            Class<?> servicemanager = Class.forName("android.os.ServiceManager");
            Method getService = servicemanager.getMethod("getService", String.class);
            mIBinder = (IBinder) getService.invoke(null, SERVICE_NAME);
            if (mIBinder == null) {
                Log.d(TAG, "getService mIBinder == null ");
            } else {
                // link death binder
                Log.d(TAG, "getService enter SUCCESS! ");
            }
        } catch (Exception e) {
            Log.d(TAG, "milan---- mService.getService() Exception");
            e.printStackTrace();
        }
        daemon = IFpService.Stub.asInterface(mIBinder);
        try {
            Log.d(TAG, "remote service = " + mIBinder.getInterfaceDescriptor());
            IBinder client = daemon.connect(callback, 1);
            mFingerPrint = FingerPrint.Stub.asInterface(client);
            if (mFingerPrint == null) {
                Log.d(TAG, "mFingerPrint == null");
                throw new RuntimeException();
            } else
                Log.d(TAG, "mFingerPrint != null");
            Log.d(TAG, "mFingerPrint.getInfo() ==" + mFingerPrint.getInfo());
            if ((looper = Looper.myLooper()) != null) {
                mEventHandler = new EventHandler(this, looper);
            } else if ((looper = Looper.getMainLooper()) != null) {
                mEventHandler = new EventHandler(this, looper);
            } else {
                mEventHandler = null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void checkService() {
        Log.d(TAG, "milan---- mService.checkService()");
        try {
            Class<?> servicemanager = Class.forName("android.os.ServiceManager");
            Method checkService = servicemanager.getMethod("checkService", String.class);
            IBinder mIBinder = (IBinder) checkService.invoke(null, SERVICE_NAME);
            if (mIBinder != null) {
                Log.d(TAG, "checkService SUCCESS!! ");
            } else {
                showDialog();
                Log.d(TAG, "checkService  fail! ");
            }
        } catch (Exception e) {
            Log.d(TAG, "milan---- mService.checkService() Exception");
            e.printStackTrace();
        }
    }

    public void showDialog() {
        // TODO
    }

    private FingerPrintClient callback = new FingerPrintClient.Stub() {

        @Override
        public void notifyCallback(int msgType, int ext1, int ext2) throws RemoteException {
            Log.e(TAG, " notifyCallback: msgType = " + msgType + " ext1 = " + ext1 + " ext2 = " + ext2);
            if (mFpDevice.dispatchHandlerRef != null) {
                Message m = mEventHandler.obtainMessage(msgType, ext1, ext2, null);
                mFpDevice.dispatchHandlerRef.get().sendMessage(m);
            }
        }

        @Override
        public void dataCallback(int msgType, byte[] msgData) throws RemoteException {
            Log.e(TAG, " msgType: =  " + msgType + "length = " + msgData.length);
            if (mFpDevice.dispatchHandlerRef != null) {
                Message m = mEventHandler.obtainMessage(msgType, msgData);
                mFpDevice.dispatchHandlerRef.get().sendMessage(m);
            }
        }
    };

    @Override
    public byte[] sendCmd(int cmd, byte[] data) {
        return SendCmd(cmd, data);
    }

    private class EventHandler extends Handler {

        public EventHandler(FpDeviceExt fp, Looper looper) {
            super(looper);
            mFpDevice = fp;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    break;
            }
        }
    }
}
