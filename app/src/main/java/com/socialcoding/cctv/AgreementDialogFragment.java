package com.socialcoding.cctv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by yoon on 2016. 10. 31..
 */
public class AgreementDialogFragment extends Fragment implements View.OnClickListener {

    // Location agreement
    private boolean agreement;
    // Button components
    private Button[] agreementButtons;

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
        switch(v.getId()) {
            case R.id.dialog_agree_btn:
                System.out.println("Agree!");
                agreement = true;
                break;

            case R.id.dialog_disagree_btn:
                System.out.println("Disagree!");
                agreement = false;
                break;
        }
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onAgreementClicked(agreement);
        }
    }

    private void setAgreeButtons(){
        agreementButtons = new Button[]{
                (Button) getActivity().findViewById(R.id.dialog_agree_btn),
                (Button) getActivity().findViewById(R.id.dialog_disagree_btn)
        };
        for(Button b : agreementButtons) {b.setOnClickListener(this);}
    }
}
