package com.wingtech.diagnostic.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.wingtech.diagnostic.util.Log;

import java.util.Iterator;

/**
 * Created by xiekui on 17-8-2.
 */

public class GpsTestingFragment extends TestFragment {

    private LocationManager mLCationMgr = null;
    private int mSatelliteNum = 0;
    private static final String TAG = "GpsTestingFragment";
    private int num = 0;
    /**
     * 测试时间
     */
    private int totaltesttime = 10000;
    private CountDownTimer mtimer;

    private boolean isStoped = true;

    @Override
    protected void onWork() {
        super.onWork();
        mLCationMgr = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (mLCationMgr != null) {
            mLCationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, new LocationListener(){


                        @Override
                        public void onLocationChanged(Location location) {

                        }


                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }


                        @Override
                        public void onProviderEnabled(String provider) {
                            if (isGpsEnabled()) {
                                startCountDownTimer();
                            }
                        }


                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLCationMgr.addGpsStatusListener((new GpsStatus.Listener(){
                @Override
                public void onGpsStatusChanged(int event) {
                    switch(event){
                        case GpsStatus.GPS_EVENT_STARTED:
                            Log.d(TAG, "GPS_EVENT_STARTED");
                            break;
                        case GpsStatus.GPS_EVENT_STOPPED:
                            Log.d(TAG, "GPS_EVENT_STOPPED");
                            break;
                        case GpsStatus.GPS_EVENT_FIRST_FIX:
                            Log.d(TAG, "GPS_EVENT_FIRST_FIX");
                            break;
                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            updateSatellites();
                            break;
                        default:
                            Log.e(TAG, "onGpsStatusChanged: unknown event:" + event);
                            break;
                    }
                }
            }));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mLCationMgr != null) {
            mLCationMgr.removeGpsStatusListener((new GpsStatus.Listener(){
                @Override
                public void onGpsStatusChanged(int event) {

                }
            }));
            mLCationMgr.removeUpdates(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        mLCationMgr = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isGpsEnabled()) {
            Log.i(TAG, "isGpsEnabled");
        }else{
            Log.i(TAG, "GPS NOT OPEN");
            Toast.makeText(mActivity,"GPS is not open. Please open it",Toast.LENGTH_SHORT).show();
        }
        startCountDownTimer();
    }


    private boolean isGpsEnabled() {
        if (null == mLCationMgr) {
            return false;
        }

        return mLCationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void startCountDownTimer() {
        Log.d(TAG, "startCountDownTimer()");
        if (mtimer != null) {
            mtimer.cancel();
        }
        isStoped = false;
        mtimer = new CountDownTimer(totaltesttime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                refreshTestResult();
            }

            @Override
            public void onFinish() {
                timerFinished();
            }


        }.start();
    }

    private void timerFinished() {
        closeTest();
        mCallback.onChange(false);
    }

    /**
     * 结束当前测试
     */
    private synchronized void closeTest() {
        if (null != mtimer) {
            mtimer.cancel();
            mtimer = null;
        }
        if (isStoped) {
            return;
        }
        isStoped = true;

    }

    private void refreshTestResult() {
        Log.i(TAG, "num = " + num);
        if (num == 0) {
        } else {
            closeTest();
            mCallback.onChange(true);
        }
    }

    private void updateSatellites() {
        if (null == mLCationMgr) {
            return;
        }
        Log.d(TAG, "updateSatellites num = " + num);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        GpsStatus status = mLCationMgr.getGpsStatus(null);
        if(null == status){
            return;
        }

        Iterable<GpsSatellite> list = status.getSatellites();
        Iterator<GpsSatellite> node = list.iterator();

        while(node.hasNext()){

            GpsSatellite gpsSatellite = node.next();
            num++;
            Log.d(TAG, "num = " + num);
            int prn = gpsSatellite.getPrn();
            float snr = gpsSatellite.getSnr();
            float elevation = gpsSatellite.getElevation();
            float azimuth = gpsSatellite.getAzimuth();
            Log.d(TAG, "prn:" + prn + " snr:" + snr +
                    " elevation:" + elevation + " azimuth:" +azimuth);

        }

    }



}
