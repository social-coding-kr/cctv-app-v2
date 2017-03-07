package com.socialcoding.dialogFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;

import java.util.List;

/**
 * Created by yoon on 2016. 10. 31..
 */
public class AgreementDialogFragment extends DialogFragment {
    private Unbinder unbinder;

    @BindViews({R.id.button_agree_fda, R.id.button_disagree_fda})
    List<Button> agreementButtons;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_agreement, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.button_agree_fda, R.id.button_disagree_fda})
    public void onAgreementButtonsClick(View view) {
        boolean isAgreed = view.getId() == R.id.button_agree_fda;
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onAgreementClick(isAgreed);
        }
        dismiss();
    }
}
