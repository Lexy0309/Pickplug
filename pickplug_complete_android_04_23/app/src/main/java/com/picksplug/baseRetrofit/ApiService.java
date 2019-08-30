package com.picksplug.baseRetrofit;


import com.google.gson.JsonObject;
import com.picksplug.model.AddTokenResponseModel;
import com.picksplug.model.ChangePwdResponseModel;
import com.picksplug.model.ForgotResponseModel;
import com.picksplug.model.LoginResponseModel;
import com.picksplug.model.RegisterResponseModel;
import com.picksplug.model.RestoreResponseModel;
import com.picksplug.model.UpdateProfileResponseModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("signin.php")
    @FormUrlEncoded
    Call<LoginResponseModel> LoginApi(@Field("email") String email,
                                      @Field("password") String password);

    @POST("forgetpassword.php")
    @FormUrlEncoded
    Call<ForgotResponseModel> ForgotPasswordApi(@Field("email") String email);

    @POST("register.php")
    @FormUrlEncoded
    Call<RegisterResponseModel> RegisterApi(@Field("fullname") String name,
                                            @Field("email") String email,
                                            @Field("password") String password);

    @POST("instagram-login.php")
    @FormUrlEncoded
    Call<LoginResponseModel> LoginFbApi(@Field("fullname") String name,
                                        @Field("email") String email,
                                        @Field("image") String image,
                                        @Field("birthday") String birthday,
                                        @Field("token") String token,
                                        @Field("gender") String gender);
    @POST ("getUserAppLog.php")
    @FormUrlEncoded
    Call<JsonObject> getUserAppLogApi(@Field("user_name") String user_name,
                                      @Field("user_email") String user_email,
                                      @Field("last_updated") String last_updated);

    @GET("getPicksDetails.php")
    Call<JsonObject> getPickDetailsApi(@Query("SportId") String sport_id);

    @GET("getSubscriptions.php")
    Call<JsonObject> getSubscriptionApi(@Query("user") String user);

    @GET("getSubscriptionDate.php")
    Call<JsonObject> getSubscriptionDateApi(@Query("productId") String productId);
    @POST("update_password.php")
    @FormUrlEncoded
    Call<ChangePwdResponseModel> changePasswordApi(@Field("old_password") String old_password,
                                                   @Field("new_password") String new_password,
                                                   @Field("user_id") String user_id);

    @POST("addSubscription_android.php")
    @FormUrlEncoded
    Call<JsonObject> addSubscriptionApi(@Field("productId") String productId,
                                        @Field("user_email") String user_email,
                                        @Field("transaction_id") String transactionId,
                                        @Field("payment_type") String payment_type,
                                        @Field("package_name") String package_name,
                                        @Field("purchased_time") String purchased_time,
                                        @Field("purchased_token") String purchased_token,
                                        @Field("is_auto_renewing") Boolean is_auto_renewing,
                                        @Field("user_google_account_email") String user_google_account_email);

    @POST("update_profile.php")
    @FormUrlEncoded
    Call<UpdateProfileResponseModel> updateProfileApi(@Field("user_id") String user_id,
                                                      @Field("fullname") String fullname,
                                                      @Field("email") String email);

    @POST("update_profile.php")
    @Multipart
    Call<UpdateProfileResponseModel> updateProfileWithPicApi(@Part MultipartBody.Part filePart,
                                                             @Part("user_id") RequestBody user_id,
                                                             @Part("fullname") RequestBody fullname,
                                                             @Part("email") RequestBody email);

    @GET("restoresubscription.php")
    Call<RestoreResponseModel> restoreSubscriptionApi(@Query("user") String sport_id);

    @POST("addandroidtoken.php")
    @FormUrlEncoded
    Call<AddTokenResponseModel> addFirebaseTokenApi(@Field("token") String token,
                                                    @Field("device") String device);
    @POST("updateSubscription_android.php")
    @FormUrlEncoded
    Call<JsonObject> updateSubscriptionApi(@Field("user_email") String user_email,
                                           @Field("productId_list") String productId_list);

    @GET("getNotification.php")
    Call<JsonObject> getNotificationApi(@Query("start_limit") String start_limit);

    @GET("getFreePickList.php")
    Call<JsonObject> getFreePickListApi();

    @GET("getAllSports.php")
    Call<JsonObject> getAllSportsApi(@Query("user_id") String sport_id);

    @GET("getPicksBySportId.php")
    Call<JsonObject> getPicksBySportIdApi(@Query("sport_id") String sport_id, @Query("user_id") String user_id);

    @GET("getUserSubscription.php")
    Call<JsonObject> getUserSubscriptionApi(@Query("user_id") String user_id);

}
