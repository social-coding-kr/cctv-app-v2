package com.socialcoding.handlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.socialcoding.cctv.R;
import com.socialcoding.vars.EyeOfSeoulPermissions;

/**
 * Created by yoon on 2016. 10. 26..
 */
public class PermissionHandler {
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
        // Toast or dialog.

        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.redialog_agreement_layout, null);

        final AlertDialog alertDialogPermission = new AlertDialog.Builder(activity).create();

        Button settingBtn = (Button) alertLayout.findViewById(R.id.button_setting);
        Button closeBtn = (Button) alertLayout.findViewById(R.id.button_close);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
                    activity.startActivity(intent);
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialogPermission.dismiss();
            }
        });

        alertDialogPermission.setView(alertLayout);
        alertDialogPermission.show();

//        new AlertDialog.Builder(activity)
//                .setMessage(R.string.permission_reason_explaination)
//                .setCancelable(false)

//                .setNegativeButton(R.string.permission_btn_close, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).setPositiveButton(R.string.permission_btn_setting, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        try {
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                    .setData(Uri.parse("package:" + activity.getPackageName()));
//                            activity.startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            e.printStackTrace();
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
//                            activity.startActivity(intent);
//                        }
//                    }
//                }).show();

    }

    public void askPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionsRequest);
    }
}
