package com.picksplug.baseRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.picksplug.helpers.Config;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Archive_PC_1 on 3/10/2018.
 */

public class ApiClient {
    private static Retrofit retrofit = null;

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static ApiService getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit.create(ApiService.class);
    }

}
