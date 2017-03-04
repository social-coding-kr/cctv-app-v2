package com.socialcoding.cctv;

import android.content.Context;
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

import com.socialcoding.handler.Handler;
import com.socialcoding.models.EyeOfSeoulParams;
import com.socialcoding.models.EyeOfSeoulPermissions;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by darkgs on 2016-03-26.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri imageCaptureUri;
    private String imageUsage;
    private File cctvFile;
    private File infoFile;

    private ImageView cctvImageView;
    private ImageView infoImageView;
    private ImageView infoImageNullView;
    private ImageView infoImageNullIcon;

    private TextView cctvImageTextView;
    private TextView cctvInfoTextView;

    private Button[] buttons;
    private ImageView[] imageViews;
    private TextView[] textViews;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    public void initViews() {
        Button[] btns = new Button[]{
                (Button) mainActivity.findViewById(R.id.report_complete_btn),
                (Button) mainActivity.findViewById(R.id.report_cancle_btn)
        };
        ImageView[] ivs = {
                cctvImageView = (ImageView) mainActivity.findViewById(R.id.cctv_image),
                infoImageView = (ImageView) mainActivity.findViewById(R.id.info_image),
                infoImageNullView = (ImageView) mainActivity.findViewById(R.id.info_image_null),
                infoImageNullIcon = (ImageView) mainActivity.findViewById(R.id.info_image_null_icon)
        };
        TextView[] tvs = new TextView[] {
                (TextView) mainActivity.findViewById(R.id.report_cctv_image_text_view),
                cctvImageTextView = (TextView) mainActivity.findViewById(R.id.cctv_image_text_view),
                (TextView) mainActivity.findViewById(R.id.report_info_image_text_view),
                cctvInfoTextView = (TextView) mainActivity.findViewById(R.id.cctv_info_text_view),
                (TextView) mainActivity.findViewById(R.id.info_image_null_text_view),
                (TextView) mainActivity.findViewById(R.id.bottom_bar_report_address_title_text_view),
                (TextView) mainActivity.findViewById(R.id.bottom_bar_report_address_text_view)
        };

        buttons = btns;
        textViews = tvs;
        imageViews = ivs;
    }

    public void initButtons() {
        for(ImageView imageView : imageViews) {
            imageView.setOnClickListener(this);
        }
        for(Button b : buttons) {
            b.setOnClickListener(this);
        }
    }

    public void initFonts() {
        TextView titleTextView = (TextView) mainActivity.findViewById(R.id.title_text_view);
        titleTextView.setText(mainActivity.getResources().getString(R.string.report_string));
        TextView addressText = (TextView) getActivity()
                .findViewById(R.id.bottom_bar_report_address_text_view);
        addressText.setText(MainActivity.address);
    }

    private void initComponents() {
        initViews();
        initButtons();
        initFonts();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!(getActivity() instanceof MainActivity)) {
            throw new RuntimeException("Invalid activity.");
        }
        mainActivity = (MainActivity) getActivity();

        Handler.permissionHandler.handle(mainActivity,
                EyeOfSeoulPermissions.CAMERA_PERMISSION_STRING);
        initComponents();
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
            cctvImageTextView.setVisibility(View.INVISIBLE);

            return cctvFile;
        } else if ("info".equals(imageUsage)) {
            if(extras != null) {
                Bitmap photo = extras.getParcelable("data");
                infoImageView.setImageBitmap(photo);
            }
            infoFile = new File(imageCaptureUri.getPath());
            cctvInfoTextView.setVisibility(View.INVISIBLE);
            if(infoImageNullIcon.getVisibility() == View.VISIBLE) {
                infoImageNullIcon.setVisibility(View.INVISIBLE);
            }

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
        mainActivity.onNavigationItemSelected(mainActivity.navigationView.getMenu().getItem(0));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.cctv_image:
                imageUsage = "cctv";
                mainActivity.showSubFragment(mainActivity.photoPickerDialogFragment
                        , EyeOfSeoulParams.ReportTag);
                break;

            case R.id.info_image:
                imageUsage = "info";
                mainActivity.showSubFragment(mainActivity.photoPickerDialogFragment
                        , EyeOfSeoulParams.ReportTag);
                break;

            case R.id.info_image_null:
                if(infoImageNullIcon.getVisibility() == View.VISIBLE) {
                    infoImageNullIcon.setVisibility(View.INVISIBLE);
                } else {
                    infoImageNullIcon.setVisibility(View.VISIBLE);
                }

                if(infoFile != null) {
                    cctvInfoTextView.setVisibility(View.VISIBLE);
                    infoImageView.setImageBitmap(null);
                    infoFile = null;
                }
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

    public void onMethodSelected(String selector) {
        mainActivity.hideSubFragment(mainActivity.photoPickerDialogFragment);
        if("album".equals(selector)) {
            doTakeAlbumAction();
        } else if ("camera".equals(selector)) {
            doTakePhotoAction();
        }
    }
}
