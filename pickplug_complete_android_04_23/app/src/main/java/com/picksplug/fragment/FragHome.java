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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.adapter.SportsListAdapter;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.helpers.SimpleDividerItemDecoration;
import com.picksplug.model.AllSportsModel;
import com.picksplug.model.HomeTeamDetailsModel;
import com.picksplug.model.PicksDetailModel;
import com.picksplug.model.PicksKeyModel;
import com.picksplug.model.VisitingTeamDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragHome extends Fragment implements View.OnClickListener {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private TextView                            txtFreePicks,txtPremiumPicks,txtProfile,txtSubscription,txtFreePicksBadge;
    private RelativeLayout                      relativeFreePicks;
    private LinearLayout                        linearMyProfile;
    private RecyclerView                        recyclerViewSports;
    private CustomeProgressDialog               customeProgressDialog;
    private ImageView                           imgProfile;
    private String                              strTitle,strSportId,strDate,strCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_home, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                        =       getActivity();
            mAlreadyLoaded                  =       true;
            initViews();

            if (!PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,"").trim().isEmpty()){
                CommonUtils.loadCircularImageWithGlide(imgProfile,PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,""),mContext);
            }

        }

        if (mContext != null){
            ((ActDashboard)mContext).showAppLogo();
            ((ActDashboard)mContext).showHamburgerIcon();

            if (CommonUtils.isConnectingToInternet(mContext)){
                callGetAllSportsApi(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }

    }

    private void initViews() {
        txtFreePicks            =   (TextView) rootView.findViewById(R.id.txt_free_picks);
        txtPremiumPicks         =   (TextView) rootView.findViewById(R.id.txt_premium_picks);
        txtProfile              =   (TextView) rootView.findViewById(R.id.txt_my_profile);
        txtSubscription         =   (TextView) rootView.findViewById(R.id.txt_user_subscriptions);
        txtFreePicksBadge       =   (TextView) rootView.findViewById(R.id.txt_free_picks_badge);
        relativeFreePicks       =   (RelativeLayout) rootView.findViewById(R.id.relative_free_picks);
        linearMyProfile         =   (LinearLayout) rootView.findViewById(R.id.linear_my_profile);
        recyclerViewSports      =   (RecyclerView) rootView.findViewById(R.id.recycle_view_sports);
        imgProfile              =   (ImageView) rootView.findViewById(R.id.img_user_profile);

        setFonts();

        relativeFreePicks.setOnClickListener(this);
        linearMyProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relative_free_picks:
                onFreePicksClick();
                break;

            case R.id.linear_my_profile:
                ((ActDashboard)mContext).switchContent (new FragSubscription(),"FragSubscription");
                break;
        }
    }

    private void onFreePicksClick(){
        FragSport fragSport = new FragSport();
        Bundle bundle = new Bundle();
        bundle.putString("title",getResources().getString(R.string.str_free_picks));
        bundle.putString("from","freePicksList");
        fragSport.setArguments(bundle);
        ((ActDashboard)mContext).switchContent(fragSport,"FragSport");
    }

    private void setFonts(){
        txtFreePicks.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtPremiumPicks.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtFreePicksBadge.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txtProfile.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txtSubscription.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    private void setUpRecycleView(ArrayList<AllSportsModel> allSportsModelArrayList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        SportsListAdapter sportsListAdapter = new SportsListAdapter(mContext, allSportsModelArrayList);
        recyclerViewSports.setAdapter(sportsListAdapter);
        recyclerViewSports.setLayoutManager(layoutManager);
        recyclerViewSports.addItemDecoration(new SimpleDividerItemDecoration(mContext));

        sportsListAdapter.setOnRecyclerViewItemClickListener(new SportsListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(int position, AllSportsModel allSportsModel) {

                if (allSportsModel.getCount().equals("0")){
                    FragSport fragSport = new FragSport();
                    Bundle bundle = new Bundle();
                    bundle.putString("title",allSportsModel.getSportName() + " Picks");
                    bundle.putString("sportId",allSportsModel.getId());
                    bundle.putString("from","allSportList");
                    bundle.putString("count",allSportsModel.getCount());
                    fragSport.setArguments(bundle);
                    ((ActDashboard)mContext).switchContent(fragSport,"FragSport");
                } else {
                    strSportId  =   allSportsModel.getId();
                    strTitle    =   allSportsModel.getSportName();
                    strDate     =   allSportsModel.getAddedDate();
                    strCount    =   allSportsModel.getCount();
                    callGetPicksBySportId(allSportsModel.getId(),PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
                }
            }
        });
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        HorizontalDottedProgress progressBar = (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void callGetAllSportsApi(String user_id){
        showLoader();

        ApiClient.getClient().getAllSportsApi(user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                JsonObject json = response.body();

                System.out.println("response ===== " + json);

                if (response.code() == 200) {

                    JsonObject jsonObjectResults    = null;
                    if (json != null) {
                        jsonObjectResults               =   json.getAsJsonObject("Results");
                        JsonArray jsonArrayAllSport     =   jsonObjectResults.getAsJsonArray("allsports");
                        String freepicks                =   CommonUtils.getJsonStringMemeber(jsonObjectResults,"freepicks");
                        String total_sbscription        =   CommonUtils.getJsonStringMemeber(jsonObjectResults,"total_sbscription");
                        Object jsonObjectUserSub        =   jsonObjectResults.get("user_subscription");

                        System.out.println("jsonObjectResults       ========= " + jsonObjectResults);
                        System.out.println("jsonArrayAllSport       ========= " + jsonArrayAllSport);
                        System.out.println("freepicks               ========= " + freepicks);
                        System.out.println("total_sbscription       ========= " + total_sbscription);

                        txtFreePicksBadge.setVisibility(View.VISIBLE);
                        txtFreePicksBadge.setText(freepicks);
                        ArrayList<AllSportsModel> allSportsModelArrayList = new ArrayList<AllSportsModel>();

                        for (int i=0; i<jsonArrayAllSport.size(); i++){
                            JsonObject jsonObjectAllSport = jsonArrayAllSport.get(i).getAsJsonObject();

                            String Id               =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"Id");
                            String SportName        =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"SportName");
                            String Ranking          =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"Ranking");
                            String SportIcon        =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"SportIcon");
                            String thumb            =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"thumb");
                            String AddedDate        =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"AddedDate");
                            String ModifiedDate     =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"ModifiedDate");
                            String Status           =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"Status");
                            String RecordNo         =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"RecordNo");
                            String IsDeleted        =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"IsDeleted");
                            String count            =       CommonUtils.getJsonStringMemeber(jsonObjectAllSport,"count");

                            allSportsModelArrayList.add(new AllSportsModel(Id,SportName,Ranking,SportIcon,thumb,AddedDate,ModifiedDate,Status,RecordNo,IsDeleted,count));
                        }

                        if (jsonObjectUserSub instanceof JsonArray) {
                            JsonArray jsonArrayUserSub = jsonObjectResults.getAsJsonArray("user_subscription");
                            ActDashboard.jsonArrayUserSub = jsonArrayUserSub;
                            boolean isBestValuePurchased = false;

                            for (int i=0; i<jsonArrayUserSub.size(); i++) {
                                JsonObject userSubs =   jsonArrayUserSub.get(i).getAsJsonObject();
                                String Sport        =   CommonUtils.getJsonStringMemeber(userSubs,"Sport");

                                if (Sport.equals("0")){
                                    isBestValuePurchased =true;
                                    break;
                                }
                            }

                            if (isBestValuePurchased){
                                txtSubscription.setText("You have "+String.valueOf(jsonArrayAllSport.size())+" active subscriptions");
                            } else {
                                txtSubscription.setText("You have "+String.valueOf(jsonArrayUserSub.size())+" active subscriptions");
                            }

                        } else {
                            txtSubscription.setText(getResources().getString(R.string.str_no_subscriptions));
                        }

                        setUpRecycleView(allSportsModelArrayList);
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

    private void callGetPicksBySportId(final String sport_id, String user_id){
        showLoader();

        ApiClient.getClient().getPicksBySportIdApi(sport_id,user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);

                Gson gson= new Gson();
                JsonObject json = response.body();
                System.out.println("response ===== " + json);

                if (response.code() == 200) {

                    if (json != null) {

                        JsonObject jsonObjectResults    =   json.getAsJsonObject("Results");
                        String is_allow                 =   CommonUtils.getJsonStringMemeber(jsonObjectResults,"is_allow");
                        JsonObject jsonObjectAllPicks   =   jsonObjectResults.getAsJsonObject("allpicks");

                        JsonArray arrKeys               =   jsonObjectAllPicks.getAsJsonArray("keys");
                        ArrayList<PicksKeyModel> picksKeyModelArrayList = new ArrayList<PicksKeyModel>();

                        for (int i=0;i<arrKeys.size();i++){
                            JsonObject keys         =   arrKeys.get(i).getAsJsonObject();
                            String date             =   CommonUtils.getJsonStringMemeber(keys,"date");
                            String type             =   CommonUtils.getJsonStringMemeber(keys,"type");

                            picksKeyModelArrayList.add(new PicksKeyModel(date,type));

                            System.out.println("key ========= " +picksKeyModelArrayList.get(i).getDate());
                        }

                        HashMap<String,ArrayList<PicksDetailModel>> PremiumPicksModelHashMap = new HashMap<String, ArrayList<PicksDetailModel>>();

                        if (sport_id.equals("1")) {

                            for (Map.Entry<String, JsonElement> entry: jsonObjectAllPicks.entrySet()) {

                                if (entry.getKey().contains("Week")){

                                    System.out.println("Week key ========== "+entry.getKey());

                                    JsonArray arrPicks = jsonObjectAllPicks.getAsJsonArray(entry.getKey());
                                    ArrayList<PicksDetailModel> picksDetailModelArrayList = new ArrayList<PicksDetailModel>();

                                    for (int j=0;j<arrPicks.size();j++){
                                        JsonObject picks = arrPicks.get(j).getAsJsonObject();

                                        String FreePick                 =   CommonUtils.getJsonStringMemeber(picks,"FreePick");
                                        String Id                       =   CommonUtils.getJsonStringMemeber(picks,"Id");
                                        String sportImage               =   CommonUtils.getJsonStringMemeber(picks,"sportImage");
                                        String SportId                  =   CommonUtils.getJsonStringMemeber(picks,"SportId");
                                        String Week                     =   CommonUtils.getJsonStringMemeber(picks,"Week");
                                        String PickDate                 =   CommonUtils.getJsonStringMemeber(picks,"PickDate");
                                        String pickTime                 =   CommonUtils.getJsonStringMemeber(picks,"pickTime");
                                        String pickdateNew              =   CommonUtils.getJsonStringMemeber(picks,"pickdateNew");
                                        String WeekDate                 =   CommonUtils.getJsonStringMemeber(picks,"WeekDate");
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
                                            homeTeamDetailsModel,visitingTeamDetailsModel,Week,WeekDate));
                                    }

                                    PremiumPicksModelHashMap.put(entry.getKey(), picksDetailModelArrayList);

                                }
                            }

                        } else {
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
                                        JsonObject VisitingTeamDetails  =   picks.getAsJsonObject("VisitingTeamDetails");
                                        JsonObject HomeTeamDetails      =   picks.getAsJsonObject("HomeTeamDetails");


                                        HomeTeamDetailsModel homeTeamDetailsModel = gson.fromJson(HomeTeamDetails.toString(),HomeTeamDetailsModel.class);
                                        VisitingTeamDetailsModel visitingTeamDetailsModel = gson.fromJson(VisitingTeamDetails.toString(),VisitingTeamDetailsModel.class);

                                        picksDetailModelArrayList.add(new PicksDetailModel(FreePick,Id,sportImage,SportId,PickDate,pickTime,pickdateNew,
                                                VisitingTeam,HomeTeam,PickAnalysis,PickTitle,PickRecord,PickStatus,ModifiedDate,
                                                homeTeamDetailsModel,visitingTeamDetailsModel));
                                    }

                                    PremiumPicksModelHashMap.put(picksKeyModelArrayList.get(i).getDate(), picksDetailModelArrayList);
                                }
                            }
                        }
                        FragSport fragSport = new FragSport();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("PremiumPicksModelHashMap",PremiumPicksModelHashMap);
                        bundle.putString("title",strTitle + " Picks");
                        bundle.putString("sportId",strSportId);
                        bundle.putString("from","allSportList");
                        bundle.putString("is_allow",is_allow);
                        bundle.putString("count",strCount);
                        fragSport.setArguments(bundle);
                        ((ActDashboard)mContext).switchContent(fragSport,"FragSport");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
                System.out.println("Throwable Picks ========" + t.getMessage());
            }
        });
    }

}
