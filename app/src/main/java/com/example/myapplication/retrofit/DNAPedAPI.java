package com.example.myapplication.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DNAPedAPI {
    @POST("GetPedConnection")
    Call<DNAPedResult> getPedURL(@Query("SerialNumber") String SerialNumber);
    String BASE_URL = "https://dbxdev.quadranet.co.uk/Interfaces/API/DNAPayments/";
}
