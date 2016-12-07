package com.collecdoo.fragment;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String API_BASE_URL = "http://api.collecdoo.com/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create());


    public static <S> S createService(Class<S> serviceClass) {

        httpClient.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);


        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(WrapperInterface.class   , new IWrapperAdapter());
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(API_BASE_URL);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(builder.create()));

        OkHttpClient okHttpClient = httpClient.build();

        Retrofit retrofit = retrofitBuilder.client(okHttpClient).build();
        return retrofit.create(serviceClass);
    }

    class BaseResponse {
        JsonArray data;
    }


}