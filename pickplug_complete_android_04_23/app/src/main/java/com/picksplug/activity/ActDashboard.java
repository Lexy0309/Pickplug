package com.picksplug.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.billingclient.api.*;
import com.google.gson.Gson;
//import com.google.gson.JsonArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.drawer_click.FragmentDrawer;
import com.picksplug.fragment.FragAbout;
import com.picksplug.fragment.FragHome;
import com.picksplug.fragment.FragMyProfile;
import com.picksplug.fragment.FragNotifications;
import com.picksplug.helpers.AppRater;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.Config;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.model.AddTokenResponseModel;
import com.picksplug.model.InappPurchaseModel;
import com.picksplug.model.RestoreResponseModel;
import com.picksplug.model.SubscriptionModel;
import com.picksplug.model.UserSubscriptionModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActDashboard extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener, PurchasesUpdatedListener {
    private Context                     mContext;
    private FragmentDrawer              drawerFragment;
    private ActionBarDrawerToggle       toggle;
    private DrawerLayout                drawer_layout;
    private RelativeLayout              relToolbarBack;
    private LinearLayout                layoutAppLogo,layoutNotification, layoutRestore;
    private Toolbar                     mToolbar;
    private TextView                    txtFragHeader;
    private CustomeProgressDialog       customeProgressDialog;
    private HorizontalDottedProgress    progressBar;
    public static BillingClient         billingClient;
    public static ArrayList<InappPurchaseModel> inappPurchaseModelArrayList = new ArrayList<InappPurchaseModel>();
    public static JsonArray jsonArrayUserSub = new JsonArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);

        mContext            =   this;
        mToolbar            =   (Toolbar) findViewById(R.id.toolbar);
        txtFragHeader       =   (TextView)mToolbar.findViewById(R.id.txtHeader);
        relToolbarBack      =   (RelativeLayout) mToolbar.findViewById(R.id.navBack);
        layoutAppLogo       =   (LinearLayout) mToolbar.findViewById(R.id.layoutAppLogo);
        layoutNotification  =   (LinearLayout) mToolbar.findViewById(R.id.layoutNotification);
        layoutRestore       =   (LinearLayout) mToolbar.findViewById(R.id.layoutRestore);
        billingClient = BillingClient.newBuilder(this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    final List<String> skuList = new ArrayList<>();
                    skuList.add("com.picksplug.parlay");
                    skuList.add("com.picksplug.nhl");
                    skuList.add("com.picksplug.mlb");
                    skuList.add("com.picksplug.nba");
                    skuList.add("com.picksplug.ncaab");
                    skuList.add("com.picksplug.ncaaf");
                    skuList.add("com.picksplug.nfl");
                    skuList.add("com.picksplug.all_sports_monthly");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

                    final ArrayList<SkuDetails> skuDetailsList_total = new ArrayList<SkuDetails>();

                    ActDashboard.billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                                    if (responseCode == BillingClient.BillingResponse.OK) {
                                        skuDetailsList_total.addAll(skuDetailsList);
                                        SkuDetailsParams.Builder paramsForSub = SkuDetailsParams.newBuilder();
                                        paramsForSub.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                                        ActDashboard.billingClient.querySkuDetailsAsync(paramsForSub.build(),
                                                new SkuDetailsResponseListener() {
                                                    @Override
                                                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                                                        if (responseCode == BillingClient.BillingResponse.OK) {
                                                            skuDetailsList_total.addAll(0,skuDetailsList);
                                                            for (int i=0;i<skuDetailsList_total.size();i++){
                                                                String name                     =   skuDetailsList_total.get(i).getDescription();
                                                                String description              =   skuDetailsList_total.get(i).getDescription();
                                                                String price                    =   skuDetailsList_total.get(i).getPrice();
                                                                String duration                 =   skuDetailsList_total.get(i).getSubscriptionPeriod();

                                                                String startDate                =   "start date";
                                                                String endDate                  =   "end date";
                                                                String productId                =   skuDetailsList_total.get(i).getSku();
//                                                                callGetSubscriptionDateApi(productId);
                                                                Boolean isSubscription          =   skuDetailsList_total.get(i).getType().contentEquals("subs");

                                                                ActDashboard.inappPurchaseModelArrayList.add(new InappPurchaseModel(isSubscription, name, price, duration, description, startDate, endDate, productId, false, skuDetailsList_total.get(i)));
                                                            }

                                                        } else {
                                                            System.out.println("Can't querySkuDetailsAsync, responseCode: $responseCode");
                                                        }
                                                    }
                                                });
                                    } else {
                                        System.out.println("Can't querySkuDetailsAsync, responseCode: $responseCode");
                                    }
                                }
                            });
                    // The billing client is ready. You can query purchases here.
                    Purchase.PurchasesResult purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                    List<Purchase> purchaseList = purchases.getPurchasesList();
                    ArrayList<String> productId_list = new ArrayList<String>();
                    for (int i=0; i <purchaseList.size(); i++){
                        String product_id = purchaseList.get(i).getSku();
                        productId_list.add(product_id);
                    }

                    SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
                    String user_email = prefs.getString("user_email", null);

                    StringBuilder builder = new StringBuilder();
                    for(String s : productId_list) {
                        builder.append(s);
                    }
                    String str = builder.toString();
                    callUpdateSubscriptionAndroidApi(user_email, str);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
        drawer_layout   =   (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerFragment  =   (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        toggle = new ActionBarDrawerToggle(this, drawer_layout,
                mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };

        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.menu_icon);
        LinearLayout MainContainerBody = (LinearLayout) findViewById(R.id.MainContainerBody);
        drawerFragment.setUp(MainContainerBody, R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        loadHomePage();

        txtFragHeader.setTypeface(PickPlugApp.getInstance().getMediumFont());

        relToolbarBack.setOnClickListener(this);
        layoutNotification.setOnClickListener(this);
        layoutRestore.setOnClickListener(this);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }

        SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
        String user_name = prefs.getString("user_name", null);
        String user_email = prefs.getString("user_email", null);

        Long tsLong = System.currentTimeMillis()/1000;
        String last_updated = tsLong.toString();

        callGetUserAppLogApi(user_name, user_email, last_updated);

        AppRater.app_launched(this);
    }

    private void callGetSubscriptionDateApi(String productId){
        showLoader();

        ApiClient.getClient().getSubscriptionApi(productId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);

                JsonObject json = response.body();
                System.out.println("json ========= " + json);

                if (response.code() == 200) {
                    if (json != null) {
                        JsonObject jsonObjectResults        =   json.getAsJsonObject("Results");
                        JsonArray jsonArraySubscriptions    =   jsonObjectResults.getAsJsonArray();
                        System.out.println("jsonObjectResults       ========= " + jsonObjectResults);

                        for (int i=0;i<jsonArraySubscriptions.size();i++){
                            JsonObject subscription         =   jsonArraySubscriptions.get(i).getAsJsonObject();
                            String StartDate              =   CommonUtils.getJsonStringMemeber(subscription,"StartDate");
                            String EndDate                       =   CommonUtils.getJsonStringMemeber(subscription,"EndDate");


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
    private void callUpdateSubscriptionAndroidApi(String user_email, String productId_list){
        ApiClient.getClient().updateSubscriptionApi(user_email, productId_list).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext, customeProgressDialog);
                Log.e("UpdateSubscription","Update    Subscription=============");
                if(response.code() == 200){
                    CommonUtils.showSnackbarWithoutView("Success", mContext);
                }else{
                    CommonUtils.showSnackbarWithoutView("callUpdateSubscriptionAndroidApi Unable to reach Server. Please try again", mContext);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommonUtils.showDismiss(mContext, customeProgressDialog);
                CommonUtils.showSnackbarWithoutView("callUpdateSubscriptionAndroidApi Unable to reach server. Please try again", mContext);

            }
        });
    }
    private  void callGetUserAppLogApi(String user_name, String user_email, String last_updated){
        ApiClient.getClient().getUserAppLogApi(user_name, user_email, last_updated).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommonUtils.showDismiss(mContext, customeProgressDialog);
                Log.e("AppLog","App log=============");
                if(response.code() == 200){
                    CommonUtils.showSnackbarWithoutView("Success", mContext);
                }else{
                    CommonUtils.showSnackbarWithoutView("callGetUserAppLogApi Unable to reach Server. Please try again", mContext);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommonUtils.showDismiss(mContext, customeProgressDialog);
                CommonUtils.showSnackbarWithoutView("callGetUserAppLogApi Unable to reach server. Please try again", mContext);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        updateTokenTimer();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void loadHomePage() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }

        displayView(0);
    }

    public void displayView(int position) {
        switch (position) {
            case 0:
                ///MUST REPLACE HERE:::::::::::::
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_body, new FragHome(), "FragHome")
                        .commit();
                break;

            case 1:
                switchContent(new FragMyProfile(), "FragMyProfile");
                break;

            case 2:
                switchContent(new FragAbout(),"FragAbout");
                break;

            case 3:
                openPlayStore();
                break;

            case 4:
                startActivity(new Intent(mContext,ActLegal.class));
                break;

            case 5:
                onContactUsClick();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navBack:
                onBackPressed();
                layoutNotification.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutNotification:
                switchContent(new FragNotifications(),"FragNotifications");
                break;

            case R.id.layoutRestore:
                if (CommonUtils.isConnectingToInternet(mContext)){
                    callRestoreSubscriptionApi(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
                } else {
                    CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
                }
                break;
        }
    }

    //switch content and add to backStack
    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction switch_Ft = fragmentManager.beginTransaction();
        switch_Ft.replace(R.id.container_body, fragment).addToBackStack(tag).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setToolbarTitle(String title){
        txtFragHeader.setVisibility(View.VISIBLE);
        txtFragHeader.setText(title);
    }

    public void showAppLogo(){
        layoutAppLogo.setVisibility(View.VISIBLE);
        txtFragHeader.setVisibility(View.GONE);
        relToolbarBack.setVisibility(View.GONE);
        layoutRestore.setVisibility(View.GONE);
    }

    public void showRestoreIcon(){
        layoutRestore.setVisibility(View.VISIBLE);
        layoutNotification.setVisibility(View.GONE);
    }

    public void hideAppLogo(){
        layoutAppLogo.setVisibility(View.GONE);
        txtFragHeader.setVisibility(View.VISIBLE);
        relToolbarBack.setVisibility(View.VISIBLE);
    }

    public void showHamburgerIcon(){
        toggle.setDrawerIndicatorEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.menu_icon);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        System.out.println("toggle ====== " + toggle.isDrawerIndicatorEnabled());
    }

    public void hideHamburgerIcon(){
        toggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(null);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        System.out.println("toggle ====== " + toggle.isDrawerIndicatorEnabled());
    }

    private void openPlayStore(){
        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }
    }

    private void callRestoreSubscriptionApi(String user_id){
        showLoader();

        ApiClient.getClient().restoreSubscriptionApi(user_id).enqueue(new Callback<RestoreResponseModel>() {
            @Override
            public void onResponse(Call<RestoreResponseModel> call, Response<RestoreResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                Log.e("response =======",response.body().toString());

                if (response.code() == 200) {
                    Log.e("response ===== ",new Gson().toJson(response.body().getResponse()));

                    CommonUtils.showSnackbarWithoutView(response.body().getMessage(),mContext);
                }
            }

            @Override
            public void onFailure(Call<RestoreResponseModel> call, Throwable t) {
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

    private void updateTokenTimer() {
        Log.e("","==== updateTokenTimer ======");
        boolean isSendRequest = false;
        long updateTokenTime = PreferenceConnector.readLong(mContext,PreferenceConnector.KEY_TIME,0);
        if (updateTokenTime==0){
            System.out.println("=====Dealparse=========="+updateTokenTime+"======difference====");
            PreferenceConnector.writeLong(mContext,PreferenceConnector.KEY_TIME,System.currentTimeMillis());
            isSendRequest = true;
        }else{
            long currentTimeMillis  =   System.currentTimeMillis();
            long difference         =   currentTimeMillis -  updateTokenTime;
            long seconds            =   difference /1000;
            System.out.println(currentTimeMillis+"=====Dealparse=========="+updateTokenTime+"======difference===="+difference+"========"+seconds);

            if (seconds > Config.ADS_TIMER_INTERVAL){
                PreferenceConnector.writeLong(mContext,PreferenceConnector.KEY_TIME,currentTimeMillis);
                isSendRequest = true;
            }
        }
        //isSendRequest = true;
        if (isSendRequest){
            System.out.println("===== isSendRequest ======");
            try {
                String strDeviceToken="";
                strDeviceToken = PreferenceConnector.readString(mContext, PreferenceConnector.KEY_PUSH_TOKEN, "");

                Log.e("strDeviceToken","===="+strDeviceToken);
                Log.e("DEVICE_TYPE_ANDROID","===="+ Config.DEVICE_TYPE_ANDROID);

                if (CommonUtils.isConnectingToInternet(mContext)){
                    System.out.println("======= if body api call =========");
                    callAddFirebaseTokenApi(strDeviceToken,Config.DEVICE_TYPE_ANDROID);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void callAddFirebaseTokenApi(String token, String device){
        System.out.println("======= api call =========");

            ApiClient.getClient().addFirebaseTokenApi(token,device).enqueue(new Callback<AddTokenResponseModel>() {
        @Override
        public void onResponse(Call<AddTokenResponseModel> call, Response<AddTokenResponseModel> response) {
            System.out.println("Response ========== " + new Gson().toJson(response.body().getResults()));

            if (response.body().getResults().getStatus().equals("success")){
                System.out.println("status ========= " + response.body().getResults().getStatus());
                System.out.println("message ========= " + response.body().getResults().getMessage());
            } else {
                System.out.println("status ========= " + response.body().getResults().getStatus());
                System.out.println("message ========= " + response.body().getResults().getMessage());
            }
        }

        @Override
        public void onFailure(Call<AddTokenResponseModel> call, Throwable t) {
            System.out.println("Throwable ========= " + t.getMessage());
        }
    });
}

    private void onContactUsClick(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",Config.CONTACT_EMAIL, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    @Override
    public void onPurchasesUpdated(@BillingClient.BillingResponse int responseCode, List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }

    private void handlePurchase(Purchase purchase) {
        String order_id = purchase.getOrderId();
        String package_name = purchase.getPackageName();
        String sku = purchase.getSku();
        Long purchased_timestamp = purchase.getPurchaseTime();
        String purchased_time = purchased_timestamp.toString();
        String purchase_token = purchase.getPurchaseToken();
        Boolean is_auto_renewing = purchase.isAutoRenewing();
        String user_google_account_email = getGoogleAccount();

        SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
        final String user_email = prefs.getString("user_email", null);
        callAddSubScriptionApi(user_email, sku, order_id, "Google", package_name, purchased_time, purchase_token, is_auto_renewing, user_google_account_email);
        // API CALL HERE====================================

    }

    private  void callAddSubScriptionApi(String user_email, String productId, String tranasction_id, String payment_type, String package_name, String purchased_time, String purchase_token, Boolean is_auto_renewing, String user_google_account_email){
        ApiClient.getClient().addSubscriptionApi(user_email, productId, tranasction_id, payment_type, package_name, purchased_time, purchase_token, is_auto_renewing, user_google_account_email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.e("AppLog","App log=============");
                if(response.code() == 200){
                    CommonUtils.showSnackbarWithoutView("Success", mContext);
                }else{
                    CommonUtils.showSnackbarWithoutView("callAddSubScriptionApi Unable to reach Server. Please try again", mContext);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommonUtils.showSnackbarWithoutView("callAddSubScriptionApi Unable to reach server. Please try again", mContext);
            }
        });
    }

    private String getGoogleAccount() {
        Account[] accounts = AccountManager.get(this).getAccounts();
        ArrayList<String> playAccounts = new ArrayList<String>();
        if(accounts != null && accounts.length > 0) {
            for (Account account : accounts) {
                String name = account.name;
                String type = account.type;
                if(account.type.equals("com.google")) {
                    playAccounts.add(name);
                }
            }
            Log.d("tag", "Google Play Accounts present on phone are :: " + playAccounts);
        }
        return playAccounts.get(playAccounts.size()-1);
    }
}
