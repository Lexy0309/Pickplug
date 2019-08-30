package com.picksplug.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.model.ChangePwdResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archive_infotech on 2/7/18.
 */

public class FragChangePassword extends Fragment implements View.OnClickListener {
    private Context                     mContext;
    private View                        rootView;
    private boolean                     mAlreadyLoaded;
    private EditText                    edtCurrentPwd,edtNewPwd,edtConfirmPwd;
    private Button                      btnSave;
    private CustomeProgressDialog       customeProgressDialog;
    private HorizontalDottedProgress    progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_change_password, container, false);
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
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(getResources().getString(R.string.str_change_password));
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }
    }

    private void initViews() {
        edtCurrentPwd       =   (EditText) rootView.findViewById(R.id.current_edt_pwd);
        edtNewPwd           =   (EditText) rootView.findViewById(R.id.edt_new_pwd);
        edtConfirmPwd       =   (EditText) rootView.findViewById(R.id.edt_confirm_pwd);
        btnSave             =   (Button) rootView.findViewById(R.id.btn_save);

        setFonts();

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                onBtnSaveClick();
                break;
        }
    }

    private void onBtnSaveClick(){
        String strCurrentPwd    =   edtCurrentPwd.getText().toString();
        String strNewPwd        =   edtNewPwd.getText().toString();
        String strConfirmPwd    =   edtConfirmPwd.getText().toString();

        if (strCurrentPwd.trim().isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_current_pwd),mContext);
        } else if (strNewPwd.trim().isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_new_pwd),mContext);
        } else if (strConfirmPwd.trim().isEmpty()) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_confirm_new_pwd),mContext);
        } else if (!(strConfirmPwd.equals(strNewPwd))) {
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_pwd_confirm_pwd_not_match),mContext);
        } else {
            if (CommonUtils.isConnectingToInternet(mContext)) {
                callChangePasswordApi(strCurrentPwd,strNewPwd, PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,""));
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }
    }

    private void setFonts(){
        edtCurrentPwd.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edtNewPwd.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edtConfirmPwd.setTypeface(PickPlugApp.getInstance().getMediumFont());
        btnSave.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void callChangePasswordApi(final String old_password, final String new_password, final String user_id){
        showLoader();

        ApiClient.getClient().changePasswordApi(old_password,new_password,user_id).enqueue(new Callback<ChangePwdResponseModel>() {
            @Override
            public void onResponse(Call<ChangePwdResponseModel> call, Response<ChangePwdResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                Log.e("response =======",response.body().toString());

                if (response.code() == 200) {
                    System.out.println("old_password ========= " + old_password);
                    System.out.println("new_password ========= " + new_password);
                    System.out.println("user_id ========= " + user_id);
                    Log.e("response ===== ",new Gson().toJson(response.body().getResults()));

                    if (response.body().getResults().getStatus().equalsIgnoreCase("success")){
                        Toast.makeText(mContext,response.body().getResults().getMessage(),Toast.LENGTH_SHORT).show();
                        ((ActDashboard)mContext).onBackPressed();
                        //CommonUtils.showSnackbarWithoutView(response.body().getResults().getMessage(),mContext);
                    } else {
                        Toast.makeText(mContext,response.body().getResults().getMessage(),Toast.LENGTH_SHORT).show();
                        //CommonUtils.showSnackbarWithoutView(response.body().getResults().getMessage(),mContext);
                    }

                }
            }

            @Override
            public void onFailure(Call<ChangePwdResponseModel> call, Throwable t) {
                System.out.println("Throwable ========" + t.getMessage());
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });

    }

}
