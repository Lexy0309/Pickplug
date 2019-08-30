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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.adapter.SectionRecyclerViewAdapter;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.SimpleDividerItemDecoration;
import com.picksplug.model.HomeTeamDetailsModel;
import com.picksplug.model.PicksDetailModel;
import com.picksplug.model.PicksKeyModel;
import com.picksplug.model.SectionModel;
import com.picksplug.model.VisitingTeamDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragPicks extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private RecyclerView                        recyclerViewPicks;
    private String                              strFrom = "",strSportId="",strIsAllow="";
    private CustomeProgressDialog               customeProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_picks, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                        =   getActivity();
            mAlreadyLoaded                  =   true;
            initViews();

            if (getArguments() != null){
                strFrom                         =   getArguments().getString("from");
                System.out.println("from ======== " + strFrom);

                if (strFrom.equals("freePicksList")){
                    if (CommonUtils.isConnectingToInternet(mContext)){
                        callFreePicksListApi();
                    } else {
                        CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
                    }

                } else {
                    HashMap<String,ArrayList<PicksDetailModel>> premiumPicksModelHashMap = new HashMap<>();
                    premiumPicksModelHashMap    =   (HashMap<String,ArrayList<PicksDetailModel>>) getArguments().getSerializable("PremiumPicksModelHashMap");
                    strSportId                  =   getArguments().getString("sportId");
                    strIsAllow                  =   getArguments().getString("is_allow");

                    setUpRecycleView(premiumPicksModelHashMap,"premiumPicks",strIsAllow);
                }
            }
        }
    }

    private void initViews(){
        recyclerViewPicks   =   (RecyclerView)rootView.findViewById(R.id.recycle_view_picks);
    }

    private void setUpRecycleView(HashMap<String,ArrayList<PicksDetailModel>> hashMap,String from,String strIsAllow){
        ArrayList<SectionModel> sectionModelArrayList           =   new ArrayList<>();

        //for loop for sections
        for (Map.Entry<String,ArrayList<PicksDetailModel>> entry : hashMap.entrySet()) {
            ArrayList<PicksDetailModel> pickDetailModelArrayList     =   new ArrayList<>();
            String key              =       entry.getKey();
            pickDetailModelArrayList.addAll(entry.getValue());
            sectionModelArrayList.add(new SectionModel(key,pickDetailModelArrayList));
        }

        SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(mContext, sectionModelArrayList,from,strIsAllow);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerViewPicks.setHasFixedSize(true);
        recyclerViewPicks.setLayoutManager(layoutManager);
        recyclerViewPicks.setAdapter(adapter);
        recyclerViewPicks.addItemDecoration(new SimpleDividerItemDecoration(mContext));
    }

    private void showLoader() {
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        HorizontalDottedProgress progressBar = (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void callFreePicksListApi(){
        showLoader();

        ApiClient.getClient().getFreePickListApi().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                Gson gson= new Gson();
                JsonObject json = response.body();
                System.out.println("response ===== " + json);

                if (response.code() == 200) {

                    if (json != null) {
                        JsonObject jsonObjectResults = json.getAsJsonObject("Results");
                        String is_allow                 =   CommonUtils.getJsonStringMemeber(jsonObjectResults,"is_allow");
                        JsonObject jsonObjectAllPicks   =   jsonObjectResults.getAsJsonObject("allpicks");

                        if (jsonObjectAllPicks.entrySet().size() > 0 && is_allow.equalsIgnoreCase("yes")){

                            JsonArray arrKeys            =   jsonObjectAllPicks.getAsJsonArray("keys");
                            ArrayList<PicksKeyModel> picksKeyModelArrayList = new ArrayList<PicksKeyModel>();

                            for (int i=0;i<arrKeys.size();i++){
                                JsonObject keys         =   arrKeys.get(i).getAsJsonObject();
                                String date             =   CommonUtils.getJsonStringMemeber(keys,"date");
                                String type             =   CommonUtils.getJsonStringMemeber(keys,"type");

                                picksKeyModelArrayList.add(new PicksKeyModel(date,type));

                                System.out.println("key ========= " +picksKeyModelArrayList.get(i).getDate());
                            }

                            HashMap<String,ArrayList<PicksDetailModel>> freePicksModelHashMap = new HashMap<String, ArrayList<PicksDetailModel>>();
                            for (int i=0;i<picksKeyModelArrayList.size();i++){
                                ArrayList<PicksDetailModel> picksDetailModelArrayList = new ArrayList<PicksDetailModel>();

                                if (jsonObjectAllPicks.has(picksKeyModelArrayList.get(i).getDate())){

                                    JsonArray arrPicks = jsonObjectAllPicks.getAsJsonArray(picksKeyModelArrayList.get(i).getDate());

                                    for (int j=0;j<arrPicks.size();j++){
                                        JsonObject picks = arrPicks.get(j).getAsJsonObject();

                                        String FreePick                 =   CommonUtils.getJsonStringMemeber(picks,"FreePick");
                                        String Id                       =   CommonUtils.getJsonStringMemeber(picks,"Id");
                                        String sportImage               =   CommonUtils.getJsonStringMemeber(picks,"sportImage");
                                        String SportId                  =   CommonUtils.getJsonStringMemeber(picks,"SportId");
                                        String PickDate                 =   CommonUtils.getJsonStringMemeber(picks,"PickDate");
                                        String pickTime                 =   CommonUtils.getJsonStringMemeber(picks,"pickTime");
                                        String pickdateNew              =   CommonUtils.getJsonStringMemeber(picks,"pickdateNew");
                                        String VisitingTeam             =   CommonUtils.getJsonStringMemeber(picks,"VisitingTeam");
                                        String HomeTeam                 =   CommonUtils.getJsonStringMemeber(picks,"HomeTeam");
                                        String PickAnalysis             =   CommonUtils.getJsonStringMemeber(picks,"PickAnalysis");
                                        String PickTitle                =   CommonUtils.getJsonStringMemeber(picks,"PickTitle");
                                        String PickRecord               =   CommonUtils.getJsonStringMemeber(picks,"PickRecord");
                                        String PickStatus               =   CommonUtils.getJsonStringMemeber(picks,"PickStatus");
                                        String ModifiedDate             =   CommonUtils.getJsonStringMemeber(picks,"ModifiedDate");
                                        JsonObject HomeTeamDetails      =   picks.getAsJsonObject("HomeTeamDetails");
                                        JsonObject VisitingTeamDetails  =   picks.getAsJsonObject("VisitingTeamDetails");

                                        HomeTeamDetailsModel homeTeamDetailsModel = gson.fromJson(HomeTeamDetails.toString(),HomeTeamDetailsModel.class);
                                        VisitingTeamDetailsModel visitingTeamDetailsModel = gson.fromJson(VisitingTeamDetails.toString(),VisitingTeamDetailsModel.class);

                                        picksDetailModelArrayList.add(new PicksDetailModel(FreePick,Id,sportImage,SportId,PickDate,pickTime,pickdateNew,
                                                VisitingTeam,HomeTeam,PickAnalysis,PickTitle,PickRecord,PickStatus,ModifiedDate,
                                                homeTeamDetailsModel,visitingTeamDetailsModel));
                                    }

                                    freePicksModelHashMap.put(picksKeyModelArrayList.get(i).getDate(), picksDetailModelArrayList);
                                }

                                System.out.println("key ========= " +picksKeyModelArrayList.get(i).getDate());
                            }

                            setUpRecycleView(freePicksModelHashMap,"freePicks",is_allow);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Throwable Freepick ========" + t.getMessage());
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });
    }

}
