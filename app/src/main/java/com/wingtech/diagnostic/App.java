package com.wingtech.diagnostic;

import android.app.Activity;
import android.app.Application;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.ArrayMap;

import java.util.ArrayList;

/**
 * Created by xiekui on 17-8-7.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private ArrayMap<String, Integer> mResMap;
    private static ArrayList<Activity> mList;

    @Override
    public void onCreate() {
        super.onCreate();
        mList = new ArrayList<>();
        mResMap = new ArrayMap<>();
        registerActivityLifecycleCallbacks(this);
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

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mList.remove(activity);
    }

    public static void exit() {
        mList.forEach(Activity::finish);
    }
}
