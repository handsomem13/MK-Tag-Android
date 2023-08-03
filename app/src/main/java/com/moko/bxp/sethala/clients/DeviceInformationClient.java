package com.moko.bxp.sethala.clients;
import android.content.Context;

import com.moko.bxp.sethala.interfaces.DeviceInformationService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DeviceInformationClient {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getAbsoluteUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static DeviceInformationService getApiService() {
        return retrofit.create(DeviceInformationService.class);
    }
    private static String getAbsoluteUrl() {
        return Configuration.BaseUrl ;
    }

}
