package com.socialcoding.cctv;

import android.graphics.Typeface;
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
        initComponents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        boolean agreement;

        switch(v.getId()) {
            case R.id.agreement_dialog_agree_btn:
                getActivity().findViewById(R.id.bottom_bar_google_map_loading_text_view)
                        .setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.bottom_bar_google_map_ask_layout)
                        .setVisibility(View.INVISIBLE);
                agreement = true;
                break;

            case R.id.agreement_dialog_disagree_btn:
                agreement = false;
                break;

            default:
                return;
        }

        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onAgreementClicked(agreement);
        }
    }

    private void initComponents() {
        if (!(getActivity() instanceof MainActivity)) {
            throw new RuntimeException("Invalid activity.");
        }
        Button[] agreementButtons;
        TextView[] agreementTextViews;

        MainActivity mainActivity = (MainActivity) getActivity();
        Typeface naumBarunGothic = MainActivity.naumBarunGothic;

        agreementButtons = new Button[]{
                (Button) mainActivity.findViewById(R.id.agreement_dialog_agree_btn),
                (Button) mainActivity.findViewById(R.id.agreement_dialog_disagree_btn)
        };
        agreementTextViews = new TextView[] {
                (TextView) mainActivity.findViewById(R.id.agreement_dialog_title_text_view),
                (TextView) mainActivity.findViewById(R.id.agreement_dialog_reason_text_view),
                (TextView) mainActivity.findViewById(R.id.agreement_dialog_ask_text_view)
        };

        for(Button b : agreementButtons) {
            b.setOnClickListener(this);
            b.setTypeface(naumBarunGothic);
        }
        for(TextView tv : agreementTextViews) {
            tv.setOnClickListener(this);
            tv.setTypeface(naumBarunGothic);
        }
    }

}
