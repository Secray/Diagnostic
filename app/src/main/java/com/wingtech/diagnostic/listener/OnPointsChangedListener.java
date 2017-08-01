package com.wingtech.diagnostic.listener;

import android.util.ArrayMap;

import com.wingtech.diagnostic.widget.MultiTouchView;

/**
 * @author xiekui
 * @date 2017-7-31
 */

public interface OnPointsChangedListener {
    void onChange(ArrayMap<Integer, MultiTouchView.Point> points);
}
