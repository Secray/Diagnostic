package com.goodix.util;

import android.R.integer;
import android.text.StaticLayout;
import android.util.Log;

public class TestConfig {
	public static String ENGTEST_PATH = "/sdcard/Goodix/";
    public static String REG_PATH = "/sdcard/Goodix/Template/";
    public static String HBD_PATH = "/sdcard/Goodix/Heartbeat/";
    public static String HBD_REF_PATH = "/sdcard/Goodix/Heartbeat_ref/";
    public static String SAMPLE_PATH = "/sdcard/Goodix/Sample/";
    public static String SAMPLE_FAKE_PATH = "/sdcard/Goodix/Sample_fake/";
    
    public static int GF_OTP_CHIP_MASK = 0xFFF0;  //equal to gf_config.h:GF_OTP_CHIP_MASK
    private static int mRow = 60; // default value
    private static int mCol = 128;// default value
    private static int mChip_version = 0;
    private static boolean isSupportGsc = false;
    private static int mMaxEnrollNum = 30;

    public static void initbitmapConfig(int row, int col, int chip_version) {
        mRow = row;
        mCol = col;
        mChip_version = chip_version; 
        Log.d("FARFRR", "mChip_version = " + Integer.toHexString(mChip_version));
    }
    public static void initMaxEnrollNum(int maxNum) {
        mMaxEnrollNum = maxNum;
    }
    
    public static void initSupportGscInfo(int ret) {
        isSupportGsc = (ret == 0) ? false : true;
        Log.d("FARFRR", "isSupportGsc = " + isSupportGsc);
    }

    public static int getBitmapRow() {
        return mRow;
    }
    public static int getMaxEnrollMax() {
        return mMaxEnrollNum;
    }

    public static int getBitmapCol() {
        return mCol;
    }
    
    public static boolean SupportGsc() {
        /*if ((mChip_version & GF_OTP_CHIP_MASK) == 0x8010
                || (mChip_version & GF_OTP_CHIP_MASK) == 0x8030) {
            //A & C
            return true;
        }else {
            //B & D
            return false;
        } */  
        return isSupportGsc;
    }
}

