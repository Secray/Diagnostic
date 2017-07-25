package com.wingtech.diagnostic.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.v7.widget.AppCompatImageButton;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import com.asus.atd.smmitest.R;

import java.io.IOException;
import java.util.List;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class CameraTestActivity extends BaseActivity implements SurfaceHolder.Callback {
    SurfaceView mSurface;
    AppCompatImageButton mCapture;
    ImageView mPreview;
    View mBtnGroup;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    int mCameraId;

    @Override
    protected int getLayoutResId() {
        return R.layout.content_test_camera;
    }

    @Override
    protected void initViews() {
        mSurface = (SurfaceView) findViewById(R.id.surface);
        mCapture = (AppCompatImageButton) findViewById(R.id.capture);
        mPreview = (ImageView) findViewById(R.id.preview);
        mBtnGroup = findViewById(R.id.judgment);
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, new TakePictureCallback());
            }
        });
    }

    @Override
    protected void initToolbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onWork() {
        mHolder = mSurface.getHolder();
        mHolder.setFixedSize(176, 155);
        mHolder.setKeepScreenOn(true);
        mHolder.addCallback(this);
        mCameraId = getIntent().getIntExtra("camId", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHolder.removeCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mHolder);
                Camera.Parameters params = mCamera.getParameters();
                Camera.Size largestSize = getBestSupportedSize(params
                        .getSupportedPreviewSizes());
                params.setPreviewSize(largestSize.width, largestSize.height);
                params.setPictureSize(largestSize.width, largestSize.height);
                params.setPictureFormat(PixelFormat.JPEG);
                params.setJpegQuality(80);
                params.setPreviewFrameRate(5);
                params.setRotation(90);
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        Camera.Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void prepareCamera() {
        if (mCamera == null) {
            int num = Camera.getNumberOfCameras();
            int defaultCameraId = 0;
            int frontCameraId = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (int i = 0; i < num; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    defaultCameraId = i;
                }

                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    frontCameraId = i;
                }
            }
            mCamera = Camera.open(mCameraId == 0 ? defaultCameraId : frontCameraId);
        }
    }

    private void destroyCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private final class TakePictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null && data.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                mPreview.setVisibility(View.VISIBLE);
                mSurface.setVisibility(View.GONE);
                mCapture.setVisibility(View.GONE);
                mBtnGroup.setVisibility(View.VISIBLE);
                mPreview.setImageBitmap(newBitmap);
            }
        }
    }
}
