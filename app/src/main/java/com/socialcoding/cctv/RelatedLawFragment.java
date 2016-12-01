package com.socialcoding.cctv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.socialcoding.models.Law;
import com.socialcoding.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yoon on 2016. 10. 22..
 */
public class RelatedLawFragment extends Fragment {

    private MainActivity mainActivity;

    private WebView mWebView; // 웹뷰
    private WebSettings mWebSettings; // 웹뷰 세팅

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

        TextView titleTextView = (TextView) mainActivity.findViewById(R.id.title_text_view);
        titleTextView.setText(getResources().getString(R.string.related_law_string));

        mWebView = (WebView) mainActivity.findViewById(R.id.webview); // 레이어와 연결
        //mWebView.setWebViewClient(new WebViewClient()); // 클릭 시 새창 안뜨게
        mWebSettings = mWebView.getSettings(); // 세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 자바스크립트 사용 허용

        //원하는 url 입력 -> 나중에 사용자가 입력하는걸로 받으면 되겠다
        mWebView.loadUrl("file:///android_asset/tutorial_test.html");
    }
}
