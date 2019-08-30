package com.picksplug.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.adapter.SubscriptionListAdapter;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.InappPurchaseModel;
import com.picksplug.model.SubscriptionModel;
import com.picksplug.model.UserSubscriptionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragSubscription extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private CustomeProgressDialog               customeProgressDialog;
    private HorizontalDottedProgress            progressBar;
    private TextView                            txtPurchaseSubscription;
    private RecyclerView                        recyclerViewSubscription;
    private SubscriptionListAdapter             subscriptionListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_subscription, container, false);
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

            if (CommonUtils.isConnectingToInternet(mContext)){
//                callGetSubscriptionApi(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
                if (ActDashboard.billingClient.isReady()) {
                    setUpRecycleView(ActDashboard.inappPurchaseModelArrayList);

                }
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(getResources().getString(R.string.nav_subscription));
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }
    }

    private void initViews(){
        txtPurchaseSubscription     =   (TextView)rootView.findViewById(R.id.txt_purchase_subscription);
        recyclerViewSubscription    =   (RecyclerView) rootView.findViewById(R.id.recycle_view_subscription);

        txtPurchaseSubscription.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    private void callGetSubscriptionApi(String user){
        showLoader();

        ApiClient.getClient().getSubscriptionApi(user).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);

                JsonObject json = response.body();
                System.out.println("json ========= " + json);

                if (response.code() == 200) {
                    if (json != null) {
                        JsonObject jsonObjectResults        =   json.getAsJsonObject("Results");
                        JsonArray jsonArraySubscriptions    =   jsonObjectResults.getAsJsonArray("Subscriptions");
                        Object jsonObjectUserSub            =   jsonObjectResults.get("User_Subscriptions");
                        String User_Picks                   =   CommonUtils.getJsonStringMemeber(jsonObjectResults,"User_Picks");

                        System.out.println("jsonObjectResults       ========= " + jsonObjectResults);
                        System.out.println("jsonObjectUserSub       ========= " + jsonObjectUserSub);
                        System.out.println("User_Picks              ========= " + User_Picks);

                        ArrayList<SubscriptionModel> subscriptionModelArrayList = new ArrayList<SubscriptionModel>();
                        for (int i=0;i<jsonArraySubscriptions.size();i++){
                            JsonObject subscription         =   jsonArraySubscriptions.get(i).getAsJsonObject();
                            String IsBestValue              =   CommonUtils.getJsonStringMemeber(subscription,"IsBestValue");
                            String Id                       =   CommonUtils.getJsonStringMemeber(subscription,"Id");
                            String Name                     =   CommonUtils.getJsonStringMemeber(subscription,"Name");
                            String Duration                 =   CommonUtils.getJsonStringMemeber(subscription,"Duration");
                            String Sport                    =   CommonUtils.getJsonStringMemeber(subscription,"Sport");
                            String StartDate                =   CommonUtils.getJsonStringMemeber(subscription,"StartDate");
                            String EndDate                  =   CommonUtils.getJsonStringMemeber(subscription,"EndDate");
                            String Enabled                  =   CommonUtils.getJsonStringMemeber(subscription,"Enabled");
                            String ProductId                =   CommonUtils.getJsonStringMemeber(subscription,"ProductId");

                            subscriptionModelArrayList.add(new SubscriptionModel(IsBestValue,Id,Name,Duration,Sport,StartDate,EndDate,Enabled,ProductId,"0"));
                        }

                        ArrayList<UserSubscriptionModel> userSubscriptionModelArrayList = new ArrayList<UserSubscriptionModel>();
                        if (jsonObjectUserSub instanceof JsonArray) {
                            JsonArray jsonArrayUserSub    =   jsonObjectResults.getAsJsonArray("User_Subscriptions");

                            for (int i=0;i<jsonArrayUserSub.size();i++){
                                JsonObject user_subscription    =   jsonArrayUserSub.get(i).getAsJsonObject();
                                String Id                       =   CommonUtils.getJsonStringMemeber(user_subscription,"Id");
                                String User                     =   CommonUtils.getJsonStringMemeber(user_subscription,"User");
                                String Sport                    =   CommonUtils.getJsonStringMemeber(user_subscription,"Sport");
                                String ExpiryDate               =   CommonUtils.getJsonStringMemeber(user_subscription,"ExpiryDate");
                                String updatedAt                =   CommonUtils.getJsonStringMemeber(user_subscription,"updatedAt");
                                String expiry_duration          =   CommonUtils.getJsonStringMemeber(user_subscription,"expiry_duration");
                                String is_new_app_subscription  =   CommonUtils.getJsonStringMemeber(user_subscription,"is_new_app_subscription");
                                String subscription_id          =   CommonUtils.getJsonStringMemeber(user_subscription,"subscription_id");

                                userSubscriptionModelArrayList.add(new UserSubscriptionModel(Id,User,Sport,ExpiryDate,updatedAt,expiry_duration,is_new_app_subscription,subscription_id));
                                System.out.println("SubscriptionId ========= "+userSubscriptionModelArrayList.get(i).getSubscriptionId());
                            }

                        }

                        //check purchased subscriptions
                        for (int i=0; i<subscriptionModelArrayList.size();i++){
                            for (int j=0;j<userSubscriptionModelArrayList.size();j++){
                                if (subscriptionModelArrayList.get(i).getId().equals(userSubscriptionModelArrayList.get(j).getSubscriptionId())){
                                    subscriptionModelArrayList.get(i).setIsPurchased("1");
                                    break;
                                }
                            }
                        }

                        //setUpRecycleView(subscriptionModelArrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Throwable ========" + t.getMessage());
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

//    private void setUpRecycleView(ArrayList<SubscriptionModel> subscriptionModelArrayList){
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
//        subscriptionListAdapter = new SubscriptionListAdapter(mContext, subscriptionModelArrayList);
//        recyclerViewSubscription.setAdapter(subscriptionListAdapter);
//        recyclerViewSubscription.setLayoutManager(layoutManager);
//    }
    private void setUpRecycleView(ArrayList<InappPurchaseModel> inappPurchaseModelArrayList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        subscriptionListAdapter = new SubscriptionListAdapter(mContext, inappPurchaseModelArrayList );
        recyclerViewSubscription.setAdapter(subscriptionListAdapter);
        recyclerViewSubscription.setLayoutManager(layoutManager);
    }

}
