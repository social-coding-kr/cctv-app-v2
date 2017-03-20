package com.socialcoding.dialogFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.socialcoding.cctv.R;
import com.socialcoding.fragments.ReportFragment;
import com.socialcoding.vars.EyeOfSeoulParams;
import java.util.List;

/**
 * Created by yoon on 2016. 11. 23..
 */

public class PhotoPickerDialogFragment extends DialogFragment {
    private ReportFragment reportFragment;
    private Unbinder unbinder;

    @BindViews({R.id.button_album_fdpp, R.id.button_camera_fdpp})
    List<Button> photoPickerButtons;

    @BindView(R.id.view_image_cancle_fdpp)
    ImageView photoPickerCancleImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_photo_picker_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        reportFragment = (ReportFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(EyeOfSeoulParams.ReportTag);

        return view;
    }

    @OnClick({R.id.button_album_fdpp, R.id.button_camera_fdpp, R.id.view_image_cancle_fdpp})
    void onPickingMethodClick(View v) {
        if (v.getId() == R.id.button_album_fdpp || v.getId() == R.id.button_camera_fdpp) {
            reportFragment.onMethodSelected(v.getId() == R.id.button_album_fdpp ? "album" : "camera");
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
