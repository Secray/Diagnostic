package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import com.wingtech.diagnostic.util.Log;

import java.util.List;

/**
 * Created by xiekui on 17-8-2.
 */

public class SDCardTestingFragment extends TestFragment {
    private MountPoint mMountPoint = new MountPoint();
    @Override
    protected void onWork() {
        super.onWork();
        InitStoragePath();
        ExternalStorageState();
    }

    void ExternalStorageState() {
        mResult = mMountPoint.mIsMounted;
        mCallback.onChange(mResult);
    }

    private void InitStoragePath() {
        StorageManager mStorageManager =
                (StorageManager) mActivity.getSystemService(Context.STORAGE_SERVICE);
        // check media availability to init mMountPathList
        List<StorageVolume> storageVolumeList = mStorageManager.getStorageVolumes();
        Log.i("StorageVolume size = " + storageVolumeList.size());
        for (StorageVolume volume : storageVolumeList) {
            Log.i(" " + volume.getDescription(mActivity));
            if (volume.isRemovable()) {
                mMountPoint.mDescription = volume.getDescription(mActivity);
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

    private static class MountPoint {
        String mDescription;
        String mState;
        boolean mIsExternal;
        boolean mIsMounted;
    }
}
