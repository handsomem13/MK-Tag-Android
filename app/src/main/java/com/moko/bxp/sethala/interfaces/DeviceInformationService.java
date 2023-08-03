package com.moko.bxp.sethala.interfaces;

import com.moko.bxp.sethala.models.DeviceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface DeviceInformationService {
    @GET("asset/getPendingUpdates")
    Call<DeviceModel> getDevices(@Header("Authorization") String authToken);
}
