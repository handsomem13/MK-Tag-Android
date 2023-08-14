package com.moko.bxp.sethala.models;

public class AccelerometerSettings {
    private float FullScale;
    private float SamplingRate;
    private float MotionThreshold;
    private String LastUpdatedDateTimeStamp;
    private String Status;
    private boolean StaticHeartbeatEnabled;
    private float StaticCycleTime;
    private float AdvDuration;


    // Getter Methods

    public float getFullScale() {
        return FullScale;
    }

    public float getSamplingRate() {
        return SamplingRate;
    }

    public float getMotionThreshold() {
        return MotionThreshold;
    }

    public String getLastUpdatedDateTimeStamp() {
        return LastUpdatedDateTimeStamp;
    }

    public String getStatus() {
        return Status;
    }

    public boolean getStaticHeartbeatEnabled() {
        return StaticHeartbeatEnabled;
    }

    public float getStaticCycleTime() {
        return StaticCycleTime;
    }

    public float getAdvDuration() {
        return AdvDuration;
    }

    // Setter Methods

    public void setFullScale(float FullScale) {
        this.FullScale = FullScale;
    }

    public void setSamplingRate(float SamplingRate) {
        this.SamplingRate = SamplingRate;
    }

    public void setMotionThreshold(float MotionThreshold) {
        this.MotionThreshold = MotionThreshold;
    }

    public void setLastUpdatedDateTimeStamp(String LastUpdatedDateTimeStamp) {
        this.LastUpdatedDateTimeStamp = LastUpdatedDateTimeStamp;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setStaticHeartbeatEnabled(boolean StaticHeartbeatEnabled) {
        this.StaticHeartbeatEnabled = StaticHeartbeatEnabled;
    }

    public void setStaticCycleTime(float StaticCycleTime) {
        this.StaticCycleTime = StaticCycleTime;
    }

    public void setAdvDuration(float AdvDuration) {
        this.AdvDuration = AdvDuration;
    }
}
