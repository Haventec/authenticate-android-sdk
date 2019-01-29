package com.haventec.testandroidsdk.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDetails {
    private String userUuid;
    private String username;
    private String email;
    private boolean isActive;
    private boolean isLocked;
    private long dateCreated;
    private long lastLogin;
    private String mobileNumber;

    public UserDetails(JSONObject jsonObject) {
        try {
            userUuid = jsonObject.getString("userUuid");
            username = jsonObject.getString("username");
            email = jsonObject.getString("email");
            isActive = jsonObject.getBoolean("active");
            isLocked = jsonObject.getBoolean("locked");
            dateCreated = jsonObject.getLong("dateCreated");
            lastLogin = jsonObject.getLong("lastLogin");
            mobileNumber = jsonObject.getString("mobileNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
