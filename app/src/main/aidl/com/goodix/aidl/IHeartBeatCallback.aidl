package com.goodix.aidl;

interface IHeartBeatCallback 
{ 
    boolean handleMessage(int msg,int arg0,int arg1,inout byte[] data);//chenyi modify
}