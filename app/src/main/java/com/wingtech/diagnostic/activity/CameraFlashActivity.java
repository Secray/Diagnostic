package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.asusodm.atd.smmitest.R;

import java.util.Random;


/**
 * Created by gaoweili on 17-7-28.
 */

public class CameraFlashActivity extends TestingActivity implements View.OnClickListener {

    Camera camera = null;
    private Camera.Parameters parameter;
    private CameraManager mCameraManager;
    public static final String TAG = "CameraFlashActivity";
    private int mFlashId = 0;
    private int mIndex;
    private int mCount;
    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common;
    }

    @Override
    protected void initViews() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        findViewById(R.id.action_one).setOnClickListener(this);
        findViewById(R.id.action_two).setOnClickListener(this);
        findViewById(R.id.action_three).setOnClickListener(this);
        findViewById(R.id.action_four).setOnClickListener(this);
        findViewById(R.id.action_five).setOnClickListener(this);
        findViewById(R.id.action_fail).setOnClickListener(this);
        Random r = new Random();
        mIndex = r.nextInt(5) + 1;
    }

    @Override
    protected void initToolbar() {
        mFlashId = getIntent().getIntExtra("flashid", 0);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onWork() {
        ((TextView) findViewById(R.id.test_title)).setText(getIntent().getStringExtra("title"));
        com.wingtech.diagnostic.util.Log.i("index = " + mIndex);
        if (mFlashId == 1)
            setFlashlight(0, false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setFlashlight(mFlashId,true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setFlashlight(mFlashId,false);

                mHandler.postDelayed(this, 500);
                mCount++;

                if (mCount == mIndex) {
                    mHandler.removeCallbacks(this);
                }
            }
        }, 500);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String getCameraId(int flashId) throws CameraAccessException {
        String[] ids = mCameraManager.getCameraIdList();
        for (String id : ids) {
            CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
            Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
            if (1 == flashId) {
                if (flashAvailable != null && flashAvailable && lensFacing != null
                        && lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    return id;
                }
            } else if (0 == flashId) {
                if (flashAvailable != null && flashAvailable && lensFacing != null
                        && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    return id;
                }
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFlashlight(int flashId, boolean enabled) {
        synchronized (this) {
            try {
                String cameraId = getCameraId(flashId);
                if (TextUtils.isEmpty(cameraId)) {
                    Log.e(TAG, "cameraID is null");
                    return;
                } else
                    Log.e(TAG, "wuhaiwen cameraID: " + flashId + " " + cameraId);
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
        mHandler.removeCallbacksAndMessages(null);
        setFlashlight(mFlashId, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_one:
                if (mIndex == 1) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_two:
                if (mIndex == 2) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_three:
                if (mIndex == 3) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_four:
                if (mIndex == 4) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_five:
                if (mIndex == 5) {
                    mResult = true;
                } else {
                    mResult = false;
                }
                break;
            case R.id.action_fail:
                mResult = false;
                break;
        }
        sendResult();
    }
}
