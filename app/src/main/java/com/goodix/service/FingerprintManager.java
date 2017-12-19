package com.goodix.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.helper.Helper;
import com.goodix.aidl.ICaptureCallback;
import com.goodix.aidl.IEnrollCallback;
import com.goodix.aidl.IFingerprintManager;
import com.goodix.aidl.IHeartBeatCallback;
import com.goodix.aidl.IRegisterCallback;
import com.goodix.aidl.ITestCallback;
import com.goodix.aidl.IUpdateBaseCallback;
import com.goodix.aidl.IVerifyCallback;

import java.lang.reflect.Method;

public class FingerprintManager implements ServiceConnection {
    
	private static final String IS_HUI_DING = "goodix.fp.hardware.ready";//0 是信炜   ，1 是汇顶
	private String isHuiDing = null;
    private static final String TAG = "FpSetting";
    public static final int FP_SERVICE_STATUS_NOT_INIT = 1;
    public static final int FP_SERVICE_STATUS_INIT_FAILED = 2;
    public static final int FP_SERVICE_STATUS_CONNECT = 3;
    public static final int FP_SERVICE_STATUS_INIT_SUCCESS = 4;
    private int mServiceStatus = FP_SERVICE_STATUS_NOT_INIT;
    private boolean FLAG = false;
    private IFingerprintManager mService;
    private Context mContext;
    private static final String SERVICE_NAME = "milan_service";    
    private FpManagerServiceConnectListener mFmServiceConnectListener;  //FpApplication object
    
    public void setFpManagerServiceConnectListener(FpManagerServiceConnectListener listener){
        mFmServiceConnectListener = listener;
    }
    
    public interface FpManagerServiceConnectListener{
        public void onFpManagerServiceConnected();
        public void onFpManagerServiceDisconnected();
    }
    
    // new system java service, called in FingerprintManager
    // constructor
    private void getService() {
        Log.d(TAG, "milan---- mService.getService()");
        IBinder service = null;
        try {
            Class<?> servicemanager = Class
                    .forName("android.os.ServiceManager");
            Method getService = servicemanager.getMethod("getService",
                    String.class);
            service = (IBinder) getService.invoke(null, SERVICE_NAME);
            if (service == null) {
                Log.d(TAG, "FingerprintManger getService enter Exception ");
            } else {
                // link death binder
                Log.d(TAG, "FingerprintManger getService enter SUCCESS! ");
            }
        } catch (Exception e) {
            Log.d(TAG, "milan---- mService.getService() excp");
            e.printStackTrace();
            // throw new
            // FingerprintException("FingerprintManager get service Exception");
        }
        mService = IFingerprintManager.Stub.asInterface(service);
        if (mService == null) {
            Log.d(TAG, "get serviceimpl interface is failed");
            // throw new
            // FingerprintException("get serviceimpl interface is failed");
        }
        Log.d(TAG, "SUCCESS");
    }
   
    // for old service
    public void getLocalService(Context context) {
        mContext = context;
        try {        
			isHuiDing  = Helper.getSystemProperties(IS_HUI_DING,"1");
			Log.d(TAG, "isisHuiDing = " + isHuiDing);
			if(isHuiDing.equals("1")){
				Intent intent = new Intent(context, FingerprintManagerService.class);
				context.bindService(intent, this, Context.BIND_AUTO_CREATE);
				Log.d(TAG, "isisHuiDing = context.bindService");
			}
        } catch (Exception e) {
            mService = null;
            updateStatus(FP_SERVICE_STATUS_INIT_FAILED);
            e.printStackTrace();
        }
    }

    public int query() {
        Log.d(TAG, "milan---- mService.query()");
        try {
            return mService.query();
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.query() excp");
            e.printStackTrace();
        }
        return 0;
    }

    public void unbindService() {
        if (mContext != null) {
            mContext.unbindService(this);
        }
    }
    
    public int delete(int i) {
        try {
            return mService.delete(i);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }
    

    public String getInformation() {
        try {
            Log.d(TAG, "milan---- mService.getInfo()");
            return mService.getInfo();
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.getInfo() excp");
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean checkPassword(String psw) {
        if (null != psw) {
            return psw.equals("1234");
        }
        return false;
    }
    
    public boolean changePassword(String oldPassword, String newPassword) {
        return true;
    }
    
    public FingerprintManager(IFingerprintManager service) {
        mService = service;
    }

    // userful constructor
    public FingerprintManager(Context context) {
        if (null == mService) {
            Log.d(TAG, "getLocalService()");
            getLocalService(context);
        } else {
            updateStatus(FP_SERVICE_STATUS_INIT_SUCCESS);
        }
    }
    
    private synchronized void updateStatus(int status) {
        mServiceStatus = status;
    }
    
    public int getServiceStatus() {
        return mServiceStatus;
    }
    
    public VerifySession newVerifySession(IVerifyCallback callback) {
        return new VerifySession(callback);
    }
    
    public EnrollSession newEnrollSession(IEnrollCallback callback) {
        return new EnrollSession(callback);
    }
    
    public RegisterSession newRegisterSession(IRegisterCallback callback) {
        return new RegisterSession(callback);
    }
    
    public CaptureSession newCaptureSession(ICaptureCallback callback) {
        return new CaptureSession(callback);
    }
    
    public TestSession newTestSession(ITestCallback callback) {
        return new TestSession(callback);
    }
    
    public HBDSession newHBDSession(IHeartBeatCallback callback) {
        return new HBDSession(callback);
    }
    
    // verify
    public final class VerifySession {

        private IBinder mToken;

        private IVerifyCallback mCallback;
        
        public VerifySession(IVerifyCallback callback) {
            Log.v(TAG, "new VerifySession.");
            mCallback = callback;
            mToken = new Binder();
        }
        
        public int enter() {
            try {
                Log.v(TAG, "verify session enter.");
                return mService.verify(mToken, mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        

        public int exit() {
            try {
                Log.v(TAG, "verify session exit.");
                return mService.cancelVerify(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    
    // enroll
    public final class EnrollSession {
        
        private IBinder mToken;
        private IEnrollCallback mCallback;
        
        public EnrollSession(IEnrollCallback callback) {
            mCallback = callback;
            mToken = new Binder();
        }
        
        public int enter() {
            try {
                Log.v(TAG, "new enroll session.");
                return mService.enroll(mToken, mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int exit() {
            try {
                Log.v(TAG, "enroll session exit.");
                return mService.cancelEnroll(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int reset() {
            try {
                Log.v(TAG, "enroll session reset.");
                return mService.resetEnroll(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int save(int index) {
            try {
                Log.v(TAG, "enroll session save.");
                return mService.saveEnroll(mToken, index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    
    // register
    public final class RegisterSession {
        
        private IBinder mToken;
        private IRegisterCallback mCallback;
        
        public RegisterSession(IRegisterCallback callback) {
            mCallback = callback;
            mToken = new Binder();
        }
        
        public int enter() {
            try {
                Log.v(TAG, "new register session.");
                return mService.register(mToken, mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int exit() {
            try {
                Log.v(TAG, "exit capture session.");
                return mService.cancelRegister(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    
    // capture
    public final class CaptureSession {
        
        private IBinder mToken;
        private ICaptureCallback mCallback;
        
        public CaptureSession(ICaptureCallback callback) {
            mCallback = callback;
            mToken = new Binder();
        }
        
        public int enter() {
            try {
                Log.v(TAG, "new capture session.");
                return mService.capture(mToken, mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int exit() {
            try {
                Log.v(TAG, "exit capture session.");
                return mService.cancelCapture(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    
    // test
    public final class TestSession {
        
        private IBinder mToken;
        private ITestCallback mCallback;
        
        public TestSession(ITestCallback callback) {
            mCallback = callback;
            mToken = new Binder();
        }
        
        public int enter() {
            try {
                Log.v(TAG, "new test session.");
                return mService.test(mToken, mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int exit() {
            try {
                Log.v(TAG, "exit test session.");
                return mService.cancelTest(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    
    public final class HBDSession {
        
        private IBinder mToken;
        private IHeartBeatCallback mCallback;
        
        public HBDSession(IHeartBeatCallback callback) {
            Log.v(TAG, "new HBDSession.");
            mCallback = callback;
            mToken = new Binder();
        }
        
        public int enter() {
            try {
                Log.v(TAG, "enter HBD session.");
                return mService.enterHeatBeat(mToken, mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
        
        public int exit() {
            try {
                Log.v(TAG, "HBD session exit.");
                return mService.cancelHeatBeat(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    
    public byte[] sendCmd(int cmd, byte[] data) {
        Log.d(TAG, "milan---- mService.sendCmd()");
        byte[] ret = null;
        try {
            ret = mService.SendCmd(cmd, data);
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.sendCmd() excp");
            e.printStackTrace();
        }
        return ret;
    }
    
    public void setMode(int mode) {
        Log.d(TAG, "milan---- mService.setMode()");
        try {
            mService.setMode(mode);
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.setMode() excp");
            e.printStackTrace();
        }
    }
    
    public void setEnrollNum(int num) {
        Log.d(TAG, "milan---- mService.setEnrollNum()");
        try {
            mService.setEnrollNum(num);
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.setEnrollNum() excp");
            e.printStackTrace();
        }
    }
    
    public byte[] getEnrollNum() {
        Log.d(TAG, "milan---- mService.getEnrollNum()");
        byte[] data = null;
        try {
            data = mService.getEnrollNum();
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.getEnrollNum() excp");
            e.printStackTrace();
        }
        return data;
    }
    
    public void updateBaseValue() {
        Log.d(TAG, "milan---- mService.updateBaseValue()");
        try {
            mService.updateBaseValue();
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.updateBaseValue() excp");
            e.printStackTrace();
        }
    };
    
    public void setUpdateBaseCallback(IUpdateBaseCallback callback) {
        Log.d(TAG, "milan---- mService.setUpdateBaseCallback()");
        try {
            mService.setUpdateBaseCallback(callback);
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.setUpdateBaseCallback() excp");
            e.printStackTrace();
        }
    };
    
    public void enableKeyMode(int flag, int keyType) {
        Log.d(TAG, "milan---- mService.enableKeyMode()");
        try {
            mService.enableKeyMode(flag, keyType);
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.enableKeyMode() excp");
            e.printStackTrace();
        }
    }
    
    public void enableGsc(int flag) {
        Log.d(TAG, "milan---- mService.enableGsc()");
        try {
            mService.enableGsc(flag);
        } catch (RemoteException e) {
            Log.d(TAG, "milan---- mService.enableKeyMode() excp");
            e.printStackTrace();
        }
    }
    
    @Override
    public void onServiceConnected(ComponentName arg0, IBinder binder) {
        Log.d(TAG, "FpApplication onServiceConnected");
        mService = IFingerprintManager.Stub.asInterface(binder);
        Log.d(TAG, "FpApplication mService == " + mService);
        FLAG = true;
        updateStatus(FP_SERVICE_STATUS_CONNECT);
        //mFmServiceConnectListener.onFpManagerServiceConnected();  //FpApplication object
    }
    
    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        Log.d(TAG, "onServiceDisconnected");
        mService = null;
        FLAG = false;
    }
    
    public boolean getFlag() {
        return FLAG;
    }
}
