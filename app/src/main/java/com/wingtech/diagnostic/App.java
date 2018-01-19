package com.wingtech.diagnostic;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.goodix.service.FingerprintManager;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.TestItem;
import com.wingtech.diagnostic.util.TestItemHandler;

import java.util.ArrayList;

/**
 * Created by xiekui on 17-8-7.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private static ArrayList<Activity> mList;
    public static ArrayList<TestItem> mItems;

    private FingerprintManager mFingerprintManager;
    private static App mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mList = new ArrayList<>();
        mItems = new ArrayList<>();
        registerActivityLifecycleCallbacks(this);

        new TestItemHandler(this).start();

        mApplication = this;
        //initFpMangerService();
    }

    @Override
    public void onTerminate() {
        Log.i("onTerminate");
        if (mFingerprintManager != null) {
            mFingerprintManager.unbindService();
        }
        super.onTerminate();
    }

    private void initFpMangerService() {
        mFingerprintManager = new FingerprintManager(this);
    }

    public FingerprintManager getFpServiceManager() {
        if (mFingerprintManager == null) {
            initFpMangerService();
        }
        return mFingerprintManager;
    }

    public synchronized static App getInstance() {
        return mApplication;
    }

    public static void addItems(TestItem item) {
        if (item != null) {
            mItems.add(item);
        }
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
