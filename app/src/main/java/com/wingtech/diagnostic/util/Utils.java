package com.wingtech.diagnostic.util;

import android.content.Context;
import android.util.TypedValue;

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
}
