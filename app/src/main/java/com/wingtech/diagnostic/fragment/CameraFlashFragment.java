package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.SingleTestActivity;

import static com.wingtech.diagnostic.util.Constants.CAMERAFLASH_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class CameraFlashFragment extends TestFragment {

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
    protected void initViewEvents(View view) {
        mCameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
        mPass = (AppCompatButton) view.findViewById(R.id.pass);
        mFail = (AppCompatButton) view.findViewById(R.id.fail);
        myWork();
    }

    private void myWork() {

        mPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onChange(true);
            }
        });
        mFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onChange(false);
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
    public void onResume() {
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
    public void onPause() {
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
