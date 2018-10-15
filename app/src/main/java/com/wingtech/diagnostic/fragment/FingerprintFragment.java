package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;

import com.android.helper.Helper;
import com.fpsensor.fpsensorManager.fpSensorManager;
import com.fpsensor.intf.IFpSensorTestTool;
import com.goodix.aidl.ITestCallback;
import com.goodix.util.FileUtil;
import com.swfp.device.DeviceManager;
import com.swfp.device.TeeDeviceManagerImpl;
import com.swfp.utils.MessageType;
import com.wingtech.diagnostic.App;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.goodix.device.MessageType.FINGERPRINT_CMD_MP_TEST_EXIT;
import static com.goodix.device.MessageType.FINGERPRINT_CMD_MP_TEST_INIT;
import static com.goodix.device.MessageType.FINGERPRINT_CMD_MP_TEST_SELFTEST;

/**
 * Created by xiekui on 17-8-3.
 */

public class FingerprintFragment extends TestFragment {
    private static final String FINGERPRINT_TYPE = "goodix.fp.hardware.ready"; //0是信炜 1是汇顶
    FingerprintManager mFingerprintManager;
    private String mType;
    private Handler mTaskHandler;
    HandlerThread mHandlerThread;
    private DeviceManager manager;
    private List<Runnable> mListRuns;
    private ExecutorService mCacheThreadPool;

    protected com.goodix.service.FingerprintManager mFpManager;
    protected com.goodix.service.FingerprintManager.TestSession mSession;

    private boolean mIsSPIPass;
    private boolean mIsIRQPass;

    private fpSensorManager thefpSensorMgr;
    private IFpSensorTestTool theSensorTestTool;

    private boolean isSwfp;
    private static final int MSG_TEST_RESULT = 998;
    public static final int MSG_NEXT_TASK = 999;
    public static final int MSG_SPI = 1000;
    public static final int MSG_GOODIX_SPI = 4500;
    public static final int MSG_IRQ = 1001;
    public static final int MSG_PASS = 1;
    public static final int MSG_FAIL = 0;
    public static final int MSG_TEST_DISCONNECTED = 6666;

    @Override
    protected void onWork() {
        super.onWork();
        mCacheThreadPool = Executors.newCachedThreadPool();
        mType = Helper.getSystemProperties(FINGERPRINT_TYPE, "1");
        Log.i("FingerPrint properties Fingerprint product " + mType);
        if ("1".equals(mType) && !Build.MODEL.equals("ASUS_X017D") && !Build.MODEL.equals("ASUS_X017DA")) {
            checkHardwareDetected();
            mFpManager = App.getInstance().getFpServiceManager();
            initDeviceMode();
        } else if (!isSwfp && (Build.MODEL.equals("ASUS_X017D")|| Build.MODEL.equals("ASUS_X017DA"))) {
            Log.d(Log.TAG, "jichuang");
            initJiChuangManager();
        } else {
            Log.d(Log.TAG, "xinwei");
            mListRuns = new ArrayList<>();
            mListRuns.add(mRunSpi);
            mListRuns.add(mRunIrq);
            TeeDeviceManagerImpl impl = new TeeDeviceManagerImpl();
            manager = impl;
            manager.registerCallBack((what, arg1, arg2) -> {
                Log.e("main msg what = " + what + "(0x" + Integer.toHexString(what) + ")" + " arg1 = "
                        + arg1 + "(0x" + Integer.toHexString(arg1) + ")" + " arg2 = " + arg2);
                mHandler.obtainMessage(what, arg1, arg2).sendToTarget();
            });

            mHandlerThread = new HandlerThread("task");
            mHandlerThread.start();
            mTaskHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_NEXT_TASK:
                            if (mListRuns != null && mListRuns.size() > 0) {
                                mTaskHandler.post(mListRuns.remove(0));
                            } else {
                                mHandler.obtainMessage(MSG_TEST_RESULT).sendToTarget();
                            }
                            break;
                    }
                }
            };
        }
    }

    @Override
    public void onResume() {
        isSwfp = Utils.isSwfp();
        if(Build.MODEL.equals("ASUS_X017DA"))
            isSwfp = false;
        super.onResume();
        if ("1".equals(mType) && !Build.MODEL.equals("ASUS_X017D") && !Build.MODEL.equals("ASUS_X017DA")) {
            Log.d("TEST_CHECK_SENSOR_TEST_INFO start");
            mCacheThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] init = mFpManager.sendCmd(
                            FINGERPRINT_CMD_MP_TEST_INIT,
                            FileUtil.intToBytes(3000));
                    if (init == null) {
                        mHandler.sendEmptyMessage(MSG_TEST_DISCONNECTED);
                    } else {
                        mFpManager.sendCmd(
                                FINGERPRINT_CMD_MP_TEST_SELFTEST,
                                FileUtil.intToBytes(3000));
                    }
                }
            });
        } else if (!isSwfp && (Build.MODEL.equals("ASUS_X017D") || Build.MODEL.equals("ASUS_X017DA"))) {
            int result = -1;
            try {
                if (theSensorTestTool != null)
                    result = theSensorTestTool.fpSensorSelfTest();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mCallback.onChange(result == 0);
        } else {
            manager.connect();
            doNextJob();
        }
    }

    public void initJiChuangManager() {
        thefpSensorMgr = fpSensorManager.getFpManager(getActivity());
        if (thefpSensorMgr == null) {
            //error. the fpExtensionSvc.apk has error
            Log.d(Log.TAG, "Test Error. env set up error");
            return;
        }

        theSensorTestTool = thefpSensorMgr.getFpSensorTestToolService();
        if (theSensorTestTool == null) {
            //error. the sensor is not connected when phone boot up
            Log.d(Log.TAG, "sensor not connected");
            return;
        }
    }

    private void initDeviceMode() {
        if (null == mSession) {
            mSession = mFpManager.newTestSession(mTestCallback);
            mSession.enter();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if ("1".equals(mType) && !Build.MODEL.equals("ASUS_X017D")&& !Build.MODEL.equals("ASUS_X017DA")) {
            mCacheThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    mFpManager.sendCmd(FINGERPRINT_CMD_MP_TEST_EXIT,
                            FileUtil.intToBytes(3000));
                }
            });
        } else if ("0".equals(mType) || isSwfp) {
            mTaskHandler.removeCallbacksAndMessages(null);
            mHandlerThread.quit();

            manager.reset();
            manager.disConnect();
        }

        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCacheThreadPool.shutdown();
    }

    private void doNextJob() {
        mTaskHandler.obtainMessage(MSG_NEXT_TASK).sendToTarget();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkHardwareDetected() {
        boolean isHardwareDetected;
        mFingerprintManager = (FingerprintManager) mActivity.getSystemService(Context.FINGERPRINT_SERVICE);
        isHardwareDetected = mFingerprintManager.isHardwareDetected();
        Log.i("isHardwareDetected = " + isHardwareDetected);
        if (!isHardwareDetected) {
            mCallback.onChange(false);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SPI:
                    if (msg.arg1 == MSG_PASS) {
                        Log.i("xinwei spi test pass");
                        mIsSPIPass = true;
                    } else {
                        mIsSPIPass = false;
                        Log.i("xinwei spi test fail");
                    }
                    doNextJob();
                    break;
                case MSG_IRQ:
                    if (msg.arg1 == MSG_PASS) {
                        Log.i("xinwei irq test pass");
                        mIsIRQPass = true;
                    } else {
                        mIsIRQPass = false;
                        Log.i("xinwei irq test fail");
                    }
                    doNextJob();
                    break;
                case MSG_TEST_RESULT:
                    mCallback.onChange(mIsIRQPass && mIsSPIPass);
                    break;
                case MSG_GOODIX_SPI:
                    if (msg.arg1 == 0) {
                        Log.i("msg.arg2 = " + msg.arg2);
                        boolean result = msg.arg2 == 0;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onChange(result);
                            }
                        }, 1000);
                    }
                    break;
                case MSG_TEST_DISCONNECTED:
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onChange(false);

                        }
                    }, 1000);
                    break;

            }
        }
    };


    private Runnable mRunSpi = new Runnable() {
        @Override
        public void run() {
            byte[] buf = new byte[8];
            int[] len = new int[1];
            len[0] = buf.length;
            int ret = manager.sendCmd(MessageType.FP_MSG_TEST_CMD_SPI_RDWR, 0, buf, len);
            if (ret == 0 && buf[0] == 1) {
                mHandler.obtainMessage(MSG_SPI, MSG_PASS, 0).sendToTarget();
            } else {
                mHandler.obtainMessage(MSG_SPI, MSG_FAIL, 0).sendToTarget();
            }
        }
    };

    /**
     * order 1
     */
    private Runnable mRunIrq = new Runnable() {
        @Override
        public void run() {
            byte[] buf = new byte[8];
            int[] len = new int[1];
            len[0] = buf.length;
            int ret = manager.sendCmd(MessageType.FP_MSG_TEST_CMD_IRQ, 0, buf, len);
            if (ret == 0 && buf[0] == 1) {
                mHandler.obtainMessage(MSG_IRQ, MSG_PASS, 0).sendToTarget();
            } else {
                mHandler.obtainMessage(MSG_IRQ, MSG_FAIL, 0).sendToTarget();
            }
        }
    };

    protected ITestCallback mTestCallback = new ITestCallback.Stub() {

        @Override
        public boolean handleMessage(int what, int arg1, int arg2, byte[] data)
                throws RemoteException {
            Log.v(String.format("what = %d , arg1 = %d ,arg2 = %d", what,
                    arg1, arg2));
            if (mHandler != null) {
                mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2,
                        data));
            }
            return false;
        }
    };
}
