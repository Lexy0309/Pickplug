package com.picksplug.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.adapter.SubscriptionListAdapter;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.InappPurchaseModel;
import com.picksplug.model.PicksDetailModel;
import com.picksplug.model.SubscriptionModel;

import java.util.ArrayList;


public class FragPickSubscription extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private RelativeLayout                      layoutPicks;
    private TextView                            txtPickDateTime,txtPickOption;
    private TextView                            txtHomeTeam,txtVisitingTeam;
    private ImageView                           imgHomeTeam,imgVisitingTeam,imgSport;
    private RecyclerView                        recyclerViewSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_pick_subscription, container, false);
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
                setValues();
            }
        }
    }

    private void initViews(){
        txtPickDateTime             =   (TextView)rootView.findViewById(R.id.txt_sport_pick_date);
        txtPickOption               =   (TextView)rootView.findViewById(R.id.txt_pick_option);
        recyclerViewSubscription    =   (RecyclerView) rootView.findViewById(R.id.recycle_view_pick_subscriptions);
        layoutPicks                 =   (RelativeLayout)rootView.findViewById(R.id.layout_picks);
        txtHomeTeam                 =   (TextView)layoutPicks.findViewById(R.id.list_picks_txt_home_team);
        txtVisitingTeam             =   (TextView)layoutPicks.findViewById(R.id.list_picks_txt_visiting_team);
        imgHomeTeam                 =   (ImageView) layoutPicks.findViewById(R.id.list_picks_img_home_team);
        imgVisitingTeam             =   (ImageView) layoutPicks.findViewById(R.id.list_picks_img_visiting_team);
        imgSport                    =   (ImageView) layoutPicks.findViewById(R.id.list_picks_img_sport);

        setFonts();
    }

    private void setFonts(){
        txtHomeTeam.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtVisitingTeam.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtPickDateTime.setTypeface(PickPlugApp.getInstance().getRegularFont());
        txtPickOption.setTypeface(PickPlugApp.getInstance().getRegularFont());
    }

    private void setUpRecycleView(ArrayList<InappPurchaseModel> inappPurchaseModelArrayList){

        SubscriptionListAdapter adapter = new SubscriptionListAdapter(mContext, inappPurchaseModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerViewSubscription.setLayoutManager(layoutManager);
        recyclerViewSubscription.setAdapter(adapter);
    }

    private void setValues(){
        PicksDetailModel picksDetailModel = (PicksDetailModel)getArguments().getSerializable("pickDetailModel");
//        ArrayList<InappPurchaseModel> inappPurchaseModelArrayList = (ArrayList<InappPurchaseModel>)getArguments().getSerializable("InappPurchaseModel");

        if (picksDetailModel.getSportId().equals("1")){
            txtPickDateTime.setText(CommonUtils.getFormattedDate(picksDetailModel.getWeekDate(),"dd-MM-yyyy HH:mm:ss","EEEE MMM d - hh:mm aa"));
        } else {
            txtPickDateTime.setText(CommonUtils.getFormattedDate(picksDetailModel.getPickDate(),"dd-MM-yyyy HH:mm:ss","EEEE MMM d - hh:mm aa"));
        }

        txtHomeTeam.setText(picksDetailModel.getHomeTeamDetails().getTeamName());
        txtVisitingTeam.setText(picksDetailModel.getVisitingTeamDetails().getTeamName());
        CommonUtils.loadImageWithGlide(imgHomeTeam,picksDetailModel.getHomeTeamDetails().getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(imgVisitingTeam,picksDetailModel.getVisitingTeamDetails().getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(imgSport,picksDetailModel.getSportImage(),mContext);
//        System.out.println("size ======= " + inappPurchaseModelArrayList.size());

        ArrayList<InappPurchaseModel> filterd_List = new ArrayList<InappPurchaseModel>();

        for (InappPurchaseModel inapp: ActDashboard.inappPurchaseModelArrayList) {
            //if (inapp.isSubscription) {
                filterd_List.add(inapp);
           // }
            //else {

            //}
        }
        setUpRecycleView(filterd_List);
    }

}
