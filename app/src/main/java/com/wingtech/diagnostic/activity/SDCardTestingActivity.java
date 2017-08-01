package com.wingtech.diagnostic.activity;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import com.wingtech.diagnostic.util.Log;

import java.util.List;

import static com.wingtech.diagnostic.util.Constants.SDCARD_REQUEST_CODE;

/**
 * @author xiekui
 * @date 2017-7-31
 */

public class SDCardTestingActivity extends TestingActivity {
    private MountPoint mMountPoint = new MountPoint();

    @Override
    protected void onWork() {
        super.onWork();
        mRequestCode = SDCARD_REQUEST_CODE;
        InitStoragePath();
    }

    private static class MountPoint {
        String mDescription;
        String mState;
        boolean mIsExternal;
        boolean mIsMounted;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExternalStorageState();
    }

    void ExternalStorageState() {
        mResult = mMountPoint.mIsMounted;
        sendResult();
    }

    private void InitStoragePath() {
        StorageManager mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        // check media availability to init mMountPathList
        List<StorageVolume> storageVolumeList = mStorageManager.getStorageVolumes();
        Log.i("StorageVolume size = " + storageVolumeList.size());
        for (StorageVolume volume : storageVolumeList) {
            Log.i(" " + volume.getDescription(this));
            if (volume.isRemovable()) {
                mMountPoint.mDescription = volume.getDescription(this);
                String state = volume.getState();
                Log.d("state = " + state);
                mMountPoint.mIsMounted = Environment.MEDIA_MOUNTED.equals(state);
                mMountPoint.mIsExternal = volume.isRemovable();
                Log.d("init,description :" + mMountPoint.mDescription + ",state : "
                        + mMountPoint.mState + ",isMounted : " + mMountPoint.mIsMounted
                        + ",isExternal : " + mMountPoint.mIsExternal);
                return;
            }
        }
    }
}
