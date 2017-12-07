package com.goodix.util;

import java.util.Arrays;


/*
 * 
 * 
typedef struct Mp_test_result{  //14
    int step;
    int selftest;
    int performance;
    int speed;
    int image_quality;
    int gsc_nopress;
    int gsc_flesh;
    int hbd;
    int scene;
    int defect_detection;
    int pixel_detection;
    int reset;
    int fwVersionTest;
    int constency;
    mp_test_detail detail;
}Mp_test_result_t;

typedef struct mp_test_detail{  //1+5+2+2+2+2+2+10+2+20=48
    int reset_flag;  //for reset
    int fp_time_cost_ms[FP_TIME_NUM];  //for speed
    int quality[2];  //for quality: score & coverage
    int BR0[2];  //for gsc_nopress
    int BR1[2];  //for gsc_flesh
    int RI[2];   //for hbd
    int OpenShortIndex[2];   //for openshort
    int PixelIndex[10];   //for pixel
    int ConsistencyIndex[2];   //for consistency
    int Reserved[20];     //reserved
    unsigned char fwVersion[16];  //for selftest
}mp_test_detail;
 * 
 * */


public class ResultData {
    private int[] intData = new int[62];
    private char[] version = new char[16];
    
    public int getStep() {
        return intData[0];
    }
    
    public int[] getIntDatas() {
        return intData;
    }
    
    public int getResetFlag() {
        return intData[14];
    }
    
    public int[] getSpeedTimes() {
        return Arrays.copyOfRange(intData, 15, 20);
    }
    
    public int[] getQuality() {
        return Arrays.copyOfRange(intData, 20, 22);
    }
    
    public int[] getBR0() {
        return Arrays.copyOfRange(intData, 22, 24);
    }
    
    public int[] getBR1() {
        return Arrays.copyOfRange(intData, 24, 26);
    }
    
    public int[] getRI() {
        return Arrays.copyOfRange(intData, 26, 28);
    }
    
    public int[] getOpenShortIndex() {
        return Arrays.copyOfRange(intData, 28, 30);
    }
    public int[] getPixelIndex() {
        return Arrays.copyOfRange(intData, 30, 40);
    }
    public int[] getConsistencyIndex() {
        return Arrays.copyOfRange(intData, 41, 42);
    }
    
    public int[] getReservedIndex() {
        int[] RI = new int[20];
        System.arraycopy(intData, 42, RI, 0, 20);
        return RI;
    }
    
    public String getFwVersion() {
        char[] subVersion = Arrays.copyOfRange(version, 0, 14);
        return String.valueOf(subVersion);
    }
    
    public void setInt(int index, int data) {
        intData[index] = data;
        // Change Item test result value: success from 1 to 0; fail result all changes to -1
        if (1 <= index && index <= 13) {
            intData[index] = (intData[index] == 1) ? 0 : -1;
        }
    }
    
    public void setChar(int index, char data) {
        version[index] = data;
    }
    
    public int intDataLenth() {
        return intData.length;
    }
    
    public int versionLenth() {
        return version.length;
    }
}
