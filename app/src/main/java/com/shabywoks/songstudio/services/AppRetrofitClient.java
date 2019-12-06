package com.shabywoks.songstudio.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public class AppRetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://starlord.hackerearth.com";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
