package com.Quadranet.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DNAPedAPI {
    @POST("GetPedConnection")
    Call<DNAPedResult> getPedURL(@Query("SerialNumber") String SerialNumber, @Query("IpAddress") String IpAddress);

    //String BASE_URL = "https://dbxlive.quadranet.co.uk/Interfaces/API/DNAPayments/";
    //String BASE_URL = "https://dbxdev.quadranet.co.uk/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://qsllp016:888/Interfaces/API/DNAPayments/";
    String BASE_URL = "http://192.168.8.181:999/Interfaces/API/DNAPayments/";
}
