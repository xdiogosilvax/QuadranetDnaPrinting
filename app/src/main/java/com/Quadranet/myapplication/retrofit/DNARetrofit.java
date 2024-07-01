package com.Quadranet.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DNARetrofit {

    @POST("GetBillPrintBySN")
    Call<EposResult> callEpos(@Query("SerialNumber") String SerialNumber);
    //String BASE_URL = "https://dbxqa3.quadranet.co.uk/Interfaces/API/DNAPayments/";
    String BASE_URL = "https://dbxdemo.quadranet.co.uk/Login/Interfaces/API/DNAPayments/";
    //String BASE_URL = "https://dbxdev.quadranet.co.uk/Login/Interfaces/API/DNAPayments/";
     //String BASE_URL = "http://qsllp016:888/Login/Interfaces/API/DNAPayments/";
//    String BASE_URL = "http://qsllp016:999/Login/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://qsl-lap103:888/Interfaces/API/DNAPayments/";


}
