/************************************************************************
 * <p>
 * Title: FingerprintManagerService.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright (C), 1997-2014, Shenzhen Goodix Technology Co.,Ltd.
 * </p>
 * <p>
 * Company: Goodix
 * </p>
 * 
 * @author peng.hu
 * @date 2014-9-20
 * @version 1.0
 ************************************************************************/
package com.goodix.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.goodix.aidl.ICaptureCallback;
import com.goodix.aidl.IEnrollCallback;
import com.goodix.aidl.IFingerprintManager;
import com.goodix.aidl.IHeartBeatCallback;
import com.goodix.aidl.IRegisterCallback;
import com.goodix.aidl.ITestCallback;
import com.goodix.aidl.IUpdateBaseCallback;
import com.goodix.aidl.IVerifyCallback;
import com.goodix.device.FpDeviceFactory;
import com.goodix.device.IDevice;
import com.goodix.device.MessageType;
import com.goodix.util.L;

import java.util.Vector;

/**
 * <p>
 * Title: FingerprintManagerService.java
 * </p>
 * <p>
 * Description: manager fingerprint device
 * </p>
 */
public class FingerprintManagerService extends Service {
    
    protected static final String TAG = "FpSetting";
    private IDevice device;
    private final String SERVICE_VERSION = "local";
    private String mPath;
    private String mCount_Capture = String.valueOf(30);
    private String mCount_Register = String.valueOf(8);
    private IUpdateBaseCallback mUpdateBaseCb = null;
    private IBinder mPreToken;
    final Object mLock = new Object();
    
    private class Client {
        
        public static final int TYPE_ENROLL = 1;
        public static final int TYPE_VERIFY = 2;
        public static final int TYPE_HEARTBEAT = 3;
        public static final int TYPE_REGISTER = 4;
        public static final int TYPE_CAPTURE = 5;
        public static final int TYPE_TEST = 6;
        public IBinder token;
        public int type;
        public Object callback;
        
        public Client(IBinder token, int type, Object callback) {
            this.token = token;
            this.type = type;
            this.callback = callback;
        }
    }
    
    private enum ManagerStatus {
        MANAGER_INIT, MANAGER_IDLE, MANAGER_ENROLL, MANAGER_VERIFY, MANAGER_HEARTBEAT, MANAGER_REGISTER, MANAGER_CAPTURE, MANAGER_TEST
    }
    
    private ManagerStatus mManagerStatus = ManagerStatus.MANAGER_IDLE;
    
    private enum EventStatus {
        EVENT_IDLE, EVENT_TOUCH, EVENT_UNTOUCH_NO_RESULT, EVENT_RESULT_NO_UNTOUCH, EVENT_COMPLETE,
    }
    
    private EventStatus mEventStatus = EventStatus.EVENT_IDLE;
    private Vector<Client> mClientList = new Vector<Client>();
    private Handler mDispathMessageHandler;
    
    // private PowerManager mPmGer;
    @Override
    public IBinder onBind(Intent arg0) {
        return stub;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "service create");
        HandlerThread mDispatchMessageThread = new HandlerThread("dispatch");
        mDispatchMessageThread.start();
        mDispathMessageHandler = new DispatchMessageHandler(
                mDispatchMessageThread.getLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                device = new FpDeviceFactory(Build.MODEL).getFpDevice();
                if (device != null) {
                    device.setDispathcMessageHandler(mDispathMessageHandler);
                }
            }
        }).start();

        registerScreenActionReceiver();
    }
    
    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    
    private void registerScreenActionReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);
    }
    
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(final Context context, final Intent intent) {
            // String strAction = intent.getAction();
            // boolean bScreenOn = mPmGer.isScreenOn();
            // L.d("FingerprintManagerService .onReceive .Intent.strAction == "
            // + intent.getAction());
            // if ((strAction.equals(Intent.ACTION_SCREEN_ON) && bScreenOn)
            // || (strAction.equals(Intent.ACTION_SCREEN_OFF) && !bScreenOn))
            // {
            // UpdateStatus();
            // }
        }
    };
    
    /*public void updateBaseValueInternal() {
        Log.d(TAG, "send update command to device");
        device.cancelRecognize();
        device.SendCmd(MessageType.GOODIX_ENGTEST_CMD_UPDATE_BASE, null);
    }*/
    
    public void setUpdateBaseCallbackInternal(IUpdateBaseCallback callback) {
        mUpdateBaseCb = callback;
    }
    
    public void UpdateStatus() {
        /*
         * 1. Get pre and next status of mananger. 2. exit pre . 3. Enter next
         * status.
         */
        boolean bScreenOn = true;// mPmGer.isScreenOn();
        L.d("[BEGIN]FingerprintManagerService : UpdateStatus.mManagerStatus == "
                + mManagerStatus
                + "  screen is = "
                + ((bScreenOn == true) ? "ON" : "OFF"));
        ManagerStatus nextStatus = ManagerStatus.MANAGER_IDLE;
        IBinder nextToken = null;
        if (!mClientList.isEmpty()) {
            Client client = mClientList.lastElement();
            if (client.type == Client.TYPE_ENROLL) {
                L.d("nextStatus = ManagerStatus.MANAGER_ENROLL");
                nextStatus = ManagerStatus.MANAGER_ENROLL;
            } else if (client.type == Client.TYPE_VERIFY) {
                L.d("nextStatus = ManagerStatus.MANAGER_VERIFY");
                nextStatus = ManagerStatus.MANAGER_VERIFY;
            } else if (client.type == Client.TYPE_HEARTBEAT) {
                L.d("nextStatus = ManagerStatus.MANAGER_HEARTBEAT");
                nextStatus = ManagerStatus.MANAGER_HEARTBEAT;
            }
            else if (client.type == Client.TYPE_REGISTER) {
                Log.v(TAG, "nextStatus = ManagerStatus.MANAGER_REGISTER");
                nextStatus = ManagerStatus.MANAGER_REGISTER;
            } else if (client.type == Client.TYPE_CAPTURE) {
                Log.v(TAG, "nextStatus = ManagerStatus.MANAGER_CAPTURE");
                nextStatus = ManagerStatus.MANAGER_CAPTURE;
            } else if (client.type == Client.TYPE_TEST) {
                Log.v(TAG, "nextStatus = ManagerStatus.MANAGER_TEST");
                nextStatus = ManagerStatus.MANAGER_TEST;
            }
            nextToken = client.token;
        }
        if (nextStatus == mManagerStatus && nextToken == mPreToken) {
            if (bScreenOn == false
                    && mManagerStatus == ManagerStatus.MANAGER_VERIFY) {
                //L.d("UpdateStatus set FF mode");
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
                if (device != null) {
                    device.cancelRecognize();
                }
                //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_FF);
                mEventStatus = EventStatus.EVENT_IDLE;
            }
            return;
        }
        switch (mManagerStatus) {
            case MANAGER_IDLE:
            break;
            case MANAGER_ENROLL:
                L.v("device.cancelRegister()");
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
                if (device != null) {
                    device.cancelRegister();
                }
            break;
            case MANAGER_VERIFY:
                L.d("device.cancelRecognize()");
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
                if (device != null) {
                    device.cancelRecognize();
                }
                if (nextStatus != ManagerStatus.MANAGER_ENROLL
                        && nextStatus != ManagerStatus.MANAGER_VERIFY) {
                    //L.d("set key mode");
                    //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_KEY);
                }
            break;
            case MANAGER_HEARTBEAT:
                if (device != null) {
                    device.disableHbRetrieve();
                }
                // device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_KEY);
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
            break;
            case MANAGER_REGISTER:
                Log.v(TAG, "device.cancelRegister()");
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
            // device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_KEY);
            break;
            case MANAGER_CAPTURE:
                Log.v(TAG, "device.cancelCapture()");
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
            // device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_KEY);
            break;
            case MANAGER_TEST:
                Log.v(TAG, "device.cancelTest()");
                mManagerStatus = ManagerStatus.MANAGER_IDLE;
            // device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_KEY);
            break;
            default:
            break;
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (nextStatus) {
            case MANAGER_IDLE:
            break;
            case MANAGER_ENROLL:
                if (device != null) {
                    device.getPermission("1234");
                    device.register();
                }
            break;
            case MANAGER_VERIFY:
                L.v("device.recognize();");
                if (bScreenOn == true) {
                    //L.d("set imag mode");
                    //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_IMG);
                    if (device != null) {
                        device.recognize();
                    }
                } else {
                    //L.d("set FF mode");
                    mManagerStatus = ManagerStatus.MANAGER_IDLE;
                    mPreToken = nextToken;
                    //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_FF);
                    return;
                }
            break;
            case MANAGER_HEARTBEAT: {
                L.v("device.enableHeatBeat();");
                // device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_HB);
                if (device != null) {
                    device.enableHbRetrieve();
                }
            }
            break;
            case MANAGER_REGISTER:
                //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_IMG);
            break;
            case MANAGER_CAPTURE:
                //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_IMG);
            break;
            case MANAGER_TEST:
                //device.setMode(CommandType.GOODIX_ENGTEST_CMD_SET_MODE_KEY);
            break;
            default:
            break;
        }
        mManagerStatus = nextStatus;
        mPreToken = nextToken;
        mEventStatus = EventStatus.EVENT_IDLE;
        L.d("FingerprintManagerService : UpdateStatus.end");
    }
    
    private boolean isTokenExist(IBinder token) {
        if (!mClientList.isEmpty()) {
            for (Client client : mClientList) {
                if (client.token == token) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean removeClient(IBinder token) {
        if (!mClientList.isEmpty()) {
            for (Client client : mClientList) {
                if (client.token == token) {
                    mClientList.remove(client);
                    return true;
                }
            }
        }
        return false;
    }
    
    private IFingerprintManager.Stub stub = new IFingerprintManager.Stub() {
        
        @Override
        public int verify(IBinder token, IVerifyCallback callback)
                throws RemoteException {
            synchronized (mLock) {
                L.v("FingerprintManagerService : verify");
                /**
                 * 1. check token , the token has existed in list. 2. add to
                 * list. 3. update status.
                 */
                if (!isTokenExist(token)) {
                    L.v("FingerprintManagerService : add client to list");
                    mClientList.add(new Client(token, Client.TYPE_VERIFY,
                            callback));
                    UpdateStatus();
                }
                return 0;
            }
        }
        
        @Override
        public int cancelVerify(IBinder token) throws RemoteException {
            /*
             * 1. check the client is last one or not. 2. delete client. 3.
             * update status if the token is last one.
             */
            synchronized (mLock) {
                L.v("FingerprintManagerService : cancelVerify");
                if (null != mClientList && mClientList.size() > 0) {
                    if (mClientList.lastElement().token == token) {
                        mClientList.removeElement(mClientList.lastElement());
                        UpdateStatus();
                    } else {
                        L.d("cancelVerify : removeClient");
                        removeClient(token);
                    }
                }
                return 0;
            }
        }
        
        @Override
        public int enroll(IBinder token, IEnrollCallback callback)
                throws RemoteException {
            /**
             * 1. check token , the token has existed in list. 2. add to list.
             * 3. update status.
             */
            synchronized (mLock) {
                L.v("FingerprintManagerService : enEnroll");
                if (!isTokenExist(token)) {
                    mClientList.add(new Client(token, Client.TYPE_ENROLL,
                            callback));
                    UpdateStatus();
                }
                return 0;
            }
        }
        
        @Override
        public int resetEnroll(IBinder token) throws RemoteException {
            /*
             * 1. check the client is last one or not. 2. delete client. 3.
             * update status if the token is last one.
             */
            synchronized (mLock) {
                L.v("FingerprintManagerService : resetEnroll");
                if (mClientList.size() > 0) {
                    if (mClientList.lastElement().token == token) {
                        /* reset device enroll */
                        if (device != null) {
                            device.resetRegister();
                        }
                    }
                }
                return 0;
            }
        }
        
        @Override
        public int cancelEnroll(IBinder token) throws RemoteException {
            /*
             * 1. check the client is last one or not. 2. delete client. 3.
             * update status if the token is last one.
             */
            synchronized (mLock) {
                L.v("FingerprintManagerService : cancelEnroll");
                if (null != mClientList && mClientList.size() > 0) {
                    if (mClientList.size() > 0) {
                        Client lastClient = mClientList.lastElement();
                        if (lastClient != null && lastClient.token == token) {
                            mClientList
                                    .removeElement(mClientList.lastElement());
                            UpdateStatus();
                        } else {
                            removeClient(token);
                        }
                    }
                }
                return 0;
            }
        }
        
        @Override
        public int saveEnroll(IBinder token, int index) throws RemoteException {
            /*
             * 1. Check is the last one token. 2. Check is complete register. 3.
             * save register.
             */
            synchronized (mLock) {
                L.v("FingerprintManagerService : saveEnroll");
                int result = 0;
                if (mClientList.size() > 0) {
                    Client client = mClientList.lastElement();
                    if (client.token == token) {
                        if (device != null) {
                            result = device.saveRegister(index);
                        }
                        UpdateStatus();
                    }
                    /* ignore the request of client which not on top of stack */
                }
                return result;
            }
        }
        
        @Override
        public int enterHeatBeat(IBinder token, IHeartBeatCallback callback)
                throws RemoteException {
            synchronized (mLock) {
                L.v("ServiceImpl : enterHeatBeat");
                if (!isTokenExist(token)) {
                    mClientList.add(new Client(token, Client.TYPE_HEARTBEAT,
                            callback));
                    UpdateStatus();
                } else {
                    L.v("Heartbeat Token is exist !");
                }
                return 0;
            }
        }
        
        @Override
        public int cancelHeatBeat(IBinder token) throws RemoteException {
            L.v("ServiceImpl : cancelHeatBeat");
            if (mClientList.size() > 0) {
                if (mClientList.lastElement().token == token) {
                    mClientList.removeElement(mClientList.lastElement());
                    UpdateStatus();
                } else {
                    removeClient(token);
                }
            }
            return 0;
        }
        
        @Override
        public int query() throws RemoteException {
            synchronized (mLock) {
                L.v("FingerprintManagerService : query template.");
                // device.getPermission("1234");
                if (device != null) {
                    return device.query();
                }
                return 0;
            }
        }
        
        public int delete(int i) throws RemoteException {
            synchronized (mLock) {
                L.v("FingerprintManagerService : delete template.");
                if (mManagerStatus == ManagerStatus.MANAGER_VERIFY) {
                    mManagerStatus = ManagerStatus.MANAGER_IDLE;
                    if (device != null) {
                        device.cancelRecognize();
                    }
                }
                if (mManagerStatus != ManagerStatus.MANAGER_ENROLL) {
                    if (device != null) {
                        device.cancelRegister();
                    }
                }
                int result = 0;
                if (device != null) {
                    device.getPermission("1234");
                    result = device.delete(i);
                }
                UpdateStatus();
                return result;
            }
        }
        
        @Override
        public String getInfo() throws RemoteException {
            Log.d(TAG, "milanService ***** getInfo()");
            synchronized (mLock) {
                if (device != null) {
                    return device.getInfo();
                }
                return "";
            }
        }
        
        @Override
        public int enableGsc(int index) throws RemoteException {
            if (device != null) {
                return device.enableGsc(index);
            }
            return 0;
        }
        
        @Override
        public int enableKeyMode(int enable, int keyType)
                throws RemoteException {
            L.v(" Framework : enableKeyMode , enable = " + enable + "keyType = " + keyType);
            if (device != null) {
                return device.enableKeyMode(enable, keyType);
            }
            return 0;
        }
        
        @Override
        public int register(IBinder token, IRegisterCallback callback)
                throws RemoteException {
            if (!isTokenExist(token)) {
                mClientList.add(new Client(token, Client.TYPE_REGISTER,
                        callback));
                UpdateStatus();
            }
            return 0;
        }
        
        @Override
        public int cancelRegister(IBinder token) throws RemoteException {
            Log.v(TAG, "FingerprintManagerService : cancelRegister");
            if (mClientList.lastElement().token == token) {
                mClientList.removeElement(mClientList.lastElement());
                UpdateStatus();
            } else {
                removeClient(token);
            }
            return 0;
        }
        
        @Override
        public int capture(IBinder token, ICaptureCallback callback)
                throws RemoteException {
            if (!isTokenExist(token)) {
                mClientList
                        .add(new Client(token, Client.TYPE_CAPTURE, callback));
                UpdateStatus();
            }
            return 0;
        }
        
        @Override
        public int cancelCapture(IBinder token) throws RemoteException {
            Log.v(TAG, "FingerprintManagerService : cancelCapture");
            if (mClientList.lastElement().token == token) {
                mClientList.removeElement(mClientList.lastElement());
                UpdateStatus();
            } else {
                removeClient(token);
            }
            return 0;
        }
        
        @Override
        public int test(IBinder token, ITestCallback callback)
                throws RemoteException {
            Log.v(TAG, "FingerprintManagerService : test");
            if (!isTokenExist(token)) {
                mClientList.add(new Client(token, Client.TYPE_TEST, callback));
                UpdateStatus();
            }
            return 0;
        }
        
        @Override
        public int cancelTest(IBinder token) throws RemoteException {
            if (mClientList.size() > 0) {
                if (mClientList.lastElement().token == token) {
                    mClientList.removeElement(mClientList.lastElement());
                    UpdateStatus();
                } else {
                    removeClient(token);
                }
            }
            return 0;
        }
        
        @Override
        public int EngTest(int cmd, int arg1, int arg2) throws RemoteException {
            Log.v(TAG, "FingerprintManagerService : EngTest cmd = " + cmd);
            return 0;
        }
        
        @Override
        public byte[] SendCmd(int cmd, byte[] data) throws RemoteException {
            Log.d(TAG, "FingerprintManagerService : SendCmd cmd = " + cmd);
            if (device != null) {
                return device.sendCmd(cmd, data);
            }
            return null;
        }
        
        @Override
        public int setMode(int cmd) throws RemoteException {
            if (device != null) {
                device.setMode(cmd);
            }
            return 0;
        }
        
        @Override
        public void setEnrollNum(int num) throws RemoteException {
            mCount_Register = String.valueOf(num);
            byte[] bytes = int2byte(num);
            if (device != null) {
                device.sendCmd(MessageType.FINGERPRINT_CMD_SET_ENROLL_CNT, bytes);
            }
        }
        
        @Override
        public byte[] getEnrollNum() throws RemoteException {
            Log.d(TAG, "MilanService ---- getEnrollNum()");
            if (device != null) {
                return device.sendCmd(MessageType.FINGERPRINT_CMD_GET_ENROLL_CNT,
                        null);
            }
            return null;
        }
        
        @Override
        public void updateBaseValue() throws RemoteException {
            // updateBaseValueInternal();
        }
        
        @Override
        public void setUpdateBaseCallback(IUpdateBaseCallback callback)
                throws RemoteException {
            // setUpdateBaseCallbackInternal(callback);
        }
    };
    
    public void sendMessageToClient(Message msg) {
        L.v("sendMessageToClient = " + msg);
        if (mClientList.isEmpty()) {
            return;
        }
        Client client = mClientList.lastElement();
        try {
            if (client.type == Client.TYPE_VERIFY) {
                ((IVerifyCallback) mClientList.lastElement().callback)
                        .handleMessage(msg.what, msg.arg1, msg.arg2,
                                (byte[]) ((msg.obj == null) ? new byte[0]
                                        : msg.obj));
            } else if (client.type == Client.TYPE_ENROLL) {
                ((IEnrollCallback) mClientList.lastElement().callback)
                        .handleMessage(msg.what, msg.arg1, msg.arg2,
                                (byte[]) ((msg.obj == null) ? new byte[0]
                                        : msg.obj));
            } else if (client.type == Client.TYPE_HEARTBEAT) {
                L.v("callback :: ServiceImpl ---> HBActivity");
                ((IHeartBeatCallback) mClientList.lastElement().callback)
                        .handleMessage(msg.what, msg.arg1, msg.arg2,
                                (byte[]) ((msg.obj == null) ? new byte[0]
                                        : msg.obj));
            }
            else if (client.type == Client.TYPE_REGISTER) {
                Log.d(TAG, "--------IRegisterCallback-------------");
                ((IRegisterCallback) mClientList.lastElement().callback)
                        .handleMessage(msg.what, msg.arg1, msg.arg2,
                                (byte[]) ((msg.obj == null) ? new byte[0]
                                        : msg.obj));
            } else if (client.type == Client.TYPE_CAPTURE) {
                Log.d(TAG, "--------ICaptureCallback-------------");
                ((ICaptureCallback) mClientList.lastElement().callback)
                        .handleMessage(msg.what, msg.arg1, msg.arg2,
                                (byte[]) ((msg.obj == null) ? new byte[0]
                                        : msg.obj));
            } else if (client.type == Client.TYPE_TEST) {
                ((ITestCallback) mClientList.lastElement().callback)
                        .handleMessage(msg.what, msg.arg1, msg.arg2,
                                (byte[]) ((msg.obj == null) ? new byte[0]
                                        : msg.obj));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    private class DispatchMessageHandler extends Handler {
        
        public DispatchMessageHandler(Looper looper) {
            super(looper);
        }
        
        public void handleVerifyMessage(Message msg) {
            L.d("FingerprintManagerService : handleVerifyMessage MessageType = "
                    + MessageType.getString(msg.what));
            if (mManagerStatus != ManagerStatus.MANAGER_VERIFY) {
                return;
            }
            switch (msg.what) {
                case MessageType.MSG_TYPE_COMMON_TOUCH:
                    if (mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_TOUCH;
                        /* send message */
                        sendMessageToClient(msg);
                    }
                break;
                case MessageType.MSG_TYPE_COMMON_UNTOUCH: {
                    if (mEventStatus == EventStatus.EVENT_TOUCH
                            || mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_UNTOUCH_NO_RESULT;
                    } else if (mEventStatus == EventStatus.EVENT_RESULT_NO_UNTOUCH) {
                        mEventStatus = EventStatus.EVENT_COMPLETE;
                    }
                    sendMessageToClient(msg);
                }
                break;
                case MessageType.MSG_TYPE_COMMON_NOTIFY_INFO:
                case MessageType.MSG_TYPE_RECONGNIZE_IAMGE:
                case MessageType.MSG_TYPE_RECONGNIZE_IAMGE_INFO:
                    sendMessageToClient(msg);
                break;
                case MessageType.MSG_TYPE_RECONGNIZE_SUCCESS:
                case MessageType.MSG_TYPE_RECONGNIZE_TIMEOUT:
                case MessageType.MSG_TYPE_RECONGNIZE_FAILED:
                case MessageType.MSG_TYPE_RECONGNIZE_BAD_IMAGE:
                case MessageType.MSG_TYPE_RECONGNIZE_GET_DATA_FAILED:
                case MessageType.MSG_TYPE_RECONGNIZE_NO_REGISTER_DATA: {
                    if (mEventStatus == EventStatus.EVENT_TOUCH
                            || mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_RESULT_NO_UNTOUCH;
                    } else if (mEventStatus == EventStatus.EVENT_UNTOUCH_NO_RESULT) {
                        mEventStatus = EventStatus.EVENT_COMPLETE;
                    }
                    sendMessageToClient(msg);
                    break;
                }
                default:
                break;
            }
            if (mEventStatus == EventStatus.EVENT_COMPLETE) {
                mEventStatus = EventStatus.EVENT_IDLE;
                L.d("EVENT_COMPLETE : device.recognize()");
                if (device != null) {
                    device.recognize();
                }
            }
        }
        
        private int percent = 0;
        
        public void handleEnrollMessage(Message msg) {
            L.d("FingerprintManagerService : handleEnrollMessage MessageType = "
                    + MessageType.getString(msg.what));
            if (mManagerStatus != ManagerStatus.MANAGER_ENROLL) {
                return;
            }
            switch (msg.what) {
                case MessageType.MSG_TYPE_COMMON_TOUCH:
                    if (mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_TOUCH;
                        sendMessageToClient(msg);
                        /* send message */
                    }
                break;
                case MessageType.MSG_TYPE_COMMON_UNTOUCH: {
                    if (mEventStatus == EventStatus.EVENT_TOUCH
                            || mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_UNTOUCH_NO_RESULT;
                    } else if (mEventStatus == EventStatus.EVENT_RESULT_NO_UNTOUCH) {
                        mEventStatus = EventStatus.EVENT_COMPLETE;
                    }
                    sendMessageToClient(msg);
                }
                break;
                case MessageType.MSG_TYPE_COMMON_NOTIFY_INFO:
                case MessageType.MSG_TYPE_REGISTER_IAMGE:
                case MessageType.MSG_TYPE_REGISTER_IAMGE_INFO:
                    /* case MessageType.MSG_TYPE_REGISTER_DUPLICATE_REG: */
                    sendMessageToClient(msg);
                break;
                case MessageType.MSG_TYPE_REGISTER_PIECE:
                case MessageType.MSG_TYPE_REGISTER_NO_PIECE:
                case MessageType.MSG_TYPE_REGISTER_NO_EXTRAINFO:
                case MessageType.MSG_TYPE_REGISTER_LOW_COVER:
                case MessageType.MSG_TYPE_REGISTER_BAD_IMAGE:
                case MessageType.MSG_TYPE_REGISTER_GET_DATA_FAILED:
                case MessageType.MSG_TYPE_REGISTER_NOT_GSC:
                case MessageType.MSG_TYPE_ERROR: {
                    if (mEventStatus == EventStatus.EVENT_TOUCH
                            || mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_RESULT_NO_UNTOUCH;
                        L.d("mEventStatus = EventStatus.EVENT_RESULT_NO_UNTOUCH");
                    } else if (mEventStatus == EventStatus.EVENT_UNTOUCH_NO_RESULT) {
                        mEventStatus = EventStatus.EVENT_COMPLETE;
                    }
                    percent = msg.arg1;
                    L.d("mEventStatus = sendMessageToClient msg = " + msg);
                    sendMessageToClient(msg);
                }
                break;
                default:
                break;
            }
            if (mEventStatus == EventStatus.EVENT_COMPLETE && percent < 100) {
                L.d("EVENT_COMPLETE : device.register()");
                mEventStatus = EventStatus.EVENT_IDLE;
                // device.getPermission("1234");
                // device.register();
                percent = 0;
            }
        }
        
        // cy add handleHbMessage
        public void handleHBMessage(Message msg) {
            L.v("get HB message : handleHBMessage...(message.what = "
                    + msg.what + ")" + " %%%% " + "mManagerStatus = "
                    + mManagerStatus);
            if (mManagerStatus != ManagerStatus.MANAGER_HEARTBEAT) {
                return;
            }
            switch (msg.what) {
                case MessageType.MSG_TYPE_COMMON_TOUCH:
                    if (mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_TOUCH;
                        sendMessageToClient(msg);
                        /* send message */
                    }
                break;
                case MessageType.MSG_TYPE_COMMON_UNTOUCH: {
                    if (mEventStatus == EventStatus.EVENT_TOUCH
                            || mEventStatus == EventStatus.EVENT_IDLE) {
                        mEventStatus = EventStatus.EVENT_UNTOUCH_NO_RESULT;
                    } else if (mEventStatus == EventStatus.EVENT_RESULT_NO_UNTOUCH) {
                        mEventStatus = EventStatus.EVENT_COMPLETE;
                    }
                    sendMessageToClient(msg);
                }
                break;
                case MessageType.MSG_TYPE_COMMON_NOTIFY_INFO:
                    // Log.v(TAG, "handleHBMessage >>.......send to Client");
                    sendMessageToClient(msg);
                break;
                case MessageType.MSG_TYPE_COMMON_HB:
                    L.v("handleHBMessage >>.......send to Client { msg.obj--> length = "
                            + ((byte[]) msg.obj).length + " }");
                    sendMessageToClient(msg);
                break;
                default:
                break;
            }
        }
        
        //register
        public void handleRegisterMessage(Message msg) {
            Log.v(TAG, "FingerprintManagerService : handleRegisterMessage");
            Log.v(TAG, "MessageType = " + MessageType.getString(msg.what));
            Log.d(TAG, "--mManagerStatus = " + mManagerStatus);
            if (mManagerStatus != ManagerStatus.MANAGER_REGISTER) {
                return;
            }
            sendMessageToClient(msg);
        }
        
        //capture
        public void handleCaptureMessage(Message msg) {
            Log.v(TAG, "FingerprintManagerService : handleCaptureMessage");
            Log.v(TAG, "MessageType = " + MessageType.getString(msg.what));
            Log.d(TAG, "--mManagerStatus = " + mManagerStatus);
            if (mManagerStatus != ManagerStatus.MANAGER_CAPTURE) {
                return;
            }
            sendMessageToClient(msg);
        }
        
        //test
        public void handleTestMessage(Message msg) {
            Log.v(TAG, "FingerprintManagerService : handleTestMessage");
            Log.v(TAG, "MessageType = " + MessageType.getString(msg.what));
            if (mManagerStatus != ManagerStatus.MANAGER_TEST) {
                return;
            }
            sendMessageToClient(msg);
        }
        
        public void handleMessage(Message msg) {
            L.v("ServiceImpl : handleMessage mManagerStatus == "
                    + mManagerStatus);
            if (mManagerStatus == ManagerStatus.MANAGER_VERIFY) {
                handleVerifyMessage(msg);
            } else if (mManagerStatus == ManagerStatus.MANAGER_ENROLL) {
                handleEnrollMessage(msg);
            } else if (mManagerStatus == ManagerStatus.MANAGER_HEARTBEAT) {
                handleHBMessage(msg);
            }
            // XYF1223
            else if (mManagerStatus == ManagerStatus.MANAGER_REGISTER) {
                handleRegisterMessage(msg);
            } else if (mManagerStatus == ManagerStatus.MANAGER_CAPTURE) {
                handleCaptureMessage(msg);
            } else if (mManagerStatus == ManagerStatus.MANAGER_TEST) {
                handleTestMessage(msg);
            }
        }
    }
    
    public static byte[] int2byte(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[3 - i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }
}
