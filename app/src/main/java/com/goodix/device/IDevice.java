package com.goodix.device;

import android.os.Handler;

/**
 * Created by user on 2017/12/15 0015.
 */

public interface IDevice {
    byte[] sendCmd(int cmd, byte[] data);
    void setDispathcMessageHandler(Handler handler);
    int query();
    int register();
    int cancelRegister();
    int resetRegister();
    int saveRegister(int index);
    int setMode(int mode);
    int recognize();
    int delete(int index);
    String getInfo();
    int checkPassword(String password);
    int enableGsc(int index);
    int enableHbRetrieve();
    int disableHbRetrieve();
    int enableKeyMode(int enable, int keyType);
    int getPermission(String password);
    int changePassword(String oldPwd, String newPwd);
    int cancelRecognize();
    int sendScreenState(int state);
}
