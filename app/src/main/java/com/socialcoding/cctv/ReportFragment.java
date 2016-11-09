package com.socialcoding.cctv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by darkgs on 2016-03-26.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri imageCaptureUri;

    private ImageView cctvImageView;
    private ImageView infoImageView;

    private static String imageUsage;
    private static File cctvFile;
    private static File infoFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getActivity().getResources().getString(R.string.report_string));
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setButtons();
        setAddress();
    }

    private void setButtons() {
        Button reportCompleteButton;
        Button reportCancleButton;

        cctvImageView = (ImageView) getActivity().findViewById(R.id.cctv_image);
        infoImageView = (ImageView) getActivity().findViewById(R.id.info_image);
        reportCompleteButton = (Button) getActivity().findViewById(R.id.report_complete_btn);
        reportCancleButton = (Button) getActivity().findViewById(R.id.report_cancle_btn);

        cctvImageView.setOnClickListener(this);
        infoImageView.setOnClickListener(this);
        reportCompleteButton.setOnClickListener(this);
        reportCancleButton.setOnClickListener(this);
    }

    private void setAddress() {
        TextView addressText = (TextView) getActivity().findViewById(R.id.address_text);
        addressText.setText(MainActivity.address);
    }

    private void showDialog(String usage) {
        imageUsage = usage;
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakePhotoAction();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakeAlbumAction();
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(getActivity())
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    private void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        imageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void pickPicture(int requestCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM) {
            imageCaptureUri = data.getData();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageCaptureUri, "image/*");

        intent.putExtra("outputX", 90);
        intent.putExtra("outputY", 90);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    private File getPicture(Intent data) {
        final Bundle extras = data.getExtras();

        if ("cctv".equals(imageUsage)) {
            if(extras != null) {
                Bitmap photo = extras.getParcelable("data");
                cctvImageView.setImageBitmap(photo);
            }
            cctvFile = new File(imageCaptureUri.getPath());

            return cctvFile;
        } else if ("info".equals(imageUsage)) {
            if(extras != null) {
                Bitmap photo = extras.getParcelable("data");
                infoImageView.setImageBitmap(photo);
            }
            infoFile = new File(imageCaptureUri.getPath());

            return infoFile;
        }

        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case CROP_FROM_CAMERA:
                getPicture(data);
                break;

            default:
                pickPicture(requestCode, data);
                break;
        }
    }

    private void flush() {
        imageUsage = null;
        cctvFile = null;
        infoFile = null;
    }

    private void showGoogleMap() {
        flush();

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.onNavigationItemSelected(mainActivity.navigationView.getMenu().getItem(0));
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.cctv_image:
                showDialog("cctv");
                break;

            case R.id.info_image:
                showDialog("info");
                break;

            case R.id.report_complete_btn:
                // Send image to server
                showGoogleMap();
                break;

            case R.id.report_cancle_btn:
                showGoogleMap();
                break;
        }
    }
}
