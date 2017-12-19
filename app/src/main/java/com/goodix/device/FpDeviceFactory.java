package com.goodix.device;

/**
 * Created by user on 2017/12/15 0015.
 */

public class FpDeviceFactory {
    String mModel;
    public FpDeviceFactory(String model) {
        mModel = model;
    }

    public IDevice getFpDevice() {
        IDevice device = null;
        if (mModel.contains("X00DD")) {
            device = FpDevice.open();
        } else if(mModel.contains("X00LD")) {
            device = FpDeviceExt.open();
        } else {
            device = FpDeviceExt.open();
        }
        return device;
    }
}
