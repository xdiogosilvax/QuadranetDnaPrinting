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

    public static final String API_BASE_URL = "https://dbxqa3.quadranet.co.uk/Interfaces/API/DNAPayments/"; //insert_ip_here

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
