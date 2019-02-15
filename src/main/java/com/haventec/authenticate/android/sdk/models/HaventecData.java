package com.haventec.authenticate.android.sdk.models;

public class HaventecData {

    private byte[] salt;
    private String applicationUuid;
    private String userUuid;
    private String username;
    private String deviceName;
    private String deviceUuid;
    private String authKey;
    private Token token = new Token();

    public String getApplicationUuid() {
        return applicationUuid;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setAccessToken(String accessToken) {
        this.token.setAccessToken(accessToken);
    }
    public void setTokenType(String tokenType) {
        this.token.setType(tokenType);
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
