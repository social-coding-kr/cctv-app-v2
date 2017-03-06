package com.socialcoding.dialogFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.*;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;
import com.socialcoding.fragments.ReportFragment;

import java.util.List;

/**
 * Created by yoon on 2016. 11. 23..
 */

public class PhotoPickerDialogFragment extends DialogFragment {
    private Unbinder unbinder;

    @BindViews({R.id.button_album_fdpp, R.id.button_camera_fdpp})
    List<Button> photoPickerButtons;

    @BindView(R.id.view_image_cancle_fdpp)
    ImageView photoPickerCancleImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_photo_picker_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.button_album_fdpp, R.id.button_camera_fdpp, R.id.view_image_cancle_fdpp})
    public void onPickingMethodClick(View v) {
        switch (v.getId()) {
            case R.id.view_image_cancle_fdpp:
                break;

            case R.id.button_album_fdpp:
                ((ReportFragment) ((MainActivity) getActivity()).reportFragment).onMethodSelected("album");
                break;

            case R.id.button_camera_fdpp:
                ((ReportFragment) ((MainActivity) getActivity()).reportFragment).onMethodSelected("camera");
                break;
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
