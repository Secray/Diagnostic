package android.hardware;
interface FingerPrint{
    int connect();
    int disconnect();
    byte[] getInfo();
    int setMode(int mode);
    int query();
    int requestPermission(String passwd);
    int sendScreenState(int state);
    int regiest();
    int cancelRegist();
    int registRollback();
    int resetRegist();
    int unRegist(int index);
    int saveRegist(int index);
    int saveRegister(String name);
    int recognize();
    int recognizeWithRestrict(in int[] restrictArray, int sectype);
    int cancelRecognize();
    int setPasswd(String oldPasswd, String newPasswd);
    int checkPasswd(String passwd);
    int delFpTemplates(in int[] deleteIds, int count);
    int[] getTemplateList();
    int setPauseRegisterState(int state);
    int driverTest();
    int modifyFpName(int index, String name);
    byte[] getFpNameById(int index);
    int[] getFpTemplateIdList();
    byte[] alipayTzInvokeCommand(int cmd, in byte[] send_buf);
    int recognizeFido(in byte[] aaid, in byte[] finalchanllenge);
    byte[] sendCmd(int cmd,in byte[] inbuffer);
    
    /*add more func*/
    int enableGsc(int index);
    int enableHbRetrieve();
    int disableHbRetrieve();
    int enableKeyMode(int enable, int keyType);
    
    /*
    //TODO 
    TRANSACTION_FP_SET_SAFE_CLASS,
    TRANSACTION_GF_CMD_M,
    TRANSACTION_FP_SET_USER_ID,
    TRANSACTION_LOAD_ALL_FDDATA,
    TRANSACTION_SET_FPDB_TO_TA,
    */
}
