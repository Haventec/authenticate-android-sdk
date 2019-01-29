package com.haventec.testandroidsdk.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceDetails {
    private String deviceUuid;
    private boolean isLocked;
    private boolean isActive;
    private long dateCreated;
    private long lastLogin;
    private int failedLoginAttempts;
    private String deviceName;

    public DeviceDetails(JSONObject jsonObject) {
        try {
            deviceUuid = jsonObject.getString("deviceUuid");
            deviceName = jsonObject.getString("deviceName");
            isActive = jsonObject.getBoolean("active");
            isLocked = jsonObject.getBoolean("locked");
            dateCreated = jsonObject.getLong("dateCreated");
            lastLogin = jsonObject.getLong("lastLogin");
            failedLoginAttempts = jsonObject.getInt("failedLoginAttempts");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


}
