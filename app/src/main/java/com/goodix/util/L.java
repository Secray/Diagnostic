/************************************************************************
 * <p>Title: L.java</p>
 * <p>Description: </p>
 * <p>Copyright (C), 1997-2014, Shenzhen Goodix Technology Co.,Ltd.</p>
 * <p>Company: Goodix</p>
 * @author  
 * @date    2014-10-10
 * @version  1.0
 ************************************************************************/


package com.goodix.util;

import android.util.Log;

/**
 * 
 * @author
 * 
 */
public class L
{
    public static final String TAG = "GF_Demo_Tool";
    public static final String TAGT = "GF_Demo_Tool";
    public static boolean isDebug = true;
    public static boolean isDebugThowable = true;
    
    public static void i(String msg)
    {
        if (isDebug)
        {
            Log.i(TAG, msg);
        }
    }
    
    public static void d(String msg)
    {
        if (isDebug)
        {
            Log.d(TAG, msg);
        }
    }
    
    public static void dT(String msg)
    {
        if (isDebugThowable)
        {
            Log.d(TAGT, msg, new Throwable());
        }
    }
    
    public static void e(String msg)
    {
        if (isDebug)
        {
            Log.e(TAG, msg);
        }
    }
    
    public static void v(String msg)
    {
        if (isDebug)
        {
            Log.v(TAG, msg);
        }
    }
    
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
    
    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
    
    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
    
    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
    
    public static void dT(String tag, String msg)
    {
        if (isDebugThowable)
            Log.d(tag, msg, new Throwable());
        
    }
    
}
