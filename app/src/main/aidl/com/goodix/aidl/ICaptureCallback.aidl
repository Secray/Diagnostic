package com.goodix.aidl;

interface ICaptureCallback 
{ 
    boolean handleMessage(int msg,int arg0,int arg1,inout byte[] data);
}
