package com.moko.bxp.sethala.database;

import java.util.Date;

public class BeaconInformationModel {
    private String AssetId;
    private String AssetName;
    private String ThingName;
    private String MacAddress;
    private String Rssi;
    private String ThingId;
    private String SoftwareVersion;
    private String SettingsVersion;
    private String DateTimeStamp;
    private String LastUpdatedDateTimeStamp;
    private String IsUpToDate;
    private String Password;
    private String FirmwareUrl;
    public String getIsUpToDate() {
        return IsUpToDate;
    }

    public void setIsUpToDate(String isUpToDate) {
        IsUpToDate = isUpToDate;
    }

    public String getAssetId() {
        return AssetId;
    }

    public void setAssetId(String assetId) {
        AssetId = assetId;
    }

    public String getAssetName() {
        return AssetName;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getRssi() {
        return Rssi;
    }

    public void setRssi(String rssi) {
        Rssi = rssi;
    }

    public String getThingId() {
        return ThingId;
    }

    public void setThingId(String thingId) {
        ThingId = thingId;
    }

    public String getSoftwareVersion() {
        return SoftwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        SoftwareVersion = softwareVersion;
    }

    public String getSettingsVersion() {
        return SettingsVersion;
    }

    public void setSettingsVersion(String settingsVersion) {
        SettingsVersion = settingsVersion;
    }

    public String getDateTimeStamp() {
        return DateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        DateTimeStamp = dateTimeStamp;
    }

    public String getLastUpdatedDateTimeStamp() {
        return LastUpdatedDateTimeStamp;
    }

    public void setLastUpdatedDateTimeStamp(String lastUpdatedDateTimeStamp) {
        LastUpdatedDateTimeStamp = lastUpdatedDateTimeStamp;
    }

    public void setAssetName(String assetName) {
        AssetName = assetName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFirmwareUrl() {
        return FirmwareUrl;
    }

    public void setFirmwareUrl(String firmwareUrl) {
        FirmwareUrl = firmwareUrl;
    }

    public String getThingName() {
        return ThingName;
    }

    public void setThingName(String thingName) {
        ThingName = thingName;
    }
}
