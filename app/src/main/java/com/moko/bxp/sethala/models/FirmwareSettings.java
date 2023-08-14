package com.moko.bxp.sethala.models;

import androidx.annotation.Nullable;

public class FirmwareSettings {
    @Nullable
    private String Url;
    @Nullable
    private String Label;
    @Nullable
    private String Version;
    @Nullable
    private String Password;
    @Nullable
    private String LastUpdatedDateTimeStamp;
    @Nullable
    private String Status;


    // Getter Methods

    public String getUrl() {
        return Url;
    }

    public String getLabel() {
        return Label;
    }

    public String getVersion() {
        return Version;
    }

    public String getPassword() {
        return Password;
    }

    public String getLastUpdatedDateTimeStamp() {
        return LastUpdatedDateTimeStamp;
    }

    public String getStatus() {
        return Status;
    }

    // Setter Methods

    public void setUrl(@Nullable String Url) {
        this.Url = Url;
    }

    public void setLabel(@Nullable String Label) {
        this.Label = Label;
    }

    public void setVersion(@Nullable String Version) {
        this.Version = Version;
    }

    public void setPassword(@Nullable String Password) {
        this.Password = Password;
    }

    public void setLastUpdatedDateTimeStamp(@Nullable String LastUpdatedDateTimeStamp) {
        this.LastUpdatedDateTimeStamp = LastUpdatedDateTimeStamp;
    }

    public void setStatus(@Nullable String Status) {
        this.Status = Status;
    }
}
