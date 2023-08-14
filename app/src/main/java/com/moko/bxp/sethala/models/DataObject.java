package com.moko.bxp.sethala.models;

import androidx.annotation.Nullable;

public class DataObject {
    private @Nullable
    String SelectedFrame;
    private @Nullable int Interval;
    private @Nullable int Duration;
    private @Nullable int Standby;
    private @Nullable int Ranging;
    private @Nullable int TxPower;
    private @Nullable int StaticDuration;
    private @Nullable int StopAdvertisingSeconds;
    private @Nullable int AdvDuration;
    private @Nullable boolean Trigger;
    private @Nullable boolean IsStartAdvertising;
    private @Nullable boolean IsStopAdvertising;
    private @Nullable String TagId;
    private @Nullable String DeviceName;
    private @Nullable int Rssi1m;
    private @Nullable String Major;
    private @Nullable String Minor;
    private @Nullable String Instance;
    private @Nullable String iBeaconUUID;
    private @Nullable String Namespace;
    // Getter Methods

    @Nullable
    public String getNamespace() {
        return Namespace;
    }

    @Nullable
    public String getiBeaconUUID() {
        return iBeaconUUID;
    }

    public boolean isTrigger() {
        return Trigger;
    }

    public boolean isStartAdvertising() {
        return IsStartAdvertising;
    }

    public boolean isStopAdvertising() {
        return IsStopAdvertising;
    }

    public String getMajor() {
        return Major;
    }

    public String getMinor() {
        return Minor;
    }

    @Nullable
    public String getInstance() {
        return Instance;
    }

    public int getRssi1m() {
        return Rssi1m;
    }
    public String getSelectedFrame() {
        return SelectedFrame;
    }

    public int getInterval() {
        return Interval;
    }

    public int getDuration() {
        return Duration;
    }

    public int getStandby() {
        return Standby;
    }

    public int getRanging() {
        return Ranging;
    }

    public int getTxPower() {
        return TxPower;
    }

    public int getStaticDuration() {
        return StaticDuration;
    }

    public int getStopAdvertisingSeconds() {
        return StopAdvertisingSeconds;
    }

    public int getAdvDuration() {
        return AdvDuration;
    }

    public boolean getTrigger() {
        return Trigger;
    }

    public boolean getIsStartAdvertising() {
        return IsStartAdvertising;
    }

    public boolean getIsStopAdvertising() {
        return IsStopAdvertising;
    }

    public String getTagId() {
        return TagId;
    }

    public String getDeviceName() {
        return DeviceName;
    }


}
