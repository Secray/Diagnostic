package android.hardware;
import android.hardware.FingerPrintClient;
interface IFpService{
    int check(int fpId);
    IBinder connect(FingerPrintClient callback, int clientId);
}
