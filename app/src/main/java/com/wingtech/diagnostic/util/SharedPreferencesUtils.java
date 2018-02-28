package com.wingtech.diagnostic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;


import com.asusodm.atd.smmitest.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by gaoweili on 17-8-1.
 */

public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";
    private static String TAG = "SharedPreferencesUtils";

    public static final int PASS = 2;
    public static final int FAIL = 1;
    public static final int NOT_TEST = 0;

    private static final String RESULT_FILE = "SMMI_TestResult.txt";
    private File targetFile;
    private static BufferedWriter buf;
    private static String[] mTestCases;
    private static int[] mTestCasesErrorCode;
    private static StringBuffer stringSMMI = null;
    private static boolean isTestAll = false;

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.apply();
    }

    public static void setNull(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public static void deleteFile() {
        String emmcFilePath;
        emmcFilePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(emmcFilePath + File.separator + RESULT_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void outputFile(Context context) {
        stringSMMI = new StringBuffer();
        String title = "[SMMI Test Result]\n";
        initfileAndWriteData(title);
        if (Build.MODEL.equals("ASUS_X00LD")) {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_3_cam);
            mTestCasesErrorCode = context.getResources().getIntArray(R.array.smmi_error_code_3_cam);
        } else if (Build.MODEL.contains("ASUS_X00D")) {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_2_cam_zc553kl);
            mTestCasesErrorCode = context.getResources().getIntArray(R.array.smmi_error_code_2_cam_zc553kl);
        } else if (Build.MODEL.contains("ASUS_X017D")) {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_4_cam);
            mTestCasesErrorCode = context.getResources().getIntArray(R.array.smmi_error_code_4_cam);
        } else {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_2_cam);
            mTestCasesErrorCode = context.getResources().getIntArray(R.array.smmi_error_code_2_cam);
        }
        int result = 0;
        isTestAll = true;
        for (int i = 0; i < mTestCases.length; i++) {
            getParam(context, mTestCases[i], NOT_TEST);
            result = (int) getParam(context, mTestCases[i], NOT_TEST);
            Log.i(TAG, "mTestCases[i] = " + mTestCases[i] + "," + "result = " + result);
            stringSMMI.append(mTestCases[i]);
            stringSMMI.append(",");

            stringSMMI.append(mTestCasesErrorCode[i]);
            stringSMMI.append(",");
            stringSMMI.append("Initialize");

            stringSMMI.append("\n");
            initfileAndWriteData(stringSMMI.toString());
            stringSMMI.setLength(0);
        }

        for (int i = 0; i < mTestCases.length; i++) {
            getParam(context, mTestCases[i], NOT_TEST);
            result = (int) getParam(context, mTestCases[i], NOT_TEST);
            Log.i(TAG, "mTestCases[i] = " + mTestCases[i] + "," + "result = " + result);
            if (result == 0) {
                isTestAll = false;
            } else if (result == 1) {
                stringSMMI.append(mTestCases[i]);
                stringSMMI.append(",");
                stringSMMI.append(mTestCasesErrorCode[i]);
                stringSMMI.append(",");
                stringSMMI.append("Fail");
            } else if (result == 2) {
                stringSMMI.append(mTestCases[i]);
                stringSMMI.append(",");
                stringSMMI.append("0");
                stringSMMI.append(",");
                stringSMMI.append("Pass");
            }
            stringSMMI.append("\n");
            initfileAndWriteData(stringSMMI.toString());
            stringSMMI.setLength(0);
        }
        String endString = "[SMMI All Test Done]\n";
        if (isTestAll) {
            initfileAndWriteData(endString);
        }
    }

    public static void initfileAndWriteData(String info) {

        try {
            String emmcFilePath;
            emmcFilePath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(emmcFilePath + "/" + RESULT_FILE);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            //String info = "sdcard test";
            bw.write(info);
            bw.flush();
            Log.i(TAG, "write success!");
            bw.close();
        } catch (Exception e) {
            Log.i(TAG, "write fail!");
            e.printStackTrace();
        }
    }

    public static boolean isIsTestAllDone(Context context) {
        boolean isDone = false;
        if (Build.MODEL.equals("ASUS_X00LD")) {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_3_cam);
        } else if (Build.MODEL.contains("ASUS_X00D")) {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_2_cam_zc553kl);
        } else if (Build.MODEL.contains("ASUS_X017D")) {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_4_cam);
            mTestCasesErrorCode = context.getResources().getIntArray(R.array.smmi_error_code_4_cam);
        } else {
            mTestCases = context.getResources().getStringArray(R.array.test_cases_smmi_2_cam);
        }
        for (int i = 0; i < mTestCases.length; i++) {
            getParam(context, mTestCases[i], NOT_TEST);
            int result = (int) getParam(context, mTestCases[i], NOT_TEST);
            if (result == 0) {
               isDone = false;
               break;
            }

            if (i == mTestCases.length - 1) {
                isDone = true;
            }
        }
        return isDone;
    }
}
