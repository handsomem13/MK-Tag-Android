package com.moko.bxp.sethala.models;

import java.util.ArrayList;

public class BaseBeaconSettings {
    private String ConfigurationId;
    private String AssetId;
    ArrayList < SlotSettings > Slots = new ArrayList< SlotSettings >();
    FirmwareSettings Firmware;
    AccelerometerSettings Accelerometer;
    private String Status;
    private String CreatedDateTimeStamp;
    private String LastDateTimeStamp;
    private boolean IsDeleted;


    // Getter Methods
    public ArrayList<SlotSettings> getSlots(){
        return  Slots;
    }
    public String getConfigurationId() {
        return ConfigurationId;
    }

    public String getAssetId() {
        return AssetId;
    }

    public FirmwareSettings getFirmware() {
        return Firmware;
    }

    public AccelerometerSettings getAccelerometer() {
        return Accelerometer;
    }

    public String getStatus() {
        return Status;
    }

    public String getCreatedDateTimeStamp() {
        return CreatedDateTimeStamp;
    }

    public String getLastDateTimeStamp() {
        return LastDateTimeStamp;
    }

    public boolean getIsDeleted() {
        return IsDeleted;
    }

    // Setter Methods

    public void setConfigurationId(String ConfigurationId) {
        this.ConfigurationId = ConfigurationId;
    }

    public void setAssetId(String AssetId) {
        this.AssetId = AssetId;
    }

    public void setFirmware(FirmwareSettings FirmwareObject) {
        this.Firmware = FirmwareObject;
    }

    public void setAccelerometer(AccelerometerSettings AccelerometerObject) {
        this.Accelerometer = AccelerometerObject;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setCreatedDateTimeStamp(String CreatedDateTimeStamp) {
        this.CreatedDateTimeStamp = CreatedDateTimeStamp;
    }

    public void setLastDateTimeStamp(String LastDateTimeStamp) {
        this.LastDateTimeStamp = LastDateTimeStamp;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }
}
