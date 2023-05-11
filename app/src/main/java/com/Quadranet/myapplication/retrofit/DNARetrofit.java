package com.Quadranet.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DNARetrofit {
    @GET("/")
    Call<EposResult> callEpos();
}
