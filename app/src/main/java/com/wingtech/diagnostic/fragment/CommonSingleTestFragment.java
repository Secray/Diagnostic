package com.wingtech.diagnostic.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.activity.BoardMicActivity;
import com.wingtech.diagnostic.activity.CallActivity;
import com.wingtech.diagnostic.activity.CameraFlashActivity;
import com.wingtech.diagnostic.activity.CameraTestActivity;
import com.wingtech.diagnostic.activity.CellularNetworkActivity;
import com.wingtech.diagnostic.activity.ChargingActivity;
import com.wingtech.diagnostic.activity.DisplayActivity;
import com.wingtech.diagnostic.activity.GSensorTestActivity;
import com.wingtech.diagnostic.activity.HeadsetActivity;
import com.wingtech.diagnostic.activity.HeadsetKeyActivity;
import com.wingtech.diagnostic.activity.HeadsetMicActivity;
import com.wingtech.diagnostic.activity.KeypadActivity;
import com.wingtech.diagnostic.activity.LightSensorActivity;
import com.wingtech.diagnostic.activity.MultiTouchTestingActivity;
import com.wingtech.diagnostic.activity.NfcActivity;
import com.wingtech.diagnostic.activity.ProximityActivity;
import com.wingtech.diagnostic.activity.RecieverActivity;
import com.wingtech.diagnostic.activity.SecondaryMicActivity;
import com.wingtech.diagnostic.activity.SingleTestingActivity;
import com.wingtech.diagnostic.activity.SpeakerActivity;
import com.wingtech.diagnostic.activity.TouchTestActivity;
import com.wingtech.diagnostic.activity.VibratorTestingActivity;
import com.wingtech.diagnostic.activity.VirtualKeyActivity;
import com.wingtech.diagnostic.dialog.LoadingDialog;
import com.wingtech.diagnostic.listener.OnResultChangedCallback;
import com.wingtech.diagnostic.listener.OnTestItemListener;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;
import com.wingtech.diagnostic.util.TestItem;

import static com.wingtech.diagnostic.util.Constants.ASSITSCAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.BATTERY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.BLUETOOTH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CALL_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERAFLASH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERAFRONTFLASH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.CELLULAR_NETWORK_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.DISPLAY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.E_COMPASS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.FINGERPRINT_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GPS_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.GYROSCOPE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.G_SENSOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETKEY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSETMIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.HEADSET_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.KEYPAD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.LIGHTSENSOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MAIN_WIDE_CAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MODEM_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MOUSE_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.MULTI_TOUCH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.NFC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.PROXIMITY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.RECIEVER_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SDCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SECONDMIC_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIM2_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SIMCARD_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.SPEAK_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.TOUCH_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VGACAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VIBRATOR_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VIRTUAL_KEY_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIFI_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.WIRECHARGKEY_REQUEST_CODE;
/**
 * @author xiekui
 * @date 2017-7-20
 */

public class CommonSingleTestFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mTestImg;
    private View mTestResultField;
    private ImageView mTestResultImg;
    private TextView mTestResult;
    private TextView mDiscription;
    AppCompatButton mTestBtn;
    private OnTestItemListener mTestItemListener;
    private OnResultChangedCallback mCallback;
    private String mTitle;
    private TestItem mTestItem;
    private int flashId = 0;
    private int camId = 0;

    public void setOnTestItemListener(OnTestItemListener listener) {
        this.mTestItemListener = listener;
    }

    public void setOnResultChangedCallback(OnResultChangedCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initViewEvents(View view) {
        mTestBtn = (AppCompatButton) view.findViewById(R.id.test_action);
        mTestImg = (ImageView) view.findViewById(R.id.img_test);
        mTestResultField = view.findViewById(R.id.result_field);
        mTestResultImg = (ImageView) view.findViewById(R.id.ic_test_result);
        mTestResult = (TextView) view.findViewById(R.id.txt_test_result);
        mDiscription = (TextView) view.findViewById(R.id.test_description);
        mTestBtn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mTestItem = mTestItemListener.getTestItem();
        mTitle = mTestItem.getName();
        mTestImg.setImageResource(mTestItem.getImg());
        mDiscription.setText(returnDiscrip(mTitle));
    }

    private String returnDiscrip(String result){
        String dis = null;
        switch (mTitle) {
            case "Bluetooth Test":
                String[] discription_bt = getResources().getStringArray(R.array.TestItem_Des_BT_Test);
                dis =  discription_bt[0];
                for (int i = 1; i < discription_bt.length; i++){
                    dis = dis + discription_bt[i];
                    dis += "\n";
                }
                break;
            case "Battery Test":
                String[] discription_br = getResources().getStringArray(R.array.TestItem_Des_Battery_Test);
                dis =  discription_br[0];
                for (int i = 1; i < discription_br.length; i++){
                    dis = dis + discription_br[i];
                    dis += "\n";
                }
                break;
            case "E-Compass Test":
                String[] discription_ep = getResources().getStringArray(R.array.TestItem_Des_ECompass_Test);
                dis =  discription_ep[0];
                for (int i = 1; i < discription_ep.length; i++){
                    dis = dis + discription_ep[i];
                    dis += "\n";
                }
                break;
            case "Gyroscope Test":
                String[] discription_gyro = getResources().getStringArray(R.array.TestItem_Des_Gyroscope_Test);
                dis =  discription_gyro[0];
                for (int i = 1; i < discription_gyro.length; i++){
                    dis = dis + discription_gyro[i];
                    dis += "\n";
                }
                break;
            case "Wi-Fi Test":
                String[] discription_wifi = getResources().getStringArray(R.array.TestItem_Des_WiFiBus_Test);
                dis =  discription_wifi[0];
                for (int i = 1; i < discription_wifi.length; i++){
                    dis = dis + discription_wifi[i];
                    dis += "\n";
                }
                break;
            case "SD Card Test":
                String[] discription_sd = getResources().getStringArray(R.array.TestItem_Des_SDcard_Test);
                dis =  discription_sd[0];
                for (int i = 1; i < discription_sd.length; i++){
                    dis = dis + discription_sd[i];
                    dis += "\n";
                }
                break;
            case "SIM Card Test":
                String[] discription_sim1 = getResources().getStringArray(R.array.TestItem_Des_SIMcard_Test);
                dis =  discription_sim1[0];
                for (int i = 1; i < discription_sim1.length; i++){
                    dis = dis + discription_sim1[i];
                    dis += "\n";
                }
                break;
            case "SIM Card2 Test":
                String[] discription_sim2 = getResources().getStringArray(R.array.TestItem_Des_SIMcard_Test2);
                dis =  discription_sim2[0];
                for (int i = 1; i < discription_sim2.length; i++){
                    dis = dis + discription_sim2[i];
                    dis += "\n";
                }
                break;
            case "G-Sensor Test":
                String[] discription_gs = getResources().getStringArray(R.array.TestItem_Des_GSensor_Test);
                dis =  discription_gs[0];
                for (int i = 1; i < discription_gs.length; i++){
                    dis = dis + discription_gs[i];
                    dis += "\n";
                }
                break;
            case "MultiTouch Test":
                String[] discription_mt = getResources().getStringArray(R.array.TestItem_Des_MultiTouch_Test);
                dis =  discription_mt[0];
                for (int i = 1; i < discription_mt.length; i++){
                    dis = dis + discription_mt[i];
                    dis += "\n";
                }
                break;
            case "Vibrator Test":
                String[] discription_vib = getResources().getStringArray(R.array.TestItem_Des_Vibrator_Test);
                dis =  discription_vib[0];
                for (int i = 1; i < discription_vib.length; i++){
                    dis = dis + discription_vib[i];
                    dis += "\n";
                }
                break;
            case "CMD Mouse Test":
                String[] discription_mouse = getResources().getStringArray(R.array.TestItem_Des_CMD_Mouse_Test);
                dis =  discription_mouse[0];
                for (int i = 1; i < discription_mouse.length; i++){
                    dis = dis + discription_mouse[i];
                    dis += "\n";
                }
                break;
            case "Proximity Test":
                String[] discription_pro = getResources().getStringArray(R.array.TestItem_Des_Proximity_Test);
                dis =  discription_pro[0];
                for (int i = 1; i < discription_pro.length; i++){
                    dis = dis + discription_pro[i];
                    dis += "\n";
                }
                break;
            case "Display Test":
                String[] discription_dp = getResources().getStringArray(R.array.TestItem_Des_Display_Test);
                dis =  discription_dp[0];
                for (int i = 1; i < discription_dp.length; i++){
                    dis = dis + discription_dp[i];
                    dis += "\n";
                }
                break;
            case "HeadsetKey Test":
                String[] discription_hsk = getResources().getStringArray(R.array.TestItem_Des_HeadsetKey_Test);
                dis =  discription_hsk[0];
                for (int i = 1; i < discription_hsk.length; i++){
                    dis = dis + discription_hsk[i];
                    dis += "\n";
                }
                break;
            case "LightSensor Test":
                String[] discription_ls = getResources().getStringArray(R.array.TestItem_Des_LightSensor_Test);
                dis =  discription_ls[0];
                for (int i = 1; i < discription_ls.length; i++){
                    dis = dis + discription_ls[i];
                    dis += "\n";
                }
                break;
            case "NFC Test":
                String[] discription_nfc = getResources().getStringArray(R.array.TestItem_Des_NFC_TagReader_Test);
                dis =  discription_nfc[0];
                for (int i = 1; i < discription_nfc.length; i++){
                    dis = dis + discription_nfc[i];
                    dis += "\n";
                }
                break;
            case "Main Camera Capture Test":
                String[] discription_cam = getResources().getStringArray(R.array.TestItem_Des_MainCamCapture_Test);
                dis =  discription_cam[0];
                for (int i = 1; i < discription_cam.length; i++){
                    dis = dis + discription_cam[i];
                    dis += "\n";
                }
                break;
            case "Front Camera Capture Test":
                String[] discription_vga = getResources().getStringArray(R.array.TestItem_Des_VGACamCapture_Test);
                dis =  discription_vga[0];
                for (int i = 1; i < discription_vga.length; i++){
                    dis = dis + discription_vga[i];
                    dis += "\n";
                }
                break;
            case "Front Tele Camera Capture Test":
                String[] discription_telecamera = getResources().getStringArray(R.array.TestItem_Des_FrontTeleCamCapture_Test);
                dis =  discription_telecamera[0];
                for (int i = 1; i < discription_telecamera.length; i++){
                    dis = dis + discription_telecamera[i];
                    dis += "\n";
                }
                break;
            case "Camera Flash Test":
                String[] discription_camfl = getResources().getStringArray(R.array.TestItem_Des_CameraFlash_Test);
                dis =  discription_camfl[0];
                for (int i = 1; i < discription_camfl.length; i++){
                    dis = dis + discription_camfl[i];
                    dis += "\n";
                }
                break;
            case "Receiver Test":
                String[] discription_rv = getResources().getStringArray(R.array.TestItem_Des_Receiver1_Test);
                dis =  discription_rv[0];
                for (int i = 1; i < discription_rv.length; i++){
                    dis = dis + discription_rv[i];
                    dis += "\n";
                }
                break;
            case "Headset Test":
                String[] discription_hs = getResources().getStringArray(R.array.TestItem_Des_Headset_Test);
                dis =  discription_hs[0];
                for (int i = 1; i < discription_hs.length; i++){
                    dis = dis + discription_hs[i];
                    dis += "\n";
                }
                break;
            case "Speaker Test":
                String[] discription_sp = getResources().getStringArray(R.array.TestItem_Des_Speaker_Test);
                dis =  discription_sp[0];
                for (int i = 1; i < discription_sp.length; i++){
                    dis = dis + discription_sp[i];
                    dis += "\n";
                }
                break;
            case "BoardMic Test":
                String[] discription_mic1 = getResources().getStringArray(R.array.TestItem_Des_BoardMic_Test);
                dis =  discription_mic1[0];
                for (int i = 1; i < discription_mic1.length; i++){
                    dis = dis + discription_mic1[i];
                    dis += "\n";
                }
                break;
            case "Modem Test":
                String[] discription_modem = getResources().getStringArray(R.array.TestItem_Des_ModemBus_Test);
                dis =  discription_modem[0];
                for (int i = 1; i < discription_modem.length; i++){
                    dis = dis + discription_modem[i];
                    dis += "\n";
                }
                break;
            case "HeadsetMic Test":
                String[] discription_hsm = getResources().getStringArray(R.array.TestItem_Des_HeadsetMic_Test);
                dis =  discription_hsm[0];
                for (int i = 1; i < discription_hsm.length; i++){
                    dis = dis + discription_hsm[i];
                    dis += "\n";
                }
                break;
            case "Touch Test":
                String[] discription_touch = getResources().getStringArray(R.array.TestItem_Des_Touch_Test);
                dis =  discription_touch[0];
                for (int i = 1; i < discription_touch.length; i++){
                    dis = dis + discription_touch[i];
                    dis += "\n";
                }
                break;
            case "Keypad Test":
                String[] discription_keypad = getResources().getStringArray(R.array.TestItem_Des_Keypad_Test);
                dis =  discription_keypad[0];
                for (int i = 1; i < discription_keypad.length; i++){
                    dis = dis + discription_keypad[i];
                    dis += "\n";
                }
                break;
            case "Fingerprint Test":
                String[] discription_fg = getResources().getStringArray(R.array.TestItem_Des_FingerPrint_Test);
                dis =  discription_fg[0];
                for (int i = 1; i < discription_fg.length; i++){
                    dis = dis + discription_fg[i];
                    dis += "\n";
                }
                break;
            case "BoardMic2 Test":
                String[] discription_mic2 = getResources().getStringArray(R.array.TestItem_Des_BoardMic2_Test);
                dis =  discription_mic2[0];
                for (int i = 1; i < discription_mic2.length; i++){
                    dis = dis + discription_mic2[i];
                    dis += "\n";
                }
                break;
            case "Camera Front Flash Test":
                String[] discription_camf = getResources().getStringArray(R.array.TestItem_Des_CameraFlash_Front_Test);
                dis =  discription_camf[0];
                for (int i = 1; i < discription_camf.length; i++){
                    dis = dis + discription_camf[i];
                    dis += "\n";
                }
                break;
            case "Front WideCamera Capture Test":
                String[] discription_assist = getResources().getStringArray(R.array.TestItem_Des_FrontWideCamCapture_Test);
                dis =  discription_assist[0];
                for (int i = 1; i < discription_assist.length; i++){
                    dis = dis + discription_assist[i];
                    dis += "\n";
                }
                break;
            case "GPS Test":
                String[] discription_gps = getResources().getStringArray(R.array.TestItem_Des_GPS_Test);
                dis =  discription_gps[0];
                for (int i = 1; i < discription_gps.length; i++){
                    dis = dis + discription_gps[i];
                    dis += "\n";
                }
                break;
            case "Phone Call Test":
                String[] discription_call = getResources().getStringArray(R.array.TestItem_Des_PhoneCall_Test);
                dis =  discription_call[0];
                for (int i = 1; i < discription_call.length; i++){
                    dis = dis + discription_call[i];
                    dis += "\n";
                }
                break;
            case "SIM Signal Test":
                String[] discription_ce = getResources().getStringArray(R.array.TestItem_Des_SIM_Signal_Test);
                dis =  discription_ce[0] + "\n";
                for (int i = 1; i < discription_ce.length; i++){
                    dis = dis + discription_ce[i];
                    dis += "\n";
                }
                break;
            case "AC Charging Test":
                String[] discription_ac = getResources().getStringArray(R.array.TestItem_Des_AC_Charger_Test);
                dis =  discription_ac[0];
                for (int i = 1; i < discription_ac.length; i++){
                    dis = dis + discription_ac[i];
                    dis += "\n";
                }
                break;
            case "USB Charging Test":
                String[] discription_usb = getResources().getStringArray(R.array.TestItem_Des_USBCharger_Test);
                dis =  discription_usb[0];
                for (int i = 1; i < discription_usb.length; i++){
                    dis = dis + discription_usb[i];
                    dis += "\n";
                }
                break;
            default:
                dis = "";
                break;
        }
        Log.d("gaoweili", "dis:" + dis);
        return dis;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case BLUETOOTH_REQUEST_CODE:
            case BATTERY_REQUEST_CODE:
            case E_COMPASS_REQUEST_CODE:
            case GYROSCOPE_REQUEST_CODE:
            case WIFI_REQUEST_CODE:
            case SDCARD_REQUEST_CODE:
            case SIMCARD_REQUEST_CODE:
            case SIM2_REQUEST_CODE:
            case G_SENSOR_REQUEST_CODE:
            case MULTI_TOUCH_REQUEST_CODE:
            case VIBRATOR_REQUEST_CODE:
            case MOUSE_REQUEST_CODE:
            case PROXIMITY_REQUEST_CODE:
            case DISPLAY_REQUEST_CODE:
            case HEADSETKEY_REQUEST_CODE:
            case LIGHTSENSOR_REQUEST_CODE:
            case NFC_REQUEST_CODE:
            case WIRECHARGKEY_REQUEST_CODE:
            case CAMERA_REQUEST_CODE:
            case VGACAMERA_REQUEST_CODE:
            case CAMERAFLASH_REQUEST_CODE:
            case RECIEVER_REQUEST_CODE:
            case HEADSET_REQUEST_CODE:
            case SPEAK_REQUEST_CODE:
            case MIC_REQUEST_CODE:
            case MODEM_REQUEST_CODE:
            case HEADSETMIC_REQUEST_CODE:
            case TOUCH_REQUEST_CODE:
            case KEYPAD_REQUEST_CODE:
            case FINGERPRINT_REQUEST_CODE:
            case SECONDMIC_REQUEST_CODE:
            case CAMERAFRONTFLASH_REQUEST_CODE:
            case ASSITSCAMERA_REQUEST_CODE:
            case GPS_REQUEST_CODE:
            case CALL_REQUEST_CODE:
            case CELLULAR_NETWORK_REQUEST_CODE:
            case VIRTUAL_KEY_REQUEST_CODE:
            case MAIN_WIDE_CAMERA_REQUEST_CODE:
                boolean result = data.getBooleanExtra("result", false);
                Log.d("gaoweili", "gaoweili:");
                mDiscription.setText(returnDiscription(resultCode));
                if (mCallback != null) {
                    mCallback.onChange(result);
                } else {
                    mTestResultField.setVisibility(View.VISIBLE);
                    if (result) {
                        mTestResult.setText(getResources().getString(R.string.test_pass, mTitle));
                        mTestResult.setTextColor(getResources().getColor(R.color.test_result_pass));
                        mTestResultImg.setImageResource(R.drawable.asus_diagnostic_ic_pass);
                        SharedPreferencesUtils.setParam(mActivity, mTitle, SharedPreferencesUtils.PASS);
                    } else {
                        mTestResult.setText(getResources().getString(R.string.test_fail, mTitle));
                        mTestResult.setTextColor(getResources().getColor(R.color.test_result_fail));
                        mTestResultImg.setImageResource(R.drawable.asus_diagnostic_ic_fail);
                        SharedPreferencesUtils.setParam(mActivity, mTitle, SharedPreferencesUtils.FAIL);
                    }
                    mTestBtn.setText(R.string.btn_test_again);
                }
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferencesUtils.deleteFile();
                        SharedPreferencesUtils.outputFile(mActivity);
                        Log.i("gaoweili","file del output");
                    }
                }).start();
                break;
        }
    }

    private String returnDiscription(int code){
        String dis = null;
        switch (code) {
            case BLUETOOTH_REQUEST_CODE:
                String[] discription_bt = getResources().getStringArray(R.array.TestItem_Des_BT_Test);
                dis =  discription_bt[0];
                for (int i = 1; i < discription_bt.length; i++){
                    dis = dis + discription_bt[i];
                    dis += "\n";
                }
                break;
            case BATTERY_REQUEST_CODE:
                String[] discription_br = getResources().getStringArray(R.array.TestItem_Des_Battery_Test);
                dis =  discription_br[0];
                for (int i = 1; i < discription_br.length; i++){
                    dis = dis + discription_br[i];
                    dis += "\n";
                }
                break;
            case E_COMPASS_REQUEST_CODE:
                String[] discription_ep = getResources().getStringArray(R.array.TestItem_Des_ECompass_Test);
                dis =  discription_ep[0];
                for (int i = 1; i < discription_ep.length; i++){
                    dis = dis + discription_ep[i];
                    dis += "\n";
                }
                break;
            case GYROSCOPE_REQUEST_CODE:
                String[] discription_gyro = getResources().getStringArray(R.array.TestItem_Des_Gyroscope_Test);
                dis =  discription_gyro[0];
                for (int i = 1; i < discription_gyro.length; i++){
                    dis = dis + discription_gyro[i];
                    dis += "\n";
                }
                break;
            case WIFI_REQUEST_CODE:
                String[] discription_wifi = getResources().getStringArray(R.array.TestItem_Des_WiFiBus_Test);
                dis =  discription_wifi[0];
                for (int i = 1; i < discription_wifi.length; i++){
                    dis = dis + discription_wifi[i];
                    dis += "\n";
                }
                break;
            case SDCARD_REQUEST_CODE:
                String[] discription_sd = getResources().getStringArray(R.array.TestItem_Des_SDcard_Test);
                dis =  discription_sd[0];
                for (int i = 1; i < discription_sd.length; i++){
                    dis = dis + discription_sd[i];
                    dis += "\n";
                }
                break;
            case SIMCARD_REQUEST_CODE:
                String[] discription_sim1 = getResources().getStringArray(R.array.TestItem_Des_SIMcard_Test);
                dis =  discription_sim1[0];
                for (int i = 1; i < discription_sim1.length; i++){
                    dis = dis + discription_sim1[i];
                    dis += "\n";
                }
                break;
            case SIM2_REQUEST_CODE:
                String[] discription_sim2 = getResources().getStringArray(R.array.TestItem_Des_SIMcard_Test2);
                dis =  discription_sim2[0];
                for (int i = 1; i < discription_sim2.length; i++){
                    dis = dis + discription_sim2[i];
                    dis += "\n";
                }
                break;
            case G_SENSOR_REQUEST_CODE:
                String[] discription_gs = getResources().getStringArray(R.array.TestItem_Des_GSensor_Test);
                dis =  discription_gs[0];
                for (int i = 1; i < discription_gs.length; i++){
                    dis = dis + discription_gs[i];
                    dis += "\n";
                }
                break;
            case MULTI_TOUCH_REQUEST_CODE:
                String[] discription_mt = getResources().getStringArray(R.array.TestItem_Des_MultiTouch_Test);
                dis =  discription_mt[0];
                for (int i = 1; i < discription_mt.length; i++){
                    dis = dis + discription_mt[i];
                    dis += "\n";
                }
                break;
            case VIBRATOR_REQUEST_CODE:
                String[] discription_vib = getResources().getStringArray(R.array.TestItem_Des_Vibrator_Test);
                dis =  discription_vib[0];
                for (int i = 1; i < discription_vib.length; i++){
                    dis = dis + discription_vib[i];
                    dis += "\n";
                }
                break;
            case MOUSE_REQUEST_CODE:
                String[] discription_mouse = getResources().getStringArray(R.array.TestItem_Des_CMD_Mouse_Test);
                dis =  discription_mouse[0];
                for (int i = 1; i < discription_mouse.length; i++){
                    dis = dis + discription_mouse[i];
                    dis += "\n";
                }
                break;
            case PROXIMITY_REQUEST_CODE:
                String[] discription_pro = getResources().getStringArray(R.array.TestItem_Des_Proximity_Test);
                dis =  discription_pro[0];
                for (int i = 1; i < discription_pro.length; i++){
                    dis = dis + discription_pro[i];
                    dis += "\n";
                }
                break;
            case DISPLAY_REQUEST_CODE:
                String[] discription_dp = getResources().getStringArray(R.array.TestItem_Des_Display_Test);
                dis =  discription_dp[0];
                for (int i = 1; i < discription_dp.length; i++){
                    dis = dis + discription_dp[i];
                    dis += "\n";
                }
                break;
            case HEADSETKEY_REQUEST_CODE:
                String[] discription_hsk = getResources().getStringArray(R.array.TestItem_Des_HeadsetKey_Test);
                dis =  discription_hsk[0];
                for (int i = 1; i < discription_hsk.length; i++){
                    dis = dis + discription_hsk[i];
                    dis += "\n";
                }
                break;
            case LIGHTSENSOR_REQUEST_CODE:
                String[] discription_ls = getResources().getStringArray(R.array.TestItem_Des_LightSensor_Test);
                dis =  discription_ls[0];
                for (int i = 1; i < discription_ls.length; i++){
                    dis = dis + discription_ls[i];
                    dis += "\n";
                }
                break;
            case NFC_REQUEST_CODE:
                String[] discription_nfc = getResources().getStringArray(R.array.TestItem_Des_NFC_TagReader_Test);
                dis =  discription_nfc[0];
                for (int i = 1; i < discription_nfc.length; i++){
                    dis = dis + discription_nfc[i];
                    dis += "\n";
                }
                break;
            case WIRECHARGKEY_REQUEST_CODE:
                String[] discription_wc = getResources().getStringArray(R.array.TestItem_Des_Wireless_Charger_Test);
                dis =  discription_wc[0];
                for (int i = 1; i < discription_wc.length; i++){
                    dis = dis + discription_wc[i];
                    dis += "\n";
                }
                break;
            case CAMERA_REQUEST_CODE:
                String[] discription_cam = getResources().getStringArray(R.array.TestItem_Des_MainCamCapture_Test);
                dis =  discription_cam[0];
                for (int i = 1; i < discription_cam.length; i++){
                    dis = dis + discription_cam[i];
                    dis += "\n";
                }
                break;
            case MAIN_WIDE_CAMERA_REQUEST_CODE:
                String[] discription_wide_cam = getResources().getStringArray(R.array.TestItem_Des_MainWideCamCapture_Test);
                dis =  discription_wide_cam[0];
                for (int i = 1; i < discription_wide_cam.length; i++){
                    dis = dis + discription_wide_cam[i];
                    dis += "\n";
                }
                break;
            case VGACAMERA_REQUEST_CODE:
                String[] discription_vga = getResources().getStringArray(R.array.TestItem_Des_VGACamCapture_Test);
                dis =  discription_vga[0];
                for (int i = 1; i < discription_vga.length; i++){
                    dis = dis + discription_vga[i];
                    dis += "\n";
                }
                break;
            case CAMERAFLASH_REQUEST_CODE:
                String[] discription_camfl = getResources().getStringArray(R.array.TestItem_Des_CameraFlash_Test);
                dis =  discription_camfl[0];
                for (int i = 1; i < discription_camfl.length; i++){
                    dis = dis + discription_camfl[i];
                    dis += "\n";
                }
                break;
            case RECIEVER_REQUEST_CODE:
                String[] discription_rv = getResources().getStringArray(R.array.TestItem_Des_Receiver1_Test);
                dis =  discription_rv[0];
                for (int i = 1; i < discription_rv.length; i++){
                    dis = dis + discription_rv[i];
                    dis += "\n";
                }
                break;
            case HEADSET_REQUEST_CODE:
                String[] discription_hs = getResources().getStringArray(R.array.TestItem_Des_Headset_Test);
                dis =  discription_hs[0];
                for (int i = 1; i < discription_hs.length; i++){
                    dis = dis + discription_hs[i];
                    dis += "\n";
                }
                break;
            case SPEAK_REQUEST_CODE:
                String[] discription_sp = getResources().getStringArray(R.array.TestItem_Des_Speaker_Test);
                dis =  discription_sp[0];
                for (int i = 1; i < discription_sp.length; i++){
                    dis = dis + discription_sp[i];
                    dis += "\n";
                }
                break;
            case MIC_REQUEST_CODE:
                String[] discription_mic1 = getResources().getStringArray(R.array.TestItem_Des_BoardMic_Test);
                dis =  discription_mic1[0];
                for (int i = 1; i < discription_mic1.length; i++){
                    dis = dis + discription_mic1[i];
                    dis += "\n";
                }
                break;
            case MODEM_REQUEST_CODE:
                String[] discription_modem = getResources().getStringArray(R.array.TestItem_Des_ModemBus_Test);
                dis =  discription_modem[0];
                for (int i = 1; i < discription_modem.length; i++){
                    dis = dis + discription_modem[i];
                    dis += "\n";
                }
                break;
            case HEADSETMIC_REQUEST_CODE:
                String[] discription_hsm = getResources().getStringArray(R.array.TestItem_Des_HeadsetMic_Test);
                dis =  discription_hsm[0];
                for (int i = 1; i < discription_hsm.length; i++){
                    dis = dis + discription_hsm[i];
                    dis += "\n";
                }
                break;
            case TOUCH_REQUEST_CODE:
                String[] discription_touch = getResources().getStringArray(R.array.TestItem_Des_Touch_Test);
                dis =  discription_touch[0];
                for (int i = 1; i < discription_touch.length; i++){
                    dis = dis + discription_touch[i];
                    dis += "\n";
                }
                break;
            case KEYPAD_REQUEST_CODE:
                String[] discription_keypad = getResources().getStringArray(R.array.TestItem_Des_Keypad_Test);
                dis =  discription_keypad[0];
                for (int i = 1; i < discription_keypad.length; i++){
                    dis = dis + discription_keypad[i];
                    dis += "\n";
                }
                break;
            case FINGERPRINT_REQUEST_CODE:
                String[] discription_fg = getResources().getStringArray(R.array.TestItem_Des_FingerPrint_Test);
                dis =  discription_fg[0];
                for (int i = 1; i < discription_fg.length; i++){
                    dis = dis + discription_fg[i];
                    dis += "\n";
                }
                break;
            case SECONDMIC_REQUEST_CODE:
                String[] discription_mic2 = getResources().getStringArray(R.array.TestItem_Des_BoardMic2_Test);
                dis =  discription_mic2[0];
                for (int i = 1; i < discription_mic2.length; i++){
                    dis = dis + discription_mic2[i];
                    dis += "\n";
                }
                break;
            case CAMERAFRONTFLASH_REQUEST_CODE:
                String[] discription_camf = getResources().getStringArray(R.array.TestItem_Des_CameraFlash_Front_Test);
                dis =  discription_camf[0];
                for (int i = 1; i < discription_camf.length; i++){
                    dis = dis + discription_camf[i];
                    dis += "\n";
                }
                break;
            case ASSITSCAMERA_REQUEST_CODE:
                String[] discription_assist = getResources().getStringArray(R.array.TestItem_Des_WideCamCapture_Test);
                dis =  discription_assist[0];
                for (int i = 1; i < discription_assist.length; i++){
                    dis = dis + discription_assist[i];
                    dis += "\n";
                }
                break;
            case GPS_REQUEST_CODE:
                String[] discription_gps = getResources().getStringArray(R.array.TestItem_Des_GPS_Test);
                dis =  discription_gps[0];
                for (int i = 1; i < discription_gps.length; i++){
                    dis = dis + discription_gps[i];
                    dis += "\n";
                }
                break;
            case CALL_REQUEST_CODE:
                String[] discription_call = getResources().getStringArray(R.array.TestItem_Des_PhoneCall_Test);
                dis =  discription_call[0];
                for (int i = 1; i < discription_call.length; i++){
                    dis = dis + discription_call[i];
                    dis += "\n";
                }
                break;
            case CELLULAR_NETWORK_REQUEST_CODE:
                String[] discription_ce = getResources().getStringArray(R.array.TestItem_Des_SIM_Signal_Test);
                dis =  discription_ce[0] + "\n";
                for (int i = 1; i < discription_ce.length; i++){
                    dis = dis + discription_ce[i];
                    dis += "\n";
                }
                break;
            case VIRTUAL_KEY_REQUEST_CODE:
                dis = "virtual key is not define";
                break;
            default:
                dis = "";
                break;
        }
        Log.d("gaoweili", "dis:" + dis);
        return dis;
    }

    @Override
    public void onClick(View v) {
        switch (mTitle) {
            case "Bluetooth Test":
            case "Modem Test":
            case "Wi-Fi Test":
            case "Battery Test":
            case "E-Compass Test":
            case "Gyroscope Test":
            case "SD Card Test":
            case "SIM Card Test":
            case "SIM Card2 Test":
            case "CMD Mouse Test":
            case "Fingerprint Test":
            case "GPS Test":
                Intent i = new Intent(mActivity, SingleTestingActivity.class);
                i.putExtra("title", mTitle);
                startActivityForResult(i, MOUSE_REQUEST_CODE);
                break;

            case "Vibrator Test":
                i = new Intent(mActivity, VibratorTestingActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, VIBRATOR_REQUEST_CODE);
                break;
            case "G-Sensor Test":
                i = new Intent(mActivity, GSensorTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, G_SENSOR_REQUEST_CODE);
                break;
            case "MultiTouch Test":
                i = new Intent(mActivity, MultiTouchTestingActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, MULTI_TOUCH_REQUEST_CODE);
                break;

            case "Touch Test":
                i = new Intent(mActivity, TouchTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, CAMERA_REQUEST_CODE);
                break;
            case "Main Camera Capture Test":
                camId = 0;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("camId", camId);
                startActivityForResult(i, CAMERA_REQUEST_CODE);
                break;
            case "Front Camera Capture Test":
            case "Front Tele Camera Capture Test":
                camId = 1;
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("camId", camId);
                startActivityForResult(i, VGACAMERA_REQUEST_CODE);
                break;
            case "Front WideCamera Capture Test":
                if (Build.MODEL.equals("ASUS_X017D")) {
                    camId = 3;
                } else {
                    camId = 2;
                }
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("camId", camId);
                startActivityForResult(i, ASSITSCAMERA_REQUEST_CODE);
                break;

            case "Main WideCamera Capture Test":
                if (Build.MODEL.equals("ASUS_X017D")) {
                    camId = 2;
                }
                i = new Intent(mActivity, CameraTestActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("camId", camId);
                startActivityForResult(i, MAIN_WIDE_CAMERA_REQUEST_CODE);
                break;

            case "Camera Flash Test":
                flashId = 0;
                i = new Intent(mActivity, CameraFlashActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("flashid", flashId);
                i.putExtra("title", mTitle);
                startActivityForResult(i, CAMERAFLASH_REQUEST_CODE);
                break;
            case "Camera Front Flash Test":
                flashId = 1;
                i = new Intent(mActivity, CameraFlashActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("flashid", flashId);
                startActivityForResult(i, CAMERAFRONTFLASH_REQUEST_CODE);
                break;
            case "Display Test":
                i = new Intent(mActivity, DisplayActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                i.putExtra("title_dialog","Display");
                startActivityForResult(i, DISPLAY_REQUEST_CODE);
                break;
            case "Proximity Test":
                i = new Intent(mActivity, ProximityActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                startActivityForResult(i, PROXIMITY_REQUEST_CODE);
                break;
            case "HeadsetKey Test":
                i = new Intent(mActivity, HeadsetKeyActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                startActivityForResult(i, HEADSETKEY_REQUEST_CODE);
                break;
            case "LightSensor Test":
                i = new Intent(mActivity, LightSensorActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                startActivityForResult(i, LIGHTSENSOR_REQUEST_CODE);
                break;
            case "Keypad Test":
                i = new Intent(mActivity, KeypadActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, KEYPAD_REQUEST_CODE);
                break;

            case "Virtual Key Test":
                i = new Intent(mActivity, VirtualKeyActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, VIRTUAL_KEY_REQUEST_CODE);
                break;

            case "NFC Test":
                i = new Intent(mActivity, NfcActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, NFC_REQUEST_CODE);
                break;
            case "Wireless Charging Test":
                i = new Intent(mActivity, ChargingActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                i.putExtra("plugged", 4);
                startActivityForResult(i, WIRECHARGKEY_REQUEST_CODE);
                break;

            case "AC Charging Test":
                i = new Intent(mActivity, ChargingActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                i.putExtra("plugged", 1);
                startActivityForResult(i, WIRECHARGKEY_REQUEST_CODE);
                break;

            case "USB Charging Test":
                i = new Intent(mActivity, ChargingActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                i.putExtra("plugged", 2);
                startActivityForResult(i, WIRECHARGKEY_REQUEST_CODE);
                break;

            case "Phone Call Test":
                i = new Intent(mActivity, CallActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                startActivityForResult(i, CALL_REQUEST_CODE);
                break;

            case "SIM Signal Test":
                i = new Intent(mActivity, CellularNetworkActivity.class);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title", mTitle);
                startActivityForResult(i, CELLULAR_NETWORK_REQUEST_CODE);
                break;

            case "Receiver Test":
                i = new Intent(mActivity, RecieverActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title_dialog","Receiver");
                i.putExtra("context","Playing");
                startActivityForResult(i, RECIEVER_REQUEST_CODE);
                break;
            case "Headset Test":
                i = new Intent(mActivity, HeadsetActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("isTestAll", mCallback != null);
                i.putExtra("title_dialog","Headset");
                startActivityForResult(i, HEADSET_REQUEST_CODE);
                break;
            case "Speaker Test":
                i = new Intent(mActivity, SpeakerActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("title_dialog","Speaker");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, SPEAK_REQUEST_CODE);
                break;
            case "BoardMic Test":
                i = new Intent(mActivity, BoardMicActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("title_dialog","BoardMic");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, MIC_REQUEST_CODE);
                break;
            case "BoardMic2 Test":
                i = new Intent(mActivity, SecondaryMicActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("title_dialog","SecondaryMic");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, SECONDMIC_REQUEST_CODE);
                break;
            case "HeadsetMic Test":
                i = new Intent(mActivity, HeadsetMicActivity.class);
                i.putExtra("title", mTitle);
                i.putExtra("title_dialog","HeadsetMic");
                i.putExtra("isTestAll", mCallback != null);
                startActivityForResult(i, HEADSETMIC_REQUEST_CODE);
                break;
            default:
                Dialog dialog = new LoadingDialog(mActivity, mTitle);
                dialog.show();
                break;
        }
    }
}
