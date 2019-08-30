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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.adapter.RecordsListAdapter;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.SimpleDividerItemDecoration;
import com.picksplug.model.PickDetailModel;
import com.picksplug.model.SportsDetailModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragRecords extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private TextView                            txtTitle,txtRecordNo, txtLastPicks, txtNoRecords;
    private RecyclerView                        recyclerViewRecords;
    private RecordsListAdapter                  recordsListAdapter;
    private ArrayList<PickDetailModel>          pickDetailModelArrayList;
    private ArrayList<SportsDetailModel>        sportsDetailModelArrayList;
    private String                              strSportId="0",from;
    private RelativeLayout                      relativeHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_records, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                    =       getActivity();
            mAlreadyLoaded              =       true;
            pickDetailModelArrayList    =       new ArrayList<>();
            sportsDetailModelArrayList  =       new ArrayList<SportsDetailModel>();
            initViews();

            if (getArguments() != null){
                strSportId  =   getArguments().getString("sportId");
                from        =   getArguments().getString("from");
                System.out.println("sportId ====== " + strSportId);
                System.out.println("from ====== " + from);
            }

            if (from.equalsIgnoreCase("freePicksList")){
                txtLastPicks.setVisibility(View.GONE);
                relativeHeader.setVisibility(View.GONE);
            } else {
                txtLastPicks.setVisibility(View.VISIBLE);
                relativeHeader.setVisibility(View.VISIBLE);

                if (CommonUtils.isConnectingToInternet(mContext)){
                    callGetRecordsApi();
                } else {
                    CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
                }
            }

        }
    }

    private void initViews() {
        txtTitle                =   (TextView) rootView.findViewById(R.id.records_txt_title);
        txtRecordNo             =   (TextView) rootView.findViewById(R.id.records_txt_score);
        txtLastPicks            =   (TextView) rootView.findViewById(R.id.records_txt_last_picks);
        txtNoRecords            =   (TextView) rootView.findViewById(R.id.records_txt_no_records_found);
        recyclerViewRecords     =   (RecyclerView) rootView.findViewById(R.id.recycle_view_records);
        relativeHeader          =   (RelativeLayout) rootView.findViewById(R.id.layoutHeader);

        txtTitle.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtRecordNo.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txtLastPicks.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtNoRecords.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    private void callGetRecordsApi(){

        ApiClient.getClient().getPickDetailsApi(strSportId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                JsonObject json = response.body();
                System.out.println("json response ======= " + json);

                if (response.code() == 200) {

                    JsonObject jsonObjectPicks          =   json.getAsJsonObject("Picks");
                    JsonArray jsonArraySportDetails     =   jsonObjectPicks.getAsJsonArray("SportDetails");
                    JsonArray jsonArrayPickDetails      =   jsonObjectPicks.getAsJsonArray("PickDetails");

                    for (int i=0; i<jsonArraySportDetails.size(); i++){
                        JsonObject sportDetails = jsonArraySportDetails.get(i).getAsJsonObject();
                        SportsDetailModel sportsDetailModel = gson.fromJson(sportDetails,SportsDetailModel.class);
                        sportsDetailModelArrayList.add(sportsDetailModel);
                        txtTitle.setText(String.format("%s Records", sportsDetailModelArrayList.get(i).getSportName()));
                        txtRecordNo.setText(sportsDetailModelArrayList.get(i).getRecordNo());
                    }

                    if (jsonArrayPickDetails.size() > 0){

                        if (checkArrayForNoRecord(jsonArrayPickDetails,"No records found")){
                            txtNoRecords.setVisibility(View.VISIBLE);
                            txtLastPicks.setText("No picks");
                        } else {

                            txtLastPicks.setText(String.format("Last %s Picks", String.valueOf(jsonArrayPickDetails.size())));
                            for (int i=0; i<jsonArrayPickDetails.size(); i++){
                                JsonObject pickDetails          = jsonArrayPickDetails.get(i).getAsJsonObject();
                                PickDetailModel pickDetailModel = gson.fromJson(pickDetails,PickDetailModel.class);
                                pickDetailModelArrayList.add(pickDetailModel);
                                setUpRecycleView();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Throwable Records========== " + t.getMessage());
            }
        });
    }

    private void setUpRecycleView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recordsListAdapter = new RecordsListAdapter(mContext,pickDetailModelArrayList);
        recyclerViewRecords.setAdapter(recordsListAdapter);
        recyclerViewRecords.setLayoutManager(layoutManager);

        recyclerViewRecords.addItemDecoration(new SimpleDividerItemDecoration(mContext));

        recordsListAdapter.setOnRecyclerViewItemClickListener(new RecordsListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(int position, PickDetailModel detailModel) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail_model",detailModel);
                bundle.putString("from","records");
                bundle.putString("SportImage",sportsDetailModelArrayList.get(0).getSportImage());
                FragPickAnalysis fragPickAnalysis = new FragPickAnalysis();
                fragPickAnalysis.setArguments(bundle);
                ((ActDashboard)mContext).switchContent(fragPickAnalysis,"FragPickAnalysis");
            }
        });
    }

    private boolean checkArrayForNoRecord(JsonArray jsonArray, String stringToFind){
        return jsonArray.toString().contains(stringToFind);
    }

}
