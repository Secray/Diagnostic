package com.goodix.aidl;

import com.goodix.aidl.IEnrollCallback;
import com.goodix.aidl.IVerifyCallback;
import com.goodix.aidl.IRegisterCallback;
import com.goodix.aidl.ICaptureCallback;
import com.goodix.aidl.ITestCallback;
import com.goodix.aidl.IUpdateBaseCallback;
import com.goodix.aidl.IHeartBeatCallback;

interface IFingerprintManager 
{ 
	/*recognize*/
    int verify(IBinder token,IVerifyCallback callback);
    int cancelVerify(IBinder token);
    
    /*register*/
    int enroll(IBinder token,IEnrollCallback callback);
    int resetEnroll(IBinder token);
    int cancelEnroll(IBinder token);
    int saveEnroll(IBinder token,int index);
    
    /*heatbeat*/
    int enterHeatBeat(IBinder token,IHeartBeatCallback callback);
    int cancelHeatBeat(IBinder token);
    
    
    /* fingerprint data*/
    int query();
    int delete(int index);
    int enableGsc(int index);  
    int enableKeyMode(int enable,int keyType);
    
    /*password*/
    // int checkPassword(String password);
    // int changePassword(String oldPassword,String newPassword);
    
    /*engtest such as mode set*/
   // int EngTest(int cmd);
    
    /*get informaton about fingerFrint*/  
    String getInfo();
    
    
    /*********xyf1222*********/
     /*register*/
    int register(IBinder token,IRegisterCallback callback);
    int cancelRegister(IBinder token);
     /*capture*/
    int capture(IBinder token,ICaptureCallback callback);
    int cancelCapture(IBinder token);
     /*test*/
    int test(IBinder token,ITestCallback callback);
    int cancelTest(IBinder token);
    
    /*engtest such as mode set*/
    int EngTest(int cmd,int arg1,int arg2);
    
    /**XYF1223**/
    byte[] SendCmd(int cmd, in byte[] data);
	int setMode(int cmd);
	
    /*set max enroll num*/
    void setEnrollNum(int num);

    /*XYF1119: get the enroll num*/
    byte[] getEnrollNum();

    void updateBaseValue();
    void setUpdateBaseCallback(IUpdateBaseCallback callback);
}
