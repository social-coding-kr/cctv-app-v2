package com.socialcoding.cctv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.socialcoding.models.Law;
import com.socialcoding.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yoon on 2016. 10. 22..
 */
public class RelatedLawFragment extends Fragment implements View.OnClickListener {
    private List<Law> LawList = new ArrayList<>();

    private View singleLawLayout;
    private TextView singleLawTitleTextView;
    private TextView singleLawContentTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getActivity().getResources().getString(R.string.related_law_string));
        return inflater.inflate(R.layout.fragment_related_law, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            initAfterViewCreated();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAfterViewCreated() throws Exception {
        fetchLawList();
        for(Law law : LawList) {
            findLayoutsByLaw(law);
            initTexts(law);
            initButtonLayouts();
        }
    }

    private void fetchLawList() {
        Collections.addAll(LawList, new Law(R.id.law_layout_1, R.raw.law_title_1, R.raw.law_content_1),
                new Law(R.id.law_layout_2, R.raw.law_title_2, R.raw.law_content_2),
                new Law(R.id.law_layout_3, R.raw.law_title_3, R.raw.law_content_3),
                new Law(R.id.law_layout_4, R.raw.law_title_4, R.raw.law_content_4),
                new Law(R.id.law_layout_5, R.raw.law_title_5, R.raw.law_content_5));
    }

    private void findLayoutsByLaw(Law law) {
        singleLawLayout = getView().findViewById(law.getSingleLawLayoutId()); // after onCreateView. It is safe.
        singleLawTitleTextView = (TextView) singleLawLayout.findViewById(R.id.law_title_text);
        singleLawContentTextView = (TextView) singleLawLayout.findViewById(R.id.law_content_text);
    }

    private void initTexts(Law law) throws Exception {
        singleLawTitleTextView.setText(Utilities.rawTxtToString
                .convert(getResources().openRawResource(law.getSingleLawTitleTxtId())));
        singleLawContentTextView.setText(Utilities.rawTxtToString
                .convert(getResources().openRawResource(law.getSingleLawContentTxtId())));
    }

    private void initButtonLayouts() {
        singleLawLayout.setClickable(false);
        singleLawTitleTextView.setOnClickListener(this);
        singleLawContentTextView.setLinksClickable(false);
    }

    @Override
    public void onClick(View v) {
        v = (View) v.getParent();
        if(v.findViewById(R.id.law_content_text).getVisibility() == View.GONE) {
            v.findViewById(R.id.law_content_text).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.law_content_text).setVisibility(View.GONE);
        }
    }
}
