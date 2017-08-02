package com.wingtech.diagnostic.fragment;

import android.hardware.input.InputManager;
import android.view.InputDevice;

import com.wingtech.diagnostic.util.Log;

import static android.content.Context.INPUT_SERVICE;

/**
 * Created by xiekui on 17-8-2.
 */

public class CMDMouseTestingFragment extends TestFragment
        implements InputManager.InputDeviceListener {
    @Override
    protected void onWork() {
        super.onWork();
        InputManager im = (InputManager) mActivity.getSystemService(INPUT_SERVICE);
        im.registerInputDeviceListener(this, null);
        final int[] devices = InputDevice.getDeviceIds();
        for (int deviceId : devices) {
            InputDevice device = InputDevice.getDevice(deviceId);
            if (!device.isVirtual()) {
                if (device.getName().contains("Mouse")) {
                    mResult = true;
                    Log.d("device.getName()=" + device.getName() + " device.getId() "
                            + device.getId() + " getDescriptor " + device.getDescriptor());
                    break;
                }
            }
        }
        mCallback.onChange(mResult);
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        InputDevice device = InputDevice.getDevice(deviceId);
        if (device != null && !device.isVirtual()) {
            if(device.getName().contains("Mouse")) {
                mResult = true;
                Log.d("device.getName()=" + device.getName()
                        + " device.getId() " + device.getId()
                        + " getDescriptor " + device.getDescriptor());
                mCallback.onChange(mResult);
            }
        }
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {

    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }
}
