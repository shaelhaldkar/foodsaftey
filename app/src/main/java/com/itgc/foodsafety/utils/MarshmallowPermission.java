package com.itgc.foodsafety.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MarshmallowPermission {

    Activity activity;
    public static final int STORAGE_PERMISSION_CODE = 23;
    public static final int Call_PERMISSION_CODE = 12;

    public MarshmallowPermission(Activity activity) {
        this.activity = activity;
    }


    public boolean isPermissionAllowed() {

        int readexternal = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeexternal = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int accessfinelocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int internet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int coarselocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int finelocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);


        if (!(readexternal == PackageManager.PERMISSION_GRANTED))
            return false;
        else if (!(writeexternal == PackageManager.PERMISSION_GRANTED))
            return false;
        else if (!(camera == PackageManager.PERMISSION_GRANTED))
            return false;
        else if (!(accessfinelocation == PackageManager.PERMISSION_GRANTED))
            return false;
        else if (!(internet == PackageManager.PERMISSION_GRANTED))
            return false;
        else if (!(coarselocation == PackageManager.PERMISSION_GRANTED))
            return false;
        else if (!(finelocation == PackageManager.PERMISSION_GRANTED))
            return false;

        return true;
    }

//    //Requesting permission
    public void requestPermission() {

//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

    }

    public boolean isCamera() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    public void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Call_PERMISSION_CODE);
    }


    //Requesting permission
    public void requestCallPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, Call_PERMISSION_CODE);
    }

}