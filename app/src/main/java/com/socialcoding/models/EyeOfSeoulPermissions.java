package com.socialcoding.models;

import android.Manifest;
import android.content.pm.PackageManager;

/**
 * Created by yoon on 2016. 10. 26..
 */
public class EyeOfSeoulPermissions {
    public static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final int DENIED = PackageManager.PERMISSION_DENIED;

    public static final int PERMISSIONS_REQUEST_LOCATION = 1;
    public static final String LOCATION_PERMISSION_STRING = Manifest.permission.ACCESS_FINE_LOCATION;

    public static final String CAMERA_PERMISSION_STRING = Manifest.permission.CAMERA;
}
