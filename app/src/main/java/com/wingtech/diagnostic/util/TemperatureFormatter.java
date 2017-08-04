package com.wingtech.diagnostic.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @author xiekui
 * @date 2017-7-12
 * Temperature Formatter for thr MPAndroidChart YAxis
 * example: value is 30 and after format is 30℃
 */

public class TemperatureFormatter implements IAxisValueFormatter{
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if ((int) value % 50 != 0) {
            return "";
        }
        return (int) value + "℃";
    }
}
