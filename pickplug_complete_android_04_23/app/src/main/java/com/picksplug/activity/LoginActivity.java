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

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener{
    private ImageView                           img_login_with_facebook;
    private TextView                            txt_login_forgot_password,txt_login,txtPrivacyPolicy;
    private Button                              btn_login, btn_register;
    private EditText                            edt_login_email,edt_login_password;
    private CheckBox                            chkAgreeTerms;
    private Context                             mContext;
    private CustomeProgressDialog               customeProgressDialog;
    private HorizontalDottedProgress            progressBar;
    private CallbackManager                     callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mContext = this;
        initViews();
    }

    public void initViews(){
        img_login_with_facebook     =   (ImageView) findViewById(R.id.img_login_with_facebook);
        btn_login                   =   (Button) findViewById(R.id.btn_login);
        btn_register                =   (Button) findViewById(R.id.btn_register);
        txt_login_forgot_password   =   (TextView) findViewById(R.id.txt_login_forgot_password);
        txt_login                   =   (TextView) findViewById(R.id.txt_login);
        txtPrivacyPolicy            =   (TextView) findViewById(R.id.txt_privacy_policy);
        edt_login_email             =   (EditText) findViewById(R.id.edt_login_email);
        edt_login_password          =   (EditText) findViewById(R.id.edt_login_password);
        chkAgreeTerms               =   (CheckBox) findViewById(R.id.chk_agree_terms);

        img_login_with_facebook.setOnClickListener(this);
        txt_login_forgot_password.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

       setFonts();

        txtPrivacyPolicy.setOnClickListener(this);
        txtPrivacyPolicy.setPaintFlags(txtPrivacyPolicy.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setFonts(){
        txt_login.setTypeface(PickPlugApp.getInstance().getBoldFont());
        txt_login_forgot_password.setTypeface(PickPlugApp.getInstance().getMediumFont());
        btn_login.setTypeface(PickPlugApp.getInstance().getBoldFont());
        btn_register.setTypeface(PickPlugApp.getInstance().getBoldFont());
        edt_login_email.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edt_login_password.setTypeface(PickPlugApp.getInstance().getMediumFont());
        chkAgreeTerms.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtPrivacyPolicy.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_login_with_facebook:
                if (!CommonUtils.isConnectingToInternet(mContext))
                    CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
                else if (!chkAgreeTerms.isChecked())
                    CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_terms),mContext);
                else
                    signInWithFacebook();
                break;

            case R.id.btn_login:
                onLoginBtnClick();
                break;

            case R.id.btn_register:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.txt_login_forgot_password:
                Intent forgotPwdIntent = new Intent(mContext, ForgotPasswordActivity.class);
                startActivity(forgotPwdIntent);
                break;

            case R.id.txt_privacy_policy:
                startActivity(new Intent(mContext,ActLegal.class));
                break;

        }
    }

    private void onLoginBtnClick(){
        String email = edt_login_email.getText().toString().trim();
        String password = edt_login_password.getText().toString().trim();

        if (email.isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email),mContext);
        } else if (!CommonUtils.isEmailValid(email)) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email_not_valid),mContext);
        } else if (password.isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_password),mContext);
        } else if (!chkAgreeTerms.isChecked()){
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_terms),mContext);
        } else {
            if (CommonUtils.isConnectingToInternet(mContext)){
                callLoginApi(email,password);
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }
    }

    private void callLoginApi(String username,String password){
        showLoader();

        ApiClient.getClient().LoginApi(username,password).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                Log.e("response =======",response.body().toString());

                if (response.code() == 200) {

                    if (response.body().getLogin().getStatus().equalsIgnoreCase("success")){
                        String userId       =   response.body().getLogin().getUser().getId();
                        String userName     =   response.body().getLogin().getUser().getFullName();;
                        String email        =   response.body().getLogin().getUser().getEmail();
                        String userProfile  =   response.body().getLogin().getUser().getPhoto();

                        System.out.println("user_id     ========= " + userId);
                        System.out.println("userName    ========= " + userName);
                        System.out.println("email       ========= " + email);
                        System.out.println("userProfile ========= " + userProfile);

                        setValues(userId,userName,email,userProfile);
                        SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("user_name", userName);
                        editor.putString("user_email", email);
                        editor.commit();
                    } else {
                        CommonUtils.showSnackbarWithoutView(response.body().getLogin().getMessage(),mContext);
                    }
                } else{
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
                                            //String profileImageUrl  = ImageRequest.getProfilePictureUri(me.optString("id"), 500, 500).toString();
                                            String email            = me.optString("email");
                                            String gender           = me.optString("gender");
                                            String birthday         = me.optString("birthday");
                                            AccessToken token       = AccessToken.getCurrentAccessToken();
                                            String fb_token         = String.valueOf(token.getToken());

                                            Log.e("Login","name ======= " + name);
                                            Log.e("Login","profileImageUrl ======= " + profileImageUrl);
                                            Log.e("Login","gender ======= " + gender);
                                            Log.e("Login","birthday ======= " + birthday);
                                            Log.e("Login","email ======= " + email);

                                            if (birthday !=null && birthday.trim().isEmpty()){
                                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                try {
                                                    Date date = formatter.parse(birthday);
                                                    birthday = formatter.format(date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (CommonUtils.isConnectingToInternet(mContext)){
                                                callFacebookLogin(name,userId,email,profileImageUrl,birthday);
                                            } else {
                                                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
                                            }


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

                Log.e("Login","Facebook Login Response ========== " + new Gson().toJson(response.body().getLogin()));

                if (response.code() == 200) {


                    String userId       =   response.body().getLogin().getUser().getId();
                    String userName     =   response.body().getLogin().getUser().getFullName();
                    String email        =   response.body().getLogin().getUser().getEmail();
                    String userProfile  =   response.body().getLogin().getUser().getPhoto();

                    setValues(userId,userName,email,userProfile);

                    SharedPreferences prefs = mContext.getSharedPreferences("loginactivity", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_name", userName);
                    editor.putString("user_email", email);
                    editor.commit();

                }else{
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

    private void setValues(String userId,String userName,String email,String userProfile){
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_ID, userId);
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_NAME, userName);
        PreferenceConnector.writeBoolean(mContext, PreferenceConnector.TAG_IS_LOGIN, true);
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_EMAIL, email);
        PreferenceConnector.writeString(mContext, PreferenceConnector.TAG_USER_PROFILE, userProfile);

        Log.e("values","userId ============= " + PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
        Log.e("values","urlPhoto ============= " + PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,""));

        Intent intent = new Intent(mContext, ActDashboard.class);
        startActivity(intent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}