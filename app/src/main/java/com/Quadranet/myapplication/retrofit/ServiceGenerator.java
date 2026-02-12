package com.Quadranet.myapplication.retrofit;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    //http add urlacl url=https://+:44317/ user=everyone

    //public static final String API_BASE_URL = "https://dbxqa3.quadranet.co.uk/Interfaces/API/DNAPayments/"; //insert_ip_here
   // public static final String API_BASE_URL = "https://dbxdemo.quadranet.co.uk/Interfaces/API/DNAPayments/"; //insert_ip_here
  //  public static final String API_BASE_URL = "https://dbxdev.quadranet.co.uk/Interfaces/API/DNAPayments/"; //insert_ip_here
    public static final String API_BASE_URL = "https://dbxlive.quadranet.co.uk/Interfaces/API/DNAPayments/"; //insert_ip_here
    //public static final String API_BASE_URL = "http://qsllp016:888/Interfaces/API/DNAPayments/"; //insert_ip_here
    //public static final String API_BASE_URL = "http://qsllp016:999/Interfaces/API/DNAPayments/"; //insert_ip_here
    //public static final String API_BASE_URL = "http://qsl-lap104:888/Interfaces/API/DNAPayments/"; //insert_ip_her
    //public static final String API_BASE_URL = "http://192.168.0.20:888/Interfaces/API/DNAPayments/"; //insert_ip_her
    //public static final String API_BASE_URL = "http://172.20.10.2:888/Interfaces/API/DNAPayments/"; //insert_ip_her
    //public static final String API_BASE_URL = "http://192.168.1.105:80/Interfaces/API/DNAPayments/"; //insert_ip_her
    //public static final String API_BASE_URL = "http://192.168.18.8/Interfaces/API/DNAPayments/"; //insert_ip_her


    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }



}
