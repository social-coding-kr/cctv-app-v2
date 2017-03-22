package com.socialcoding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;

/**
 * Created by yoon on 2016. 10. 22..
 */
public class RelatedLawFragment extends Fragment {

    private MainActivity mainActivity;

    private WebView mWebView; // 웹뷰
    private WebSettings mWebSettings; // 웹뷰 세팅
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
        mainActivity = (MainActivity) getActivity();

        mSpinner = (ProgressBar) mainActivity.findViewById(R.id.web_progress);
        mSpinner.setVisibility(view.GONE); // 우선 화면에서 숨기

        TextView titleTextView = (TextView) mainActivity.findViewById(R.id.title_text_view);
        titleTextView.setText(getResources().getString(R.string.related_law_string));

        mWebView = (WebView) mainActivity.findViewById(R.id.webview); // 레이어와 연결
        mWebView.setWebViewClient(new WebViewClient()); // 클릭 시 새창 안뜨게

        mWebSettings = mWebView.getSettings(); // 세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 자바스크립트 사용 허용
        mWebView.loadUrl("file:///android_asset/lawPage.html");

        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress){
                mSpinner.setVisibility(View.VISIBLE); // 화면에 보이기

                if(newProgress == 100){
                    mSpinner.setVisibility(View.GONE); //진행상황 100이면 숨김
                }
                mSpinner.setProgress(newProgress);
            }
        });

    }

}
