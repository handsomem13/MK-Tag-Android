package com.moko.bxp.sethala.interfaces;

import com.moko.bxp.sethala.models.BeaconSettingsApiModel;
import com.moko.bxp.sethala.models.DeviceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DeviceInformationService {
    @GET("asset/getPendingUpdates")
    Call<DeviceModel> getDevices(@Header("Authorization") String authToken);
    @GET("asset/getConfigurations/{macaddress}")
    Call<BeaconSettingsApiModel> getConfigurations(@Path("macaddress") String macaddress, @Header("Authorization") String authToken);
}
