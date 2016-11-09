package com.socialcoding.cctv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yoon on 2016. 10. 31..
 */
public class AgreementDialogFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agreement_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAgreeButtons();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        boolean agreement;

        switch(v.getId()) {
            case R.id.dialog_agree_btn:
                agreement = true;
                break;

            case R.id.dialog_disagree_btn:
                agreement = false;
                break;

            default:
                return;
        }

        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onAgreementClicked(agreement);
        }
    }

    private void setAgreeButtons(){
        Button[] agreementButtons;
        TextView[] agreementTextViews;

        agreementButtons = new Button[]{
                (Button) getActivity().findViewById(R.id.dialog_agree_btn),
                (Button) getActivity().findViewById(R.id.dialog_disagree_btn)
        };
        agreementTextViews = new TextView[] {
                (TextView) getActivity().findViewById(R.id.agreement_dialog_header_text_view),
                (TextView) getActivity().findViewById(R.id.agreement_dialog_text_view)
        };

        for(Button b : agreementButtons) {
            b.setOnClickListener(this);
        }
        for(TextView tv : agreementTextViews) {
            tv.setOnClickListener(this);
        }
    }

}
