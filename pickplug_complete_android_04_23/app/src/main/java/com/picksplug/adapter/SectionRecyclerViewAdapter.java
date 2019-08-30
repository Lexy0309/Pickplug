package com.picksplug.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.fragment.FragPickAnalysis;
import com.picksplug.fragment.FragPickSubscription;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.helpers.SimpleDividerItemDecoration;
import com.picksplug.model.PicksDetailModel;
import com.picksplug.model.SectionModel;
import com.picksplug.model.SubscriptionModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archive_infotech on 7/14/18.
 */

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder> {
    private Context                             mContext;
    private ArrayList<SectionModel>             sectionModelArrayList;
    private CustomeProgressDialog               customeProgressDialog;
    private HorizontalDottedProgress            progressBar;
    private String                              from,strIsAllow;

    public SectionRecyclerViewAdapter(Context mContext, ArrayList<SectionModel> sectionModelArrayList,String from,String strIsAllow) {
        this.mContext = mContext;
        this.sectionModelArrayList = sectionModelArrayList;
        this.from = from;
        this.strIsAllow = strIsAllow;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_row_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {

        if (!CommonUtils.isValidDate(sectionModelArrayList.get(position).getSectionHeader())){
            holder.txtSectionLabel.setText(sectionModelArrayList.get(position).getSectionHeader());
        } else {
            holder.txtSectionLabel.setText(CommonUtils.getFormattedDate(sectionModelArrayList.get(position).getSectionHeader(),"dd-MM-yyyy","EEEE MMM d"));
            System.out.println("date ======= " + sectionModelArrayList.get(position).getSectionHeader());
        }

        holder.txtSectionLabel.setTypeface(PickPlugApp.getInstance().getMediumFont());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);
        holder.itemRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);

        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(mContext, sectionModelArrayList.get(position).getPickDetailModelArrayList());
        holder.itemRecyclerView.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new ItemRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(int position, PicksDetailModel pickDetailModel) {

                if (from.equals("premiumPicks")){

                    if (strIsAllow.equalsIgnoreCase("yes")){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("allPicksModel",pickDetailModel);
                        bundle.putString("from","premiumPicks");
                        FragPickAnalysis fragPickAnalysis = new FragPickAnalysis();
                        fragPickAnalysis.setArguments(bundle);
                        ((ActDashboard)mContext).switchContent(fragPickAnalysis,"FragPickAnalysis");
                    } else {
                        callGetSubscriptionApi(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""),pickDetailModel.getSportId(),pickDetailModel);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("allPicksModel",pickDetailModel);
                    bundle.putString("from","freePicksList");
                    FragPickAnalysis fragPickAnalysis = new FragPickAnalysis();
                    fragPickAnalysis.setArguments(bundle);
                    ((ActDashboard)mContext).switchContent(fragPickAnalysis,"FragPickAnalysis");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSectionLabel;
        private RecyclerView itemRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            txtSectionLabel = (TextView) itemView.findViewById(R.id.txt_section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private void callGetSubscriptionApi(String user, final String sportId,final PicksDetailModel pickDetailModel){
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

//                        ArrayList<SubscriptionModel> subscriptionModelArrayList = new ArrayList<SubscriptionModel>();
//                        for (int i=0;i<jsonArraySubscriptions.size();i++){
//                            JsonObject subscription         =   jsonArraySubscriptions.get(i).getAsJsonObject();
//                            String IsBestValue              =   CommonUtils.getJsonStringMemeber(subscription,"IsBestValue");
//                            String Id                       =   CommonUtils.getJsonStringMemeber(subscription,"Id");
//                            String Name                     =   CommonUtils.getJsonStringMemeber(subscription,"Name");
//                            String Duration                 =   CommonUtils.getJsonStringMemeber(subscription,"Duration");
//                            String Sport                    =   CommonUtils.getJsonStringMemeber(subscription,"Sport");
//                            String StartDate                =   CommonUtils.getJsonStringMemeber(subscription,"StartDate");
//                            String EndDate                  =   CommonUtils.getJsonStringMemeber(subscription,"EndDate");
//                            String Enabled                  =   CommonUtils.getJsonStringMemeber(subscription,"Enabled");
//                            String ProductId                =   CommonUtils.getJsonStringMemeber(subscription,"ProductId");
//
//                            subscriptionModelArrayList.add(new SubscriptionModel(IsBestValue,Id,Name,Duration,Sport,StartDate,EndDate,Enabled,ProductId,"0"));
//                        }
//
//                        ArrayList<SubscriptionModel> filteredSubscriptionArrayList = new ArrayList<SubscriptionModel>();
//                        for (int i=0; i<subscriptionModelArrayList.size();i++){
//                                if (subscriptionModelArrayList.get(i).getIsBestValue().equals("1"))
//                                    filteredSubscriptionArrayList.add(subscriptionModelArrayList.get(i));
//                                else if (subscriptionModelArrayList.get(i).getSport().equals(sportId))
//                                    filteredSubscriptionArrayList.add(subscriptionModelArrayList.get(i));
//                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("pickDetailModel",pickDetailModel);
//                        bundle.putSerializable("SubscriptionArrayList",filteredSubscriptionArrayList);
                        FragPickSubscription fragPickSubscription = new FragPickSubscription();
                        fragPickSubscription.setArguments(bundle);
                        ((ActDashboard)mContext).switchContent(fragPickSubscription,"FragPickSubscription");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Throwable ========" + t.getMessage());
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(mContext.getString(R.string.default_error),mContext);
            }
        });
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

}
