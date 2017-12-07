package android.hardware;
interface FingerPrintClient{
    void notifyCallback(int msgType, int ext1, int ext2);
    void dataCallback(int msgType, in byte[] msgData);
}
