package com.wingtech.diagnostic.util;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class Constants {
    public static final String BLUETOOTH_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
    public static final String BLUETOOTH_DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED";
    public static final String BLUETOOTH_DEVICE_FOUND = "android.bluetooth.device.action.FOUND";
    public static final String BLUETOOTH_DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";
    public static final String WIFI_STATE_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";

    public static final int BLUETOOTH_REQUEST_CODE = 0;
    public static final int WIFI_REQUEST_CODE = 1;
    public static final int BATTERY_REQUEST_CODE = 2;
    public static final int E_COMPASS_REQUEST_CODE = 3;
    public static final int GYROSCOPE_REQUEST_CODE = 4;
}
