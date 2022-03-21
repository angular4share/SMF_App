package com.shree.maulifoods.utility;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RESTApi {

    private static String BASE_URL = "http://192.168.179.51:82/api/";
    //private static String BASE_URL = "http://202.21.34.204:8080/SMF/api/";
    //private static String BASE_URL = "http://groupsurya.com:8080/SMF/api/";

    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL).client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create()) // new for string conversion
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build();
        }
        return retrofit;
    }

}