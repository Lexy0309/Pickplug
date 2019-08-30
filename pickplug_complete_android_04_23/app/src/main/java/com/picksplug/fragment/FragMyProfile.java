package com.picksplug.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.activity.ActEditProfile;
import com.picksplug.adapter.UserSubscriptionListAdapter;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.model.UserSubscriptionModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragMyProfile extends Fragment implements View.OnClickListener {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private TextView                            txtUserName,txtUserEmail,txtEditProfile,txtChangePwd,txtPremiumPicks,txtNoRecords;
    private ImageView                           imgProfile;
    private LinearLayout                        linearEditProfile,linearChangePassword;
    private RecyclerView                        recyclerViewUserSubscription;
    private CustomeProgressDialog               customeProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_my_profile, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext            =   getActivity();
            mAlreadyLoaded      =   true;
            initViews();
            setValues();

            if (CommonUtils.isConnectingToInternet(mContext)){
                callGetUserSubscriptionApi(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(getResources().getString(R.string.nav_my_profile));
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }
    }

    private void initViews(){
        imgProfile                      =   (ImageView) rootView.findViewById(R.id.img_profile);
        txtUserName                     =   (TextView)rootView.findViewById(R.id.txt_user_name);
        txtUserEmail                    =   (TextView)rootView.findViewById(R.id.txt_user_email);
        txtPremiumPicks                 =   (TextView)rootView.findViewById(R.id.txt_user_premium_picks);
        txtEditProfile                  =   (TextView)rootView.findViewById(R.id.txt_edit_profile);
        txtChangePwd                    =   (TextView)rootView.findViewById(R.id.txt_change_password);
        txtNoRecords                    =   (TextView)rootView.findViewById(R.id.txt_no_records);
        linearEditProfile               =   (LinearLayout) rootView.findViewById(R.id.linear_edit_profile);
        linearChangePassword            =   (LinearLayout)rootView.findViewById(R.id.linear_change_pwd);
        recyclerViewUserSubscription    =   (RecyclerView) rootView.findViewById(R.id.recycle_view_user_subscription);

        setFonts();

        linearEditProfile.setOnClickListener(this);
        linearChangePassword.setOnClickListener(this);
    }

    private void setValues(){
        txtUserName.setText(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_NAME,""));
        txtUserEmail.setText(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_EMAIL,""));

        if (!PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,"").trim().isEmpty()){
            CommonUtils.loadCircularImageWithGlide(imgProfile,PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,""),mContext);
        }
    }

    private void setFonts(){
        txtUserName.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txtUserEmail.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtPremiumPicks.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txtEditProfile.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtChangePwd.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtNoRecords.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_edit_profile:
                startActivity(new Intent(mContext, ActEditProfile.class));
                break;

            case R.id.linear_change_pwd:
                ((ActDashboard)mContext).switchContent(new FragChangePassword(),"FragChangePassword");
                break;
        }
    }

    private void callGetUserSubscriptionApi(String user_id){
        showLoader();

        ApiClient.getClient().getUserSubscriptionApi(user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                JsonObject json = response.body();

                System.out.println("response ===== " + json);

                if (json != null) {
                    Object jsonObjectResult = json.get("Results");

                    if (jsonObjectResult instanceof JsonArray){
                        JsonArray jsonArrayResult = json.getAsJsonArray("Results");

                        ArrayList<UserSubscriptionModel> userSubscriptionModelArrayList = new ArrayList<UserSubscriptionModel>();

                        for (int i=0; i<jsonArrayResult.size(); i++){
                            JsonObject objectResult = jsonArrayResult.get(i).getAsJsonObject();

                            String Id                       =       CommonUtils.getJsonStringMemeber(objectResult,"Id");
                            String User                     =       CommonUtils.getJsonStringMemeber(objectResult,"User");
                            String Sport                    =       CommonUtils.getJsonStringMemeber(objectResult,"Sport");
                            String ExpiryDate               =       CommonUtils.getJsonStringMemeber(objectResult,"ExpiryDate");
                            String updatedAt                =       CommonUtils.getJsonStringMemeber(objectResult,"updatedAt");
                            String expiry_duration          =       CommonUtils.getJsonStringMemeber(objectResult,"expiry_duration");
                            String is_new_app_subscription  =       CommonUtils.getJsonStringMemeber(objectResult,"is_new_app_subscription");
                            String subscription_id          =       CommonUtils.getJsonStringMemeber(objectResult,"subscription_id");
                            String IsBestValue              =       CommonUtils.getJsonStringMemeber(objectResult,"IsBestValue");
                            String Name                     =       CommonUtils.getJsonStringMemeber(objectResult,"Name");
                            String Duration                 =       CommonUtils.getJsonStringMemeber(objectResult,"Duration");
                            String StartDate                =       CommonUtils.getJsonStringMemeber(objectResult,"StartDate");
                            String EndDate                  =       CommonUtils.getJsonStringMemeber(objectResult,"EndDate");
                            String Enabled                  =       CommonUtils.getJsonStringMemeber(objectResult,"Enabled");
                            String ProductId                =       CommonUtils.getJsonStringMemeber(objectResult,"ProductId");
                            String sportIconImgPrefix       =       CommonUtils.getJsonStringMemeber(objectResult,"sportIconImgPrefix");
                            Object SportDetail              =       objectResult.get("SportDetail");

                            List<UserSubscriptionModel.SportDetail> sportDetailList = new ArrayList<UserSubscriptionModel.SportDetail>();
                            if (SportDetail instanceof JsonArray){
                                JsonArray jsonArraySportDetail = objectResult.getAsJsonArray("SportDetail");
                                for (int j=0; j<jsonArraySportDetail.size();j++){
                                    JsonObject jsonObjectSportDetail = jsonArraySportDetail.get(j).getAsJsonObject();

                                    String SportId              =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"Id");
                                    String SportName            =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"SportName");
                                    String Ranking              =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"Ranking");
                                    String SportIcon            =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"SportIcon");
                                    String thumb                =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"thumb");
                                    String AddedDate            =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"AddedDate");
                                    String ModifiedDate         =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"ModifiedDate");
                                    String Status               =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"Status");
                                    String RecordNo             =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"RecordNo");
                                    String IsDeleted            =       CommonUtils.getJsonStringMemeber(jsonObjectSportDetail,"IsDeleted");

                                    sportDetailList.add(new UserSubscriptionModel.SportDetail(SportId,SportName,Ranking,SportIcon,thumb,AddedDate,ModifiedDate,Status,RecordNo,IsDeleted));
                                }
                            } else {
                                sportDetailList = null;
                            }

                            userSubscriptionModelArrayList.add(new UserSubscriptionModel(Id,User,Sport,ExpiryDate,updatedAt,expiry_duration,is_new_app_subscription,subscription_id,IsBestValue,Name,Duration,StartDate,EndDate,Enabled,ProductId,sportDetailList,sportIconImgPrefix));
                            setUpRecycleView(userSubscriptionModelArrayList,sportIconImgPrefix);
                        }
                    } else {
                        recyclerViewUserSubscription.setVisibility(View.GONE);
                        txtNoRecords.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
                System.out.println("Throwable ============ " + t.getMessage());
            }
        });
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        HorizontalDottedProgress progressBar = (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setUpRecycleView(ArrayList<UserSubscriptionModel> userSubscriptionModelArrayList,String sportIconImgPrefix){
        RecyclerView.LayoutManager layoutManager                =   new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        UserSubscriptionListAdapter subscriptionListAdapter     =   new UserSubscriptionListAdapter(mContext, userSubscriptionModelArrayList,sportIconImgPrefix);
        recyclerViewUserSubscription.setAdapter(subscriptionListAdapter);
        recyclerViewUserSubscription.setLayoutManager(layoutManager);
    }
}
