package com.picksplug.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.helpers.AITextViewDesc;
import com.picksplug.helpers.PickPlugApp;

public class FragAbout extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private TextView                            lblAbout;
    private AITextViewDesc                      txtAbout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_about, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                =   getActivity();
            mAlreadyLoaded          =   true;
            initViews();
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(getResources().getString(R.string.nav_about));
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }
    }

    private void initViews() {
        lblAbout       =   (TextView) rootView.findViewById(R.id.lbl_about);
        txtAbout       =   (AITextViewDesc) rootView.findViewById(R.id.txt_about);

        setFonts();
    }

    private void setFonts(){
        lblAbout.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtAbout.setTypeface(PickPlugApp.getInstance().getRegularFont());
    }

}
