package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.wingtech.diagnostic.R;

import static com.wingtech.diagnostic.util.Constants.CAMERAFLASH_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class CameraFlashActivity extends TestingActivity {

    Camera  camera = null;
    private Camera.Parameters parameter;
    private CameraManager mCameraManager;
    public static final String TAG = "CameraFlashActivity";
    private AppCompatButton mPass = null;
    private AppCompatButton mFail = null;
    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_camera_flash;
    }

    @Override
    protected void initViews() {
        mCameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        mPass = (AppCompatButton) findViewById(R.id.pass);
        mFail = (AppCompatButton) findViewById(R.id.fail);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = CAMERAFLASH_REQUEST_CODE;
    }

    @Override
    protected void onWork() {

        mPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = true;
                sendResult();
            }
        });
        mFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                sendResult();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String getCameraId() throws CameraAccessException {
        String[] ids = mCameraManager.getCameraIdList();
        for (String id : ids) {
            CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
            Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
            if (flashAvailable != null && flashAvailable && lensFacing != null
                    && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return id;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Log.i(TAG, "end");
        super.onResume();
        setFlashlight(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFlashlight(boolean enabled) {
        synchronized (this) {
            try {
                String cameraId = getCameraId();
                if(TextUtils.isEmpty(cameraId))
                {
                    Log.e(TAG,"cameraID is null");
                    return;
                }
                mCameraManager.setTorchMode(cameraId, enabled);
            } catch (CameraAccessException e) {
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        setFlashlight(false);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
