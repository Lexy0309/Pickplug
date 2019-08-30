package com.picksplug.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.helpers.Config;

public class FragLegal extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_legal, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                =   getActivity();
            mAlreadyLoaded          =   true;
            setUpWebView();
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(getResources().getString(R.string.nav_legal));
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(){
        WebView mWebView = (WebView) rootView.findViewById(R.id.webview_legal);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Config.LEGAL_URL);
    }

}
