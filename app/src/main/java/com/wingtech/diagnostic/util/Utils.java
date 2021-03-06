package com.wingtech.diagnostic.util;

import android.content.Context;
import android.util.SparseArray;
import android.util.TypedValue;

import com.wingtech.diagnostic.fragment.BatteryTestingFragment;
import com.wingtech.diagnostic.fragment.BlueToothFragment;
import com.wingtech.diagnostic.fragment.CMDMouseTestingFragment;
import com.wingtech.diagnostic.fragment.GpsTestingFragment;
import com.wingtech.diagnostic.fragment.GyroscopeTestingFragment;
import com.wingtech.diagnostic.fragment.MagneticTestingFragment;
import com.wingtech.diagnostic.fragment.ModemTestingFragment;
import com.wingtech.diagnostic.fragment.SDCardTestingFragment;
import com.wingtech.diagnostic.fragment.SIMCardTestingFragment;
import com.wingtech.diagnostic.fragment.TestFragment;
import com.wingtech.diagnostic.fragment.WiFiTestingFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    public static ArrayList<TestItem> getTestAllCases(ArrayList<TestItem> items) {
        ArrayList<TestItem> list = new ArrayList<>(items);
        Collections.sort(list, new Comparator<TestItem>() {
            @Override
            public int compare(TestItem o1, TestItem o2) {
                return ((Boolean) o2.isAutoTest()).compareTo(o1.isAutoTest());
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
            case "SIM Card2 Test":
                fragment = new SIMCardTestingFragment(2);
                break;
            case "CMD Mouse Test":
                fragment = new CMDMouseTestingFragment();
                break;
            case "GPS Test":
                fragment = new GpsTestingFragment();
                break;
        }
        return fragment;
    }
}
