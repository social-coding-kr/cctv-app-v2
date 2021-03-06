package com.socialcoding.handlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

  private boolean isGranted() {
    return (ContextCompat.checkSelfPermission(activity, permission) == EyeOfSeoulPermissions.GRANTED);
  }

  private boolean needExplanation() {
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
  }

  private void explain() {
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
        alertDialogPermission.dismiss();
      }
    });

    closeBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        alertDialogPermission.dismiss();
      }
    });

    alertDialogPermission.setView(alertLayout);
    alertDialogPermission.show();
  }

  private void askPermission() {
    ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionsRequest);
  }
}
