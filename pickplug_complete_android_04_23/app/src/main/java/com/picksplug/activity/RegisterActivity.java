package com.picksplug.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.picksplug.R;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.model.LoginResponseModel;
import com.picksplug.model.RegisterResponseModel;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LENOVO on 6/26/2018.
 */

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener {
    private ImageView                           img_register_with_facebook, imgBack;
    private EditText                            edt_register_name,edt_register_email,edt_register_password,edt_register_cpassword;
    private Button                              btn_sign_up;
    private CheckBox                            chkAgreeTerms;
    private Context                             mContext;
    private CustomeProgressDialog               customeProgressDialog;
    private HorizontalDottedProgress            progressBar;
    private CallbackManager                     callbackManager;
    private TextView                            txt_register,register_txt_privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mContext = this;
        initViews();
    }

    public void initViews() {

        img_register_with_facebook      =   (ImageView) findViewById(R.id.img_register_with_facebook);
        imgBack                         =   (ImageView) findViewById(R.id.register_img_back);
        txt_register                    =   (TextView) findViewById(R.id.txt_register);
        register_txt_privacy_policy     =   (TextView) findViewById(R.id.register_txt_privacy_policy);
        btn_sign_up                     =   (Button) findViewById(R.id.btn_sign_up);
        edt_register_name               =   (EditText) findViewById(R.id.edt_register_name);
        edt_register_email              =   (EditText) findViewById(R.id.edt_register_email);
        edt_register_password           =   (EditText) findViewById(R.id.edt_register_password);
        edt_register_cpassword          =   (EditText) findViewById(R.id.edt_register_cpassword);
        chkAgreeTerms                   =   (CheckBox) findViewById(R.id.register_chk_agree_terms);

        register_txt_privacy_policy.setPaintFlags(register_txt_privacy_policy.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        imgBack.setOnClickListener(this);
        img_register_with_facebook.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        register_txt_privacy_policy.setOnClickListener(this);

        setFonts();
    }

    private void setFonts(){
        btn_sign_up.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txt_register.setTypeface(PickPlugApp.getInstance().getBoldFont());
        edt_register_name.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edt_register_email.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edt_register_password.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edt_register_cpassword.setTypeface(PickPlugApp.getInstance().getMediumFont());
        chkAgreeTerms.setTypeface(PickPlugApp.getInstance().getMediumFont());
        register_txt_privacy_policy.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_img_back:
                onBackPressed();
                break;

            case R.id.img_register_with_facebook:
                if (!CommonUtils.isConnectingToInternet(mContext))
                    CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
                else if (!chkAgreeTerms.isChecked())
                    CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_terms),mContext);
                else
                    signInWithFacebook();
                break;

            case R.id.btn_sign_up:
                onSignUpBtnClick();
                break;

            case R.id.register_txt_privacy_policy:
                startActivity(new Intent(mContext,ActLegal.class));
                break;
        }
    }

    private void onSignUpBtnClick(){
        String username = edt_register_name.getText().toString().trim();
        String email    = edt_register_email.getText().toString().trim();
        String password = edt_register_password.getText().toString().trim();
        String cpassword = edt_register_cpassword.getText().toString().trim();

        if (username.isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_name),mContext);
        } else if (email.trim().isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email),mContext);
        } else if (!CommonUtils.isEmailValid(email)) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email_not_valid),mContext);
        } else if (password.trim().isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_password),mContext);
        } else if (cpassword.isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_confirm_pwd),mContext);
        } else if (!password.equals(cpassword)) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_pwd_confirm_pwd_not_match),mContext);
        } else if (!chkAgreeTerms.isChecked()){
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_terms),mContext);
        } else {
            if (CommonUtils.isConnectingToInternet(mContext)){
                callRegisterApi(username,email,password);
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void callRegisterApi(String username,String email,String password){
        showLoader();

       ApiClient.getClient().RegisterApi(username,email,password).enqueue(new Callback<RegisterResponseModel>() {
           @Override
           public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
               CommonUtils.showDismiss(mContext,customeProgressDialog);
               Log.e("response =======",new Gson().toJson(response.body().getRegister()));

               if (response.code() == 200) {

                   if (response.body().getRegister().getStatus().equalsIgnoreCase("failed")){
                       CommonUtils.showSnackbarWithoutView(response.body().getRegister().getMessage(),mContext);
                   } else {
                       String userId       =   response.body().getRegister().getUser().getId();
                       String userName     =   response.body().getRegister().getUser().getFullName();
                       String email        =   response.body().getRegister().getUser().getEmail();
                       String userProfile  =   response.body().getRegister().getUser().getPhoto();
                       setValues(userId,userName,email,userProfile);
                       SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
                       SharedPreferences.Editor editor = prefs.edit();
                       editor.putString("user_name", userName);
                       editor.putString("user_email", email);
                       editor.commit();
                   }

               } else {
                   CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
               }
           }

           @Override
           public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
               CommonUtils.showDismiss(mContext,customeProgressDialog);
               CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
           }
       });
    }

    private void signInWithFacebook(){
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList( "email", "user_birthday",  "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        System.out.println("facebook output is: "+loginResult);


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject me, GraphResponse response) {

                                        if (response.getError() != null) {
                                            // handle error
                                        } else {
                                            // get email and id of the user
                                            String userId           = me.optString("id");
                                            String name             = me.optString("name");
                                            String profileImageUrl  =   "";

                                            try {
                                                profileImageUrl     = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500").toString();
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }

                                            String email            = me.optString("email");
                                            String gender           = me.optString("gender");
                                            String birthday         = me.optString("birthday");
                                            AccessToken token       = AccessToken.getCurrentAccessToken();
                                            String fb_token         = String.valueOf(token.getToken());

                                            if (birthday !=null && birthday.trim().isEmpty()){
                                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                try {
                                                    Date date = formatter.parse(birthday);
                                                    birthday = formatter.format(date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            callFacebookLogin(name,userId,email,profileImageUrl,birthday);

                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        System.out.println("facebook output is: cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        System.out.println("facebook output exception is: "+exception);
                    }
                });
    }

    public void callFacebookLogin(String name,String fb_token,String email,String profileImageUrl,String birthday){
        showLoader();
        String gender   =   "Male";
        ApiClient.getClient().LoginFbApi(name,email,profileImageUrl,birthday,fb_token,gender).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);

                if (response.code() == 200) {

                    String userId       =   new Gson().toJson(response.body().getLogin().getUser().getId());
                    String userName     =   new Gson().toJson(response.body().getLogin().getUser().getFullName());;
                    String email        =   new Gson().toJson(response.body().getLogin().getUser().getEmail());
                    String userProfile  =   new Gson().toJson(response.body().getLogin().getUser().getPhoto());

                    setValues(userId,userName,email,userProfile);
                    SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_name", userName);
                    editor.putString("user_email", email);
                    editor.commit();
                } else {
                    CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setValues(String userId,String userName,String email,String userProfile){
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_ID, userId);
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_NAME, userName);
        PreferenceConnector.writeBoolean(mContext, PreferenceConnector.TAG_IS_LOGIN, true);
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_EMAIL, email);
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_PROFILE, userProfile);

        Intent intent = new Intent(mContext, ActDashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext,LoginActivity.class));
        finish();
    }
}