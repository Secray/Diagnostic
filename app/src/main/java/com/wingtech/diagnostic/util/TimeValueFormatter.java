package com.wingtech.diagnostic.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/***
 * @author xiekui
 * @date 2017-7-12
 * Time formatter for the seconds and minutes
 * It's also used for MPAndroidChart XAxis
 */

public class TimeValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return (int) (value / 6) + "min";
    }
}
