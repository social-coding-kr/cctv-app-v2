package com.socialcoding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;

/**
 * Created by yoon on 2016. 10. 22..
 */
public class RelatedLawFragment extends Fragment {
  private ProgressBar mSpinner; // 로딩

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_related_law, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (!(getActivity() instanceof MainActivity)) {
      throw new RuntimeException("Invalid activity.");
    }
    MainActivity mainActivity = (MainActivity) getActivity();

    mSpinner = (ProgressBar) mainActivity.findViewById(R.id.web_progress);
    mSpinner.setVisibility(View.GONE); // 우선 화면에서 숨기

    TextView titleTextView = (TextView) mainActivity.findViewById(R.id.title_text_view);
    titleTextView.setText(getResources().getString(R.string.related_law_string));

    WebView mWebView = mainActivity.relatedLawView; // 미리 준비해놓은 뷰 불러들이기

    mWebView.setWebChromeClient(new WebChromeClient(){
      public void onProgressChanged(WebView view, int newProgress){
        mSpinner.setVisibility(View.VISIBLE); // 화면에 보이기

        if(newProgress == 100){
          mSpinner.setVisibility(View.GONE); //진행상황 100이면 숨김
        }
        mSpinner.setProgress(newProgress);
      }
    });

    if (mWebView.getParent() == null) {
      ((RelativeLayout) view).addView(mWebView);
    } else { // TODO(clsan) : 이미 부모와 연결되어 있음에도 불구하고 아래 라인을 주석처리하면 아무 것도 보이지 않음 <- 좀 이상함
      ((RelativeLayout) mWebView.getParent()).removeView(mWebView);
      ((RelativeLayout) view).addView(mWebView);
    }
  }
}
