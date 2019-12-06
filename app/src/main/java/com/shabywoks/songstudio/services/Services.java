package com.shabywoks.songstudio.services;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public class Services {
    public static StudioAPI studioAPI = AppRetrofitClient.getRetrofitInstance()
                                                            .create(StudioAPI.class);
}
