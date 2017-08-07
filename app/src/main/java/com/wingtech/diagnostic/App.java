package com.wingtech.diagnostic;

import android.app.Application;
import android.content.res.TypedArray;
import android.util.ArrayMap;

/**
 * Created by xiekui on 17-8-7.
 */

public class App extends Application {
    private ArrayMap<String, Integer> mResMap;

    @Override
    public void onCreate() {
        super.onCreate();
        mResMap = new ArrayMap<>();
        String[] testCases = getResources().getStringArray(R.array.test_cases);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.test_imgs);
        int len = testCases.length;
        for (int i = 0; i < len; i++) {
            mResMap.put(testCases[i], typedArray.getResourceId(i, 0));
        }
    }

    public ArrayMap<String, Integer> getResMap() {
        return mResMap;
    }
}
