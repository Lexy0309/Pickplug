package com.picksplug.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.HomeTeamDetailsModel;
import com.picksplug.model.PickDetailModel;
import com.picksplug.model.PicksDetailModel;
import com.picksplug.model.VisitingTeamDetailsModel;


public class FragPickAnalysis extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private RelativeLayout                      layoutPicks;
    private TextView                            txtPickAnalysis,lblAnalysis,txtAnalysis,txtPickDateTime;
    private TextView                            txtHomeTeam,txtVisitingTeam;
    private ImageView                           imgHomeTeam,imgVisitingTeam,imgSport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_pick_analysis, container, false);
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

            if (getArguments() != null){
                setValues(getArguments().getString("from"));
            }
        }
    }

    private void initViews(){
        txtPickAnalysis     =   (TextView)rootView.findViewById(R.id.txt_pick_analysis);
        lblAnalysis         =   (TextView)rootView.findViewById(R.id.lbl_analysis);
        txtAnalysis         =   (TextView)rootView.findViewById(R.id.txt_analysis);
        txtPickDateTime     =   (TextView)rootView.findViewById(R.id.txt_free_pick_date_time);
        layoutPicks         =   (RelativeLayout)rootView.findViewById(R.id.layout_picks);
        txtHomeTeam         =   (TextView)layoutPicks.findViewById(R.id.list_picks_txt_home_team);
        txtVisitingTeam     =   (TextView)layoutPicks.findViewById(R.id.list_picks_txt_visiting_team);
        imgHomeTeam         =   (ImageView) layoutPicks.findViewById(R.id.list_picks_img_home_team);
        imgVisitingTeam     =   (ImageView) layoutPicks.findViewById(R.id.list_picks_img_visiting_team);
        imgSport            =   (ImageView) layoutPicks.findViewById(R.id.list_picks_img_sport);

        setFonts();
    }

    private void setFonts(){
        txtHomeTeam.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtVisitingTeam.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtPickAnalysis.setTypeface(PickPlugApp.getInstance().getMediumFont());
        lblAnalysis.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtAnalysis.setTypeface(PickPlugApp.getInstance().getRegularFont());
        txtPickDateTime.setTypeface(PickPlugApp.getInstance().getRegularFont());
    }

    private void setValues(String from){
        if (from.equals("records"))
            setValuesOfRecords();
         else
            setValuesOfPicks(from);
    }

    private void setValuesOfRecords(){
        PickDetailModel pickDetailModel = (PickDetailModel)getArguments().getSerializable("detail_model");
        String SportImage = getArguments().getString("SportImage");
        HomeTeamDetailsModel homeTeamDetailsModel = pickDetailModel.getHomeTeamDetails();
        VisitingTeamDetailsModel visitingTeamDetailsModel = pickDetailModel.getVisitingTeamDetails();

        txtPickDateTime.setText(CommonUtils.getFormattedDate(pickDetailModel.getPickDate()+ " " +pickDetailModel.getPickTime(),"dd-MM-yyyy HH:mm:ss","EEEE MMM d - KK:mm a"));
        txtPickAnalysis.setText(pickDetailModel.getPickRecord());
        txtHomeTeam.setText(homeTeamDetailsModel.getTeamName());
        txtVisitingTeam.setText(visitingTeamDetailsModel.getTeamName());
        CommonUtils.loadImageWithGlide(imgHomeTeam,homeTeamDetailsModel.getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(imgVisitingTeam,visitingTeamDetailsModel.getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(imgSport,SportImage,mContext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAnalysis.setText(Html.fromHtml(pickDetailModel.getPickAnalysis(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtAnalysis.setText(Html.fromHtml(pickDetailModel.getPickAnalysis()));
        }
    }

    private void setValuesOfPicks(String from){
        PicksDetailModel picksDetailModel = (PicksDetailModel)getArguments().getSerializable("allPicksModel");

        CommonUtils.loadImageWithGlide(imgHomeTeam,picksDetailModel.getHomeTeamDetails().getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(imgVisitingTeam,picksDetailModel.getVisitingTeamDetails().getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(imgSport,picksDetailModel.getSportImage(),mContext);

        if (picksDetailModel.getSportId().equals("1")){
            txtPickDateTime.setText(CommonUtils.getFormattedDate(picksDetailModel.getWeekDate(),"dd-MM-yyyy HH:mm:ss","EEEE MMM d - hh:mm aa"));
        } else {
            txtPickDateTime.setText(CommonUtils.getFormattedDate(picksDetailModel.getPickDate(),"dd-MM-yyyy HH:mm:ss","EEEE MMM d - hh:mm aa"));
        }

        txtHomeTeam.setText(picksDetailModel.getHomeTeamDetails().getTeamName());
        txtVisitingTeam.setText(picksDetailModel.getVisitingTeamDetails().getTeamName());
        txtPickAnalysis.setText(picksDetailModel.getPickRecord());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAnalysis.setText(Html.fromHtml(picksDetailModel.getPickAnalysis(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtAnalysis.setText(Html.fromHtml(picksDetailModel.getPickAnalysis()));
        }
    }

}
