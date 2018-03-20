package com.wingtech.diagnostic.fragment;

import android.hardware.input.InputManager;
import android.os.Handler;
import android.view.InputDevice;

import com.wingtech.diagnostic.util.Log;

import static android.content.Context.INPUT_SERVICE;

/**
 * Created by xiekui on 17-8-2.
 */

public class CMDMouseTestingFragment extends TestFragment
        implements InputManager.InputDeviceListener {
    private InputManager mIm;
    private Handler mHandler;
    private boolean mIsSent;
    @Override
    protected void onWork() {
        super.onWork();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null && !mIsSent) {
                    mCallback.onChange(false);
                }
            }
        }, 10000);
        mIm = (InputManager) mActivity.getSystemService(INPUT_SERVICE);
        mIm.registerInputDeviceListener(this, null);
        final int[] devices = InputDevice.getDeviceIds();
        for (int deviceId : devices) {
            Log.i("CMDMouseTestingFragment deviceId = " + deviceId);
            InputDevice device = InputDevice.getDevice(deviceId);
            if (!device.isVirtual()) {
                if (device.getName().contains("Mouse")) {
                    mResult = true;
                    Log.d("device.getName()=" + device.getName() + " device.getId() "
                            + device.getId() + " getDescriptor " + device.getDescriptor());
                    mCallback.onChange(mResult);
                    mIsSent = true;
                    break;
                } else {
                    mTxtTitle.setText("Please insert the mouse...");
                }
            }
        }
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        Log.i("CMDMouseTestingFragment onInputDeviceAdded  deviceId = " + deviceId);
        InputDevice device = InputDevice.getDevice(deviceId);
        if (device != null && !device.isVirtual()) {
            if(device.getName().contains("Mouse")) {
                mResult = true;
                Log.d("device.getName()=" + device.getName()
                        + " device.getId() " + device.getId()
                        + " getDescriptor " + device.getDescriptor());
                mCallback.onChange(mResult);
                mIsSent = true;
            }
        }
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {

    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mIm.unregisterInputDeviceListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
