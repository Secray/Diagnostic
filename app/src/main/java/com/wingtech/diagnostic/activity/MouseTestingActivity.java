package com.wingtech.diagnostic.activity;

import android.hardware.input.InputManager;
import android.view.InputDevice;

import com.wingtech.diagnostic.util.Log;

import static com.wingtech.diagnostic.util.Constants.MOUSE_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-8-1
 */

public class MouseTestingActivity extends TestingActivity
        implements InputManager.InputDeviceListener {

    @Override
    protected void onWork() {
        super.onWork();
        mRequestCode = MOUSE_REQUEST_CODE;
        InputManager im = (InputManager) getSystemService(INPUT_SERVICE);
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
        sendResult();
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
                sendResult();
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
