package com.moko.bxp.sethala.models;

public class SlotSettings {
    private String text;
    DataObject data;
    private String LastUpdatedDateTimeStamp;


    // Getter Methods

    public String getText() {
        return text;
    }

    public DataObject getData() {
        return data;
    }

    public String getLastUpdatedDateTimeStamp() {
        return LastUpdatedDateTimeStamp;
    }

    // Setter Methods

    public void setText(String text) {
        this.text = text;
    }

    public void setData(DataObject dataObject) {
        this.data = dataObject;
    }

    public void setLastUpdatedDateTimeStamp(String LastUpdatedDateTimeStamp) {
        this.LastUpdatedDateTimeStamp = LastUpdatedDateTimeStamp;
    }
}



