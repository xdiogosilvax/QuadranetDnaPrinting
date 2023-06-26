package com.Quadranet.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DNAPedAPI {
    @POST("GetPedConnection")
    Call<DNAPedResult> getPedURL(@Query("SerialNumber") String SerialNumber);
    String BASE_URL = "https://dbxqa3.quadranet.co.uk/Interfaces/API/DNAPayments/";
}
