package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.Log;
import com.wingtech.diagnostic.widget.CameraPreview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.List;

import static com.wingtech.diagnostic.util.Constants.CAMERA_REQUEST_CODE;
import static com.wingtech.diagnostic.util.Constants.VGACAMERA_REQUEST_CODE;


/**
 * @author gaoweili
 * @date 2017-7-24
 */

public class CameraTestActivity extends TestingActivity {
    private AppCompatImageButton mCapture = null;
    private AppCompatImageView mPng = null;
    private AppCompatButton mPass = null;
    private AppCompatButton mFail = null;

    public static final String TAG = "CameraActivity";
    //private Camera mCamera;
    private CameraPreview mPreview  = null;
    private FrameLayout mCameralayout  = null;
    private int mCameraId = 0;
    public static int cancelPreviewType = 0;
    private Camera.AutoFocusCallback mAutoFocusCallback = null;
    private ToneGenerator tone;
    private android.hardware.Camera mCamera;
    //HAL1 version code
    private static final int CAMERA_HAL_API_VERSION_1_0 = 0x100;

    private int cWidth = 0;
    private int cHeight = 0;
    private int tagResult = 0;
    private int tag = 0;

    @Override
    protected int getLayoutResId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.content_test_camera;
    }

    @Override
    protected void initViews() {
        mCapture = (AppCompatImageButton) findViewById(R.id.capture);
        mPng = (AppCompatImageView) findViewById(R.id.png);
        mPass = (AppCompatButton) findViewById(R.id.pass);
        mFail = (AppCompatButton) findViewById(R.id.fail);
        mRequestCode = CAMERA_REQUEST_CODE;
    }

    @Override
    protected void initToolbar() {
        mCameraId = getIntent().getIntExtra("camId", 0);
    }

    @Override
    protected void onWork() {
        if (!checkCameraHardware(CameraTestActivity.this)) {
            Toast.makeText(CameraTestActivity.this, "相机不支持", Toast.LENGTH_SHORT)
                    .show();
        } else {
            openCamera(mCameraId);
            setCameraDisplayOrientation(CameraTestActivity.this, mCameraId, mCamera);

        }

        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mCamera.autoFocus(null);
                mAutoFocusCallback = new Camera.AutoFocusCallback() {

                    public void onAutoFocus(boolean success, Camera camera) {
                        // TODO Auto-generated method stub
                        if(success){
                            //mCamera.setOneShotPreviewCallback(null);

                        }
                    }
                };
                mCamera.autoFocus(mAutoFocusCallback);
                mCamera.takePicture(shutterCallback, null, jpegCallback);
                mCapture.setVisibility(View.GONE);
                mPass.setVisibility(View.VISIBLE);
                mFail.setVisibility(View.VISIBLE);
            }
        });

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

    public void onPictureTakenThis(byte[] data) {
        File images=new File("/sdcard");
        FileOutputStream out=null;
        if(!images.exists())
            images.mkdirs();
        String filename =  "asus_diagnostic_btn_take_photo.png";
        File image=new File(images,filename);
        try {
            out=new FileOutputStream(image);
            out.write(data);
            out.flush();
            out.close();

            FileInputStream f = new FileInputStream(image);
            Bitmap bm = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;//图片的长宽都是原来的1/8
            BufferedInputStream bis = new BufferedInputStream(f);
            bm = BitmapFactory.decodeStream(bis, null, options);
            mCameralayout.setVisibility(View.GONE);
            mPng.setVisibility(View.VISIBLE);
            mPng.setImageBitmap(rotateBitmapByDegree(bm, 90));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "保存照片失败！", Toast.LENGTH_LONG).show();
        }
        finally
        {
            // camera.startPreview();

        }
    }

    public static Bitmap recyleBitmap(Bitmap bm) {
        if(!bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
        return null;
    }
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }

        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG,"jpegCallback");
            // TODO Auto-generated method stub
            Camera.Parameters ps = camera.getParameters();
            if(ps.getPictureFormat() == PixelFormat.JPEG){
                //存储拍照获得的图片
                onPictureTakenThis(data);

            }
        }
    };

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback(){

        @Override
        public void onShutter() {
            Log.i(TAG,"shutterCallback");
            // TODO Auto-generated method stub
            if(tone == null)
                //发出提示用户的声音
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC,ToneGenerator.MAX_VOLUME);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP2);
        }
    };



    public void releaseCamera() {
        Log.v(TAG, "start releaseCamera");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
        Log.v(TAG, "onPause finish activity");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 获取相机实例
    public android.hardware.Camera getCameraInstance(int id) {
        try {
            Log.v(TAG, "getCameraInstance");
            Method openMethod = Class.forName("android.hardware.Camera").getMethod(
                    "openLegacy", int.class, int.class);
            mCamera = (android.hardware.Camera) openMethod.invoke(
                    null, id, CAMERA_HAL_API_VERSION_1_0);
            Log.v(TAG, "getCameraInstance");
        } catch (Exception e) {
			/* Retry with open if openLegacy doesn't exist/fails */
            Log.v(TAG, "openLegacy failed due to " + e.getMessage()
                    + ", using open instead");

            mCamera = android.hardware.Camera.open(id);

        }
        return mCamera;
    }

    // 开始预览相机
    public void openCamera(int id) {
        Log.v(TAG, "openCamera id = " + id);
        if (mCamera == null) {
            mCamera = getCameraInstance(id);

            //add by gaoweili start
            Camera.Parameters p = mCamera.getParameters();
            List<Camera.Size> li = p.getSupportedPictureSizes();
            for (int i = 0; i < li.size() ; i++){
                tag = (li.get(i).width ) * (li.get(i).height);
                Log.v(TAG, "li.get(i).width =" + li.get(i).width + "li.get(i).height =" + li.get(i).height);
                if(tag > tagResult){
                    tagResult = tag;
                    cWidth = li.get(i).width;
                    cHeight = li.get(i).height;
                }
            }
            List<Camera.Size> lp = p.getSupportedPreviewSizes();
            for (int i = 0; i < lp.size() ; i++){
                Log.v(TAG, "cwWidth =" + lp.get(i).width + "cwHeight =" + lp.get(i).height);
            }
            Log.v(TAG, "cWidth =" + cWidth + "cHeight =" + cHeight);
            p.setPictureSize(cWidth, cHeight);
            p.setPreviewSize(1920,1080);
            p.set("zsl","on");
            //频闪问题，设置为50HZ
           // p.set("zsl","Parameters.ANTIBANDING_50HZ = " + Camera.Parameters.ANTIBANDING_50HZ);
            //p.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mCamera.setParameters(p);
            //add by gaoweili end
            mPreview = new CameraPreview(CameraTestActivity.this, mCamera);
            mCameralayout = (FrameLayout) findViewById(R.id.camera_preview);
            mCameralayout.addView(mPreview);
            mCamera.startPreview();
        }
    }

    public static void setCameraDisplayOrientation (BaseActivity activity, int cameraId, android.hardware.Camera camera) {
        Log.v(TAG, "start setCameraDisplayOrientation" );
        int result = 90;
        camera.setDisplayOrientation(result);

    }

    @Override
    protected void sendResult() {
        Intent intent = new Intent(this, SingleTestActivity.class);
        intent.putExtra("result", mResult);
        if (mCameraId == 0){
            setResult(CAMERA_REQUEST_CODE, intent);
        }else if(mCameraId == 1){
            setResult(VGACAMERA_REQUEST_CODE, intent);
        }
        finish();
    }
}
