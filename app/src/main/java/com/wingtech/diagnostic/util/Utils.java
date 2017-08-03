package com.wingtech.diagnostic.util;

import android.content.Context;
import android.util.TypedValue;

import com.wingtech.diagnostic.fragment.BatteryTestingFragment;
import com.wingtech.diagnostic.fragment.BlueToothFragment;
import com.wingtech.diagnostic.fragment.CMDMouseTestingFragment;
import com.wingtech.diagnostic.fragment.GyroscopeTestingFragment;
import com.wingtech.diagnostic.fragment.MagneticTestingFragment;
import com.wingtech.diagnostic.fragment.ModemTestingFragment;
import com.wingtech.diagnostic.fragment.SDCardTestingFragment;
import com.wingtech.diagnostic.fragment.SIMCardTestingFragment;
import com.wingtech.diagnostic.fragment.TestFragment;
import com.wingtech.diagnostic.fragment.WiFiTestingFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xiekui
 * @date 2017-7-28
 */

public class Utils {
    public static float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                value, context.getResources().getDisplayMetrics());
    }

    public static List<String> getTestAllCases(String []testCases) {
        List<String> list = Arrays.asList(testCases);
        Collections.sort(Arrays.asList(testCases), (o1, o2) -> {
            switch (o1) {
                case "Bluetooth Test":
                case "Wi-Fi Test":
                case "Battery Test":
                case "E-Compass Test":
                case "Gyroscope Test":
                case "Modem Test":
                case "CMD Mouse Test":
                case "SD Card Test":
                case "SIM Card Test":
                case "SIM2 Test":
                    switch (o2) {
                        case "Bluetooth Test":
                        case "Wi-Fi Test":
                        case "Battery Test":
                        case "E-Compass Test":
                        case "Gyroscope Test":
                        case "Modem Test":
                        case "CMD Mouse Test":
                        case "SD Card Test":
                        case "SIM Card Test":
                        case "SIM2 Test":
                            return 0;
                        default:
                            break;
                    }
                    return -1;
                default:
                    return 1;
            }
        });
        return list;
    }

    public static TestFragment getFragment(String title) {
        TestFragment fragment = null;
        switch (title) {
            case "Bluetooth Test":
                fragment = new BlueToothFragment();
                break;
            case "Battery Test":
                fragment = new BatteryTestingFragment();
                break;
            case "Wi-Fi Test":
                fragment = new WiFiTestingFragment();
                break;
            case "E-Compass Test":
                fragment = new MagneticTestingFragment();
                break;
            case "Gyroscope Test":
                fragment = new GyroscopeTestingFragment();
                break;
            case "Modem Test":
                fragment = new ModemTestingFragment();
                break;
            case "SD Card Test":
                fragment = new SDCardTestingFragment();
                break;
            case "SIM Card Test":
                fragment = new SIMCardTestingFragment(1);
                break;
            case "SIM2 Test":
                fragment = new SIMCardTestingFragment(2);
                break;
            case "CMD Mouse Test":
                fragment = new CMDMouseTestingFragment();
                break;
        }
        return fragment;
    }
}
