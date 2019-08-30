package com.picksplug.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.picksplug.BuildConfig;
import com.picksplug.R;
import com.picksplug.activity_toolbar.HeaderToolbar;
import com.picksplug.activity_toolbar.HeaderToolbarListener;
import com.picksplug.baseRetrofit.ApiClient;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.CustomeProgressDialog;
import com.picksplug.helpers.HorizontalDottedProgress;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.model.UpdateProfileResponseModel;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActEditProfile extends AppCompatActivity implements View.OnClickListener, HeaderToolbarListener {
    private Context                         mContext;
    private EditText                        edtUserName,edtEmail;
    private ImageView                       imgProfile;
    private Button                          btnSave;
    private CustomeProgressDialog           customeProgressDialog;
    private HorizontalDottedProgress        progressBar;
    private HeaderToolbar                   headerToolbar;

    private int                             count;
    private String                          dir;
    private File                            currentTakePic,file;
    private static final String             SHARED_FOLDER = "shared";
    private static final String             SHARED_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".myfileprovider";
    private final static int                GALLERY_REQUEST_CODE = 1965,
                                            CAPTURE_IMAGE_REQUEST_CODE = 1966,
                                            MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_profile);

        mContext    =   this;
        initViews();
    }

    private void setFonts(){
        edtUserName.setTypeface(PickPlugApp.getInstance().getMediumFont());
        edtEmail.setTypeface(PickPlugApp.getInstance().getMediumFont());
        btnSave.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    private void initViews() {
        headerToolbar   =   (HeaderToolbar) findViewById(R.id.edit_profile_toolbar);
        edtUserName     =   (EditText) findViewById(R.id.user_edt_name);
        edtEmail        =   (EditText) findViewById(R.id.user_edt_email);
        imgProfile      =   (ImageView) findViewById(R.id.update_img_profile);
        btnSave         =   (Button) findViewById(R.id.btn_update_profile);

        headerToolbar.setUpToolbar(this,this);
        headerToolbar.setHeaderTitle(getResources().getString(R.string.str_edit_profile));

        setFonts();
        setValues();

        imgProfile.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void setValues(){
        edtUserName.setText(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_NAME,""));
        edtEmail.setText(PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_EMAIL,""));

        if (!PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,"").trim().isEmpty()){
            CommonUtils.loadCircularImageWithGlide(imgProfile,PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_PROFILE,""),mContext);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_profile:
                onBtnSaveClick();
                break;

            case R.id.update_img_profile:
                checkPermission();
                break;
        }
    }

    private void onBtnSaveClick(){
        String strUserId        =   PreferenceConnector.readString(mContext,PreferenceConnector.TAG_USER_ID,"");
        String strUserName      =   edtUserName.getText().toString();
        String strUserEmail     =   edtEmail.getText().toString();

        if (strUserName.trim().isEmpty()){
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_name),mContext);
        } else if (strUserEmail.trim().isEmpty()){
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email),mContext);
        } else if (!CommonUtils.isEmailValid(strUserEmail)){
            CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.val_error_email_not_valid),mContext);
        } else {
            if (CommonUtils.isConnectingToInternet(mContext)){
                if (file != null){
                    callUpdateProfileWithPicApi(strUserId,strUserName,strUserEmail);
                    Log.e("EditProfile","========= with pic ==========");
                } else {
                    callUpdateProfileApi(strUserId,strUserName,strUserEmail);
                    Log.e("EditProfile","========= without pic ==========");
                }
            } else {
                CommonUtils.showSnackbarWithoutView(getResources().getString(R.string.str_no_network_error),mContext);
            }
        }
    }

    @Override
    public void onClickHeaderToolbarBack() {
        onBackPressed();
    }

    @Override
    public void onClickNotification() {

    }

    private void showLoader(){
        customeProgressDialog   =   new CustomeProgressDialog(mContext, R.layout.custom_progess_dialog);
        progressBar             =   (HorizontalDottedProgress) customeProgressDialog.findViewById(R.id.progressBar);
        customeProgressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void callUpdateProfileApi(final String user_id, final String fullname, final String email){
        showLoader();

        ApiClient.getClient().updateProfileApi(user_id,fullname,email).enqueue(new Callback<UpdateProfileResponseModel>() {
            @Override
            public void onResponse(Call<UpdateProfileResponseModel> call, Response<UpdateProfileResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                Log.e("response =======",response.body().toString());

                if (response.code() == 200) {
                    System.out.println("user_id ========= " + user_id);
                    Log.e("response ===== ",new Gson().toJson(response.body().getResults()));

                    if (response.body().getResults().getStatus().equalsIgnoreCase("success")){
                        //CommonUtils.showSnackbarWithoutView(response.body().getResults().getMessage(),mContext);
                        Toast.makeText(mContext,response.body().getResults().getMessage(),Toast.LENGTH_SHORT).show();
                        PreferenceConnector.writeString(mContext,PreferenceConnector.TAG_USER_NAME,response.body().getResults().getData().getFullName());
                        PreferenceConnector.writeString(mContext,PreferenceConnector.TAG_USER_EMAIL,response.body().getResults().getData().getEmail());
                        onBackPressed();
                    } else {
                        CommonUtils.showSnackbarWithoutView(response.body().getResults().getMessage(),mContext);
                    }

                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponseModel> call, Throwable t) {
                System.out.println("Throwable ========" + t.getMessage());
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });

    }

    private void callUpdateProfileWithPicApi(String update_id,String update_fullname,String update_email){
        showLoader();

        Log.e("file name"," =========== "+file.getName());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        RequestBody user_id             =   RequestBody.create(MediaType.parse("text/plain"), update_id);
        RequestBody name                =   RequestBody.create(MediaType.parse("text/plain"), update_fullname);
        RequestBody email               =   RequestBody.create(MediaType.parse("text/plain"), update_email);

        ApiClient.getClient().updateProfileWithPicApi(body,user_id,name,email).enqueue(new Callback<UpdateProfileResponseModel>() {
            @Override
            public void onResponse(Call<UpdateProfileResponseModel> call, Response<UpdateProfileResponseModel> response) {
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                Log.e("response =======",response.body().toString());

                if (response.code() == 200) {
                    Log.e("response ===== ",new Gson().toJson(response.body().getResults()));

                    if (response.body().getResults().getStatus().equalsIgnoreCase("success")){
                        CommonUtils.showSnackbarWithoutView(response.body().getResults().getMessage(),mContext);

                        PreferenceConnector.writeString(mContext,PreferenceConnector.TAG_USER_NAME,response.body().getResults().getData().getFullName());
                        PreferenceConnector.writeString(mContext,PreferenceConnector.TAG_USER_EMAIL,response.body().getResults().getData().getEmail());
                        PreferenceConnector.writeString(mContext,PreferenceConnector.TAG_USER_PROFILE,response.body().getResults().getData().getPhoto());
                        trimCache(mContext);
                        onBackPressed();
                    } else {
                        CommonUtils.showSnackbarWithoutView(response.body().getResults().getMessage(),mContext);
                    }

                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponseModel> call, Throwable t) {
                Log.e("EditProfile","Throwable ========" + t.getMessage());
                CommonUtils.showDismiss(mContext,customeProgressDialog);
                CommonUtils.showSnackbarWithoutView(getString(R.string.default_error),mContext);
            }
        });

    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
            }
        } else {
            showPictureDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==  PackageManager.PERMISSION_GRANTED && grantResults[2] ==  PackageManager.PERMISSION_GRANTED) {
                showPictureDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //imgProfile.setImageBitmap(bitmap);
                    CommonUtils.loadCircularImageUriWithGlide(imgProfile,contentURI,mContext);

                    file = new File(CommonUtils.getPath(contentURI,mContext));
                    Log.e("file ====== " , file.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            String realPath = currentTakePic.getAbsolutePath();
            System.out.println("Real Path Of Image "+realPath);
            Log.e("Real Path","======"+realPath);
            Bitmap bitmap = CommonUtils.decodeFile(currentTakePic);
            Uri selected_camera_uri = CommonUtils.getImageUri(mContext,bitmap);

            CommonUtils.loadCircularImageUriWithGlide(imgProfile,selected_camera_uri,mContext);

            //getImageUri
            file = new File(CommonUtils.getPath(selected_camera_uri,mContext));
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    public void takePhotoFromCamera ()
    {
        File sharedFolder = new File(mContext.getFilesDir(), SHARED_FOLDER);
        sharedFolder.mkdirs();
        try {
            count++;
            currentTakePic = File.createTempFile("pickplug"+count, ".jpg", sharedFolder);
            currentTakePic.createNewFile();
            System.out.println(currentTakePic.getAbsolutePath());
            Uri outputFileUri = FileProvider.getUriForFile(mContext, SHARED_PROVIDER_AUTHORITY,currentTakePic);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+SHARED_FOLDER;
                sharedFolder = new File(dir);
                sharedFolder.mkdirs();
                String file = dir+count+ UUID.randomUUID().toString()+"pickplug.jpg";
                currentTakePic = new File(file);
                try {
                    currentTakePic.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                outputFileUri = Uri.fromFile(currentTakePic);

            }
            cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

}
