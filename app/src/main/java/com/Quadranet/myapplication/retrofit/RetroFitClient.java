package com.Quadranet.myapplication.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClient {

    private static RetroFitClient instance = null;
    private DNAPedAPI myApi;

    private RetroFitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DNAPedAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(DNAPedAPI.class);
    }

    public static synchronized RetroFitClient getInstance() {
        if (instance == null) {
            instance = new RetroFitClient();
        }
        return instance;
    }

    public DNAPedAPI getMyApi() {
        return myApi;
    }
}
