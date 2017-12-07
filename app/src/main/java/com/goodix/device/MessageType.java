package com.goodix.device;

public class MessageType {
    
    public static final int MSG_TYPE_COMMON_BASE = 0x00000000;
    public static final int MSG_TYPE_COMMON_TOUCH = MSG_TYPE_COMMON_BASE + 1;
    public static final int MSG_TYPE_COMMON_UNTOUCH = MSG_TYPE_COMMON_BASE + 2;
    public static final int MSG_TYPE_COMMON_NOTIFY_INFO = MSG_TYPE_COMMON_BASE + 7;
    public static final int MSG_TYPE_COMMON_HB = MSG_TYPE_COMMON_BASE + 32;
    public static final int MSG_TYPE_UPDATE_BASE_FINISHED = 9;
    public static final int MSG_TYPE_REGISTER_BASE = 0x00000010;
    public static final int MSG_TYPE_REGISTER_PIECE = MSG_TYPE_REGISTER_BASE + 1;
    public static final int MSG_TYPE_REGISTER_NO_PIECE = MSG_TYPE_REGISTER_BASE + 2;
    public static final int MSG_TYPE_REGISTER_NO_EXTRAINFO = MSG_TYPE_REGISTER_BASE + 3;
    public static final int MSG_TYPE_REGISTER_LOW_COVER = MSG_TYPE_REGISTER_BASE + 4;
    public static final int MSG_TYPE_REGISTER_BAD_IMAGE = MSG_TYPE_REGISTER_BASE + 5;
    public static final int MSG_TYPE_REGISTER_GET_DATA_FAILED = MSG_TYPE_REGISTER_BASE + 6;
    public static final int MSG_TYPE_REGISTER_TIMEOUT = MSG_TYPE_REGISTER_BASE + 7;
    public static final int MSG_TYPE_REGISTER_COMPLETE = MSG_TYPE_REGISTER_BASE + 8;
    public static final int MSG_TYPE_REGISTER_CANCEL = MSG_TYPE_REGISTER_BASE + 9;
    public static final int MSG_TYPE_REGISTER_IAMGE = MSG_TYPE_REGISTER_BASE + 10;
    public static final int MSG_TYPE_REGISTER_IAMGE_INFO = MSG_TYPE_REGISTER_BASE + 11;
    public static final int MSG_TYPE_REGISTER_NOT_GSC = MSG_TYPE_REGISTER_BASE + 12;
    public static final int MSG_TYPE_DELETE_BASE = 0x00001000;
    public static final int MSG_TYPE_DELETE_SUCCESS = MSG_TYPE_DELETE_BASE + 1;
    public static final int MSG_TYPE_DELETE_NOEXIST = MSG_TYPE_DELETE_BASE + 2;
    public static final int MSG_TYPE_DELETE_TIMEOUT = MSG_TYPE_DELETE_BASE + 3;
    public static final int MSG_TYPE_CAPTURE_COMPLETE = 5000;
    public static final int MSG_TYPE_FAR_COMPLETE = 5001;
    public static final int MSG_TYPE_FRR_COMPLETE = 5002;
    public final static int MSG_ENROLL_FROM_PIC_COMPLETE = 5003;
    public final static int MSG_ENROLL_FROM_PIC_ERROR = 5004;
    public final static int MSG_ENROLL_FROM_PIC_SUCESS = 5005;
    public final static int MSG_HBD_TEST_ERROR = 5006;

    public final static int MSG_GET_SUB_THREAD_TOAST = 10000;
    public final static int MSG_NOTICE_HASNOT_DATAS = 10001;
    
    public static final int MSG_TYPE_GET_BITMAP_SUCCESS = 300;
    public static final int MSG_TYPE_GET_BITMAP_FAIL = 301;
    public static final int MSG_TYPE_GET_BITMAP_INFO = 302;
    public static final int MSG_TYPE_GET_BITMAP_RAWDATA_SUCCESS = 303;
    public static final int MSG_TYPE_GET_GSC_SUCCESS = 304;
    public static final int MSG_TYPE_ERROR = 0x00010000;
    public static final int MSG_TYPE_RECONGNIZE_BASE = 0x00000100;
    public static final int MSG_TYPE_RECONGNIZE_SUCCESS = MSG_TYPE_RECONGNIZE_BASE + 1;
    public static final int MSG_TYPE_RECONGNIZE_TIMEOUT = MSG_TYPE_RECONGNIZE_BASE + 2;
    public static final int MSG_TYPE_RECONGNIZE_FAILED = MSG_TYPE_RECONGNIZE_BASE + 3;
    public static final int MSG_TYPE_RECONGNIZE_BAD_IMAGE = MSG_TYPE_RECONGNIZE_BASE + 4;
    public static final int MSG_TYPE_RECONGNIZE_GET_DATA_FAILED = MSG_TYPE_RECONGNIZE_BASE + 5;
    public static final int MSG_TYPE_RECONGNIZE_NO_REGISTER_DATA = MSG_TYPE_RECONGNIZE_BASE + 6;
    public static final int MSG_TYPE_RECONGNIZE_IAMGE = MSG_TYPE_RECONGNIZE_BASE + 8;
    public static final int MSG_TYPE_RECONGNIZE_IAMGE_INFO = MSG_TYPE_RECONGNIZE_BASE + 9;
    public static final int MSG_TYPE_RECONGNIZE_NOT_GSC = MSG_TYPE_RECONGNIZE_BASE + 10;
    
    //for FARFRR
    public static final int FINGERPRINT_CMD_GET_BITMAP_CANCEL = 0;
    public static final int FINGERPRINT_CMD_GET_BITMAP = 1;
    public static final int FINGERPRINT_CMD_REG_FROM_BMP = 2;
    public static final int FINGERPRINT_CMD_REG_FROM_BMP_CANCEL = 3;
    public static final int FINGERPRINT_CMD_REG_SAVE = 4;
    public static final int FINGERPRINT_CMD_VERIFY_BMP = 5;
    public static final int FINGERPRINT_CMD_DEL_BMP_TEMPLATE = 6;
    public static final int FINGERPRINT_CMD_AUTO_CALIBRATION = 7;
    public static final int FINGERPRINT_CMD_GET_HARDWARE_INFO = 8;
    //for MP
    public static final int FINGERPRINT_CMD_MP_TEST_INIT = 9;
    public static final int FINGERPRINT_CMD_MP_TEST_SELFTEST = 10;
    public static final int FINGERPRINT_CMD_MP_TEST_PERFORMANCE = 11;
    public static final int FINGERPRINT_CMD_MP_TEST_IMAGE_QUALITY = 12;
    public static final int FINGERPRINT_CMD_MP_TEST_SCENETEST = 13;
    public static final int FINGERPRINT_CMD_MP_TEST_DEFECT_DETECTION = 14;
    public static final int FINGERPRINT_CMD_MP_TEST_PIXEL_DETECTION = 15;
    public static final int FINGERPRINT_CMD_MP_TEST_EXIT = 16;
    public static final int FINGERPRINT_CMD_MP_TEST_FINGER_DOWN = 17;
    public static final int FINGERPRINT_CMD_MP_TEST_FINGER_UP = 18;
    public static final int FINGERPRINT_CMD_SET_ENROLL_CNT = 19; 
    public static final int FINGERPRINT_CMD_GET_ENROLL_CNT = 20; 
    public static final int FINGERPRINT_CMD_MP_CHECK_RING_ENABLE = 21;
    public static final int FINGERPRINT_CMD_MP_CHECK_RING_DISABLE = 22;
    public static final int FINGERPRINT_CMD_MP_TEST_RESET_PIN = 23;
    public static final int FINGERPRINT_CMD_MP_NAVIGATION_ENABLE = 24; 
    public static final int FINGERPRINT_CMD_MP_NAVIGATION_DISABLE = 25; 
    public static final int FINGERPRINT_CMD_MP_TEST_HBD_ENABLE = 26; 
    public static final int FINGERPRINT_CMD_MP_TEST_HBD_DISABLE = 27; 
    public static final int FINGERPRINT_CMD_MP_TEST_HBD_DEBUG_ENABLE = 28; 
    public static final int FINGERPRINT_CMD_MP_TEST_HBD_DEBUG_DISABLE = 29; 
    public static final int FINGERPRINT_CMD_MP_ONE_STOP_TEST = 30;
    public static final int FINGERPRINT_CMD_MP_TEST_SPEED = 31; 
    public static final int FINGERPRINT_CMD_MP_TEST_HBD_GET_BASE = 32; 
    public static final int FINGERPRINT_CMD_MP_TEST_HBD_FLESH_TEST = 33; 
    public static final int FINGERPRINT_CMD_MP_TEST_GET_GSC_DATA = 34; 
    public static final int FINGERPRINT_CMD_MP_TEST_FW_VERSION = 35; 
    public static final int FINGERPRINT_CMD_MP_SET_HBD_MODE_FLAG = 36;  
    public static final int FINGERPRINT_CMD_MP_INIT_CHIP = 37; 
    public static final int FINGERPRINT_CMD_REG_BMP_IN_ENROLL_BMP_TEST = 38;
    public static final int FINGERPRINT_CMD_SET_EXITMP_FALSE = 39;
    public static final int FINGERPRINT_CMD_MP_CONSISTENCY = 40;
    public static final int FINGERPRINT_CMD_SET_DEFAULT_MODE = 41;
    public static final int FINGERPRINT_CMD_SET_ENABLE_GSC = 42;
    public static final int FINGERPRINT_CMD_SET_DISABLE_GSC = 43;
    public static final int FINGERPRINT_CMD_GET_SUPPORTGSC_INFO = 44;
    public static final int FINGERPRINT_CMD_GET_MAX_ENROLL_NUM = 45;
    
    public static final int ITEM_FARFRR_TEST_GSC = 0;
    public static final int ITEM_MULTI_TEST_GSC = 1;
    public static final int ITEM_HBD_TEST_GSC = 2;
    public static final int ITEM_GSCNOPRESS_TEST_GSC = 3;
    public static final int ITEM_GSCFLESH_TEST_GSC = 4;
    public static final int ITEM_PIXEL_TEST_GSC = 5;
    public static final int ITEM_RESET_TEST_GSC = 6;
    public static final int ITEM_DEFECT_TEST_GSC = 7;
    public static final int ITEM_CONSISTENCY_TEST_GSC = 8;
    public static final int ITEM_KEYMODE_TEST_GSC = 9;
    
    public static final int ITEM_FARFRR_TEST_NOGSC = 0;
    public static final int ITEM_MULTI_TEST_NOGSC = 1;
    public static final int ITEM_PIXEL_TEST_NOGSC = 2;
    public static final int ITEM_RESET_TEST_NOGSC = 3;
    public static final int ITEM_DEFECT_TEST_NOGSC = 4;
    public static final int ITEM_CONSISTENCY_TEST_NOGSC = 5;
    public static final int ITEM_KEYMODE_TEST_NOGSC = 6;
    
    public static String getString(int msg) {
        switch (msg) {
            case MSG_TYPE_COMMON_BASE:
                return "MSG_TYPE_COMMON_BASE";
            case MSG_TYPE_COMMON_TOUCH:
                return "TOUCH";
            case MSG_TYPE_COMMON_UNTOUCH:
                return "UNTOUCH";
            case MSG_TYPE_COMMON_NOTIFY_INFO:
                return "MSG_TYPE_COMMON_NOTIFY_INFO";
            case MSG_TYPE_REGISTER_BASE:
                return "MSG_TYPE_REGISTER_BASE";
            case MSG_TYPE_REGISTER_PIECE:
                return "MSG_TYPE_REGISTER_PIECE";
            case MSG_TYPE_REGISTER_NO_PIECE:
                return "MSG_TYPE_REGISTER_NO_PIECE";
            case MSG_TYPE_REGISTER_NO_EXTRAINFO:
                return "MSG_TYPE_REGISTER_NO_EXTRAINFO";
            case MSG_TYPE_REGISTER_LOW_COVER:
                return "MSG_TYPE_REGISTER_LOW_COVER";
            case MSG_TYPE_REGISTER_BAD_IMAGE:
                return "MSG_TYPE_REGISTER_BAD_IMAGE";
            case MSG_TYPE_REGISTER_GET_DATA_FAILED:
                return "MSG_TYPE_REGISTER_GET_DATA_FAILED";
            case MSG_TYPE_REGISTER_TIMEOUT:
                return "MSG_TYPE_REGISTER_TIMEOUT";
            case MSG_TYPE_REGISTER_COMPLETE:
                return "MSG_TYPE_REGISTER_COMPLETE";
            case MSG_TYPE_REGISTER_CANCEL:
                return "MSG_TYPE_REGISTER_CANCEL";
            case MSG_TYPE_DELETE_BASE:
                return "MSG_TYPE_DELETE_BASE";
            case MSG_TYPE_DELETE_SUCCESS:
                return "MSG_TYPE_DELETE_SUCCESS";
            case MSG_TYPE_DELETE_NOEXIST:
                return "MSG_TYPE_DELETE_NOEXIST";
            case MSG_TYPE_DELETE_TIMEOUT:
                return "MSG_TYPE_DELETE_TIMEOUT";
            case MSG_TYPE_ERROR:
                return "MSG_TYPE_ERROR";
            default:
                return "Message : " + msg;
        }
    }
}
