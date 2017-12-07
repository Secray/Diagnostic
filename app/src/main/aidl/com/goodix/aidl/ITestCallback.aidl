package com.goodix.aidl;

interface ITestCallback 
{ 
    boolean handleMessage(int msg,int arg0,int arg1,inout byte[] data);
}
