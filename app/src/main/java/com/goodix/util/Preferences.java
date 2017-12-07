package com.goodix.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

    private static final String mName = "Preferences";
    private static final String NUMBER = "number";
    private static final String HAND = "hand";
    private static final String FINGER = "finger";
    private static final String ENVIRMENT = "envirment";
    private static final String ENVIRMENT_ENROLL= "envirment_enroll";
    private static final String ENVIRMENT_VERIFY = "envirment_verify";
    private static final String ENABLE_ENVIRMENT = "enable_envirment";
    private static final String ENABLE_SHOWSCORE = "enable.showscrore";
    private static final String ENABLE_DEBUG = "enable.debug";

    private static final String SAMPLE_NUM = "sample.num";
    private static final String ENROLL_NUM = "enroll.num";


    public static String getNumber(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getString(NUMBER, "0000000001");
    }

    public static void setNumber(Context context, String number) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putString(NUMBER, number);
        editor.commit();
    }

    public static int getHand(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(HAND, 0);
    }

    public static void setHand(Context context, int hand) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(HAND, hand);
        editor.commit();
    }

    public static int getFinger(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(FINGER, 0);
    }

    public static void setFinger(Context context, int finger) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(FINGER, finger);
        editor.commit();
    }

    public static int getEnvirment(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(ENVIRMENT, 0);
    }

    public static void setEnvirment(Context context, int finger) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(ENVIRMENT, finger);
        editor.commit();
    }

    public static Boolean getEnvirmentEnable(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getBoolean(ENABLE_ENVIRMENT,false);
    }

    public static void setEnvirmentEnable(Context context, Boolean enable) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putBoolean(ENABLE_ENVIRMENT, enable);
        editor.commit();
    }

    public static int getEnvirmentEnroll(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(ENVIRMENT_ENROLL, 0);
    }

    public static void setEnvirmentEnroll(Context context, int finger) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(ENVIRMENT_ENROLL, finger);
        editor.commit();
    }

    public static int getEnvirmentVerify(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(ENVIRMENT_VERIFY, 0);
    }

    public static void setEnvirmentVerify(Context context, int finger) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(ENVIRMENT_VERIFY, finger);
        editor.commit();
    }

    public static boolean getIsEnableShowScore(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getBoolean(ENABLE_SHOWSCORE, false);
    }
    
    public static boolean getIsEnableDebug(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getBoolean(ENABLE_DEBUG, false);
    }

    public static void setIsEnableShowScore(Context context, boolean bEnabled) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putBoolean(ENABLE_SHOWSCORE, bEnabled);
        editor.commit();
    }

    public static void setIsEnableDebug(Context context, boolean bEnabled) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putBoolean(ENABLE_DEBUG, bEnabled);
        editor.commit();
    }

    public static int getSampleNum(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(SAMPLE_NUM, 30);
    }

    public static void setSampleNum(Context context, int  number) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(SAMPLE_NUM, number);
        editor.commit();
    }

    public static int getEnrollNum(Context context) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        return mPfc.getInt(ENROLL_NUM, 8);
    }

    public static void setEnrollNum(Context context, int  number) {
        SharedPreferences mPfc = context.getSharedPreferences(mName,
                Activity.MODE_PRIVATE);
        Editor editor = (Editor) mPfc.edit();
        editor.putInt(ENROLL_NUM, number);
        editor.commit();
    }
}
