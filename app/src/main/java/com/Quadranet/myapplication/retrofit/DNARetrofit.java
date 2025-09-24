package com.Quadranet.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DNARetrofit {

    @POST("GetBillPrintBySN")
    Call<EposResult> callEpos(@Query("SerialNumber") String SerialNumber);
    //String BASE_URL = "https://dbxqa3.quadranet.co.uk/Interfaces/API/DNAPayments/";
    //String BASE_URL = "https://dbxdemo.quadranet.co.uk/Interfaces/API/DNAPayments/";
    //String BASE_URL = "https://dbxdev.quadranet.co.uk/Interfaces/API/DNAPayments/";
    //String BASE_URL = "https://dbxlive.quadranet.co.uk/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://qsllp016:888/Interfaces/API/DNAPayments/";
   // String BASE_URL = "http://qsllp016:999/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://qsl-lap104:888/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://192.168.8.181:888/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://192.168.0.20:888/Interfaces/API/DNAPayments/";
    //String BASE_URL = "http://172.20.10.2:888/Interfaces/API/DNAPayments/";
   // String BASE_URL = "http://192.168.1.105:80/Interfaces/API/DNAPayments/";
    String BASE_URL = "http://192.168.18.8/Interfaces/API/DNAPayments/";


}
