package com.socialcoding.cctv;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yoon on 2016. 11. 23..
 */

public class PhotoPickerDialogFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private TextView[] textViews;
    private Button[] buttons;
    private ImageView[] imageViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmnet_photo_picker_dialog, container, false);
    }

    private void initFonts() {
        Typeface naumBarunGothic = MainActivity.naumBarunGothic;

        for(TextView textView : textViews) {
            textView.setTypeface(naumBarunGothic);
        }
        for(Button button : buttons) {
            button.setTypeface(naumBarunGothic);
        }
    }

    private void initButtons() {
        for(Button button : buttons) {
            button.setOnClickListener(this);
        }
        for(ImageView imageView : imageViews) {
            imageView.setOnClickListener(this);
        }

    }

    private void initComponents() {
        TextView[] tvs = {
                (TextView) mainActivity.findViewById(R.id.photo_picker_dialog_title_text_view)
        };
        Button[] btns = {
                (Button) mainActivity.findViewById(R.id.photo_picker_dialog_camera_btn),
                (Button) mainActivity.findViewById(R.id.photo_picker_dialog_album_btn)
        };
        ImageView[] ivs = {
                (ImageView) mainActivity.findViewById(R.id.photo_picker_dialog_cancle_image_view)
        };

        textViews = tvs;
        buttons = btns;
        imageViews = ivs;

        initFonts();
        initButtons();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!(getActivity() instanceof MainActivity)) {
            throw new RuntimeException("Invalid activity.");
        }
        mainActivity = (MainActivity) getActivity();
        initComponents();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_picker_dialog_cancle_image_view:
                break;

            case R.id.photo_picker_dialog_album_btn:
                ((ReportFragment) mainActivity.reportFragment).onMethodSelected("album");
                break;

            case R.id.photo_picker_dialog_camera_btn:
                ((ReportFragment) mainActivity.reportFragment).onMethodSelected("camera");
                break;
        }

    }
}
