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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.adapter.NotificationListAdapter;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.EndlessOnScrollListener;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.helpers.SimpleDividerItemDecoration;
import com.picksplug.model.NotificationResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.picksplug.helpers.PreferenceConnector.KEY_REQUEST_LOAD;

public class FragNotifications extends Fragment  {
    private Context                                         mContext;
    private View                                            rootView;
    private boolean                                         mAlreadyLoaded;
    private RecyclerView                                    recyclerViewNotifications;
    private NotificationListAdapter                         notificationListAdapter;
    private CustomeProgressDialog                           customeProgressDialog;
    private HorizontalDottedProgress                        progressBar;
    private Boolean                                         IS_SCROLL_DATA = false ;
    private ArrayList<NotificationResponseModel.Result>     notificationModelArrayList;
    private LinearLayoutManager                             layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_notifications, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                    =       getActivity();
            mAlreadyLoaded              =       true;
            notificationModelArrayList  =       new ArrayList<>();
            initViews();

            if (CommonUtils.isConnectingToInternet(mContext)){
                callGetNotificationsApi("0",true);
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(getResources().getString(R.string.nav_notification));
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }

        PreferenceConnector.writeString(mContext,KEY_REQUEST_LOAD,"NO");
        recyclerViewNotifications.addOnScrollListener(new EndlessOnScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                int total = layoutManager.getItemCount();
                if(!IS_SCROLL_DATA){
                    if(total>1&&dy>0){
                        if(CommonUtils.isLastItemDisplaying(recyclerViewNotifications)){
                            callGetNotificationsApi(String.valueOf(notificationModelArrayList.size()),true);
                        }
                    }
                }
            }
            @Override
            public void onScrolledToEnd() {

            }
        });

    }

    private void initViews(){
        recyclerViewNotifications   =   (RecyclerView)rootView.findViewById(R.id.recycle_view_notification);
        setUpRecycleView();
    }

    private void setUpRecycleView(){
        layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        notificationListAdapter = new NotificationListAdapter(mContext,notificationModelArrayList);
        recyclerViewNotifications.setAdapter(notificationListAdapter);
        recyclerViewNotifications.setLayoutManager(layoutManager);

        recyclerViewNotifications.addItemDecoration(new SimpleDividerItemDecoration(mContext));
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void callGetNotificationsApi(String start_limit,final boolean showLoader){
        if (showLoader){
            showLoader();
        }

        ApiClient.getClient().getNotificationApi(start_limit).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(showLoader){
                    CommonUtils.showDismiss(mContext,customeProgressDialog);
                }

                JsonObject json = response.body();
                System.out.println("Response ======== " + json);

                if (response.code() == 200){

                    if (json != null) {
                        Object jsonObjectResult = json.get("Results");

                        if (jsonObjectResult instanceof JsonArray) {
                            JsonArray jsonArrayResult = json.getAsJsonArray("Results");

                            for (int i = 0; i < jsonArrayResult.size(); i++) {
                                JsonObject objectResult = jsonArrayResult.get(i).getAsJsonObject();

                                String notification_id      =   CommonUtils.getJsonStringMemeber(objectResult, "notification_id");
                                String title                =   CommonUtils.getJsonStringMemeber(objectResult, "title");
                                String message              =   CommonUtils.getJsonStringMemeber(objectResult, "message");
                                String created_at           =   CommonUtils.getJsonStringMemeber(objectResult, "created_at");

                                notificationModelArrayList.add(new NotificationResponseModel.Result(notification_id,title,message,created_at));
                            }
                            notificationListAdapter.notifyDataSetChanged();
                        } else {
                            CommonUtils.showSnackbarWithoutView("No More Data to Load",mContext);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if(showLoader){
                    CommonUtils.showDismiss(mContext,customeProgressDialog);
                }

                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
                System.out.println("Throwable ========== " + t.getMessage());
            }
        });
    }

}
