package com.socialcoding.handler;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.socialcoding.models.EyeOfSeoulPermissions;

/**
 * Created by yoon on 2016. 10. 26..
 */
public class PermissionHandler extends Activity {
    private Activity activity;
    private String permission;
    private int permissionsRequest;

    public int handle(Activity activity, String permission, int permissionsRequest) {
        this.activity = activity;
        this.permission = permission;
        this.permissionsRequest = permissionsRequest;
        if (isGranted()) {
            return EyeOfSeoulPermissions.GRANTED;
        } else {
            if(needExplanation()) {
                explain();
            } else {
                askPermission();
            }
        }
        return EyeOfSeoulPermissions.DENIED;
    }

    public int handle(Activity activity, String permission) {
        this.activity = activity;
        this.permission = permission;
        this.permissionsRequest = 0;
        if (isGranted()) {
            return EyeOfSeoulPermissions.GRANTED;
        } else {
            if(needExplanation()) {
                explain();
            } else {
                askPermission();
            }
        }
        return EyeOfSeoulPermissions.DENIED;
    }

    public boolean isGranted() {
        return (ContextCompat.checkSelfPermission(activity, permission) == EyeOfSeoulPermissions.GRANTED);
    }

    public boolean needExplanation() {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public void explain() {

    }

    public void askPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionsRequest);
    }
}
