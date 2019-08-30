package com.picksplug.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.picksplug.R;
import com.picksplug.activity_toolbar.HeaderToolbar;
import com.picksplug.activity_toolbar.HeaderToolbarListener;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.ForgotResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LENOVO on 6/28/2018.
 */

public class ForgotPasswordActivity   extends AppCompatActivity implements View.OnClickListener,HeaderToolbarListener {
    private Context                         mContext;
    private TextView                        lblForgotPassword;
    private Button                          btnForgotPassword;
    private EditText                        edtEmail;
    private CustomeProgressDialog           customeProgressDialog;
    private HorizontalDottedProgress        progressBar;
    private HeaderToolbar                   headerToolbar;
    private ImageView                       imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_forget_password);
        mContext = this;
        initViews();
    }

    public void initViews(){
        btnForgotPassword   =   (Button) findViewById(R.id.btn_forget_pwd);
        lblForgotPassword   =   (TextView) findViewById(R.id.lbl_forget_pwd);
        edtEmail            =   (EditText) findViewById(R.id.forget_edt_email);
        imgBack             =   (ImageView) findViewById(R.id.forget_img_back);
        headerToolbar       =   (HeaderToolbar)findViewById(R.id.forget_pwd_toolbar);

        headerToolbar.setUpToolbar(this,this);
        headerToolbar.hideNotificationIcon();
        headerToolbar.setHeaderTitle(getResources().getString(R.string.str_forgot_password));

        imgBack.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        setFonts();
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setFonts(){
        lblForgotPassword.setTypeface(PickPlugApp.getInstance().getBoldFont());
        edtEmail.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_img_back:
                finish();
                break;

            case R.id.btn_forget_pwd:
                onBtnSubmitClick();
                break;
        }
    }

    private void onBtnSubmitClick(){
        String email = edtEmail.getText().toString().trim();

        if (email.trim().isEmpty())
        {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email),mContext);
        } else if (!CommonUtils.isEmailValid(email))
        {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email_not_valid),mContext);
        } else {
            if (CommonUtils.isConnectingToInternet(mContext)){
                callForgotPasswordApi(email);
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }
    }

    private void callForgotPasswordApi(String email) {
        showLoader();

        ApiClient.getClient().ForgotPasswordApi(email).enqueue(new Callback<ForgotResponseModel>() {
            @Override
            public void onResponse(Call<ForgotResponseModel> call, Response<ForgotResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);

                System.out.println("Response ========== " + new Gson().toJson(response.body().getForget()));
                if (response.code() == 200) {

                    if (response.body().getForget().getStatus().equalsIgnoreCase("error")) {
                        CommonUtils.showSnackbarWithoutView(response.body().getForget().getMessage(), mContext);
                    } else {
                        Toast.makeText(mContext,response.body().getForget().getMessage(),Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotResponseModel> call, Throwable t) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });
    }

    @Override
    public void onClickHeaderToolbarBack() {
        onBackPressed();
    }

    @Override
    public void onClickNotification() {

    }
}
