package com.wingtech.diagnostic.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by xiekui on 17-7-10.
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
}
