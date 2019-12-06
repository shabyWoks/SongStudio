package com.shabywoks.songstudio.services;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public interface StudioAPI {

    @GET("/studio")
    public Call<JsonArray> getSongList();

}
