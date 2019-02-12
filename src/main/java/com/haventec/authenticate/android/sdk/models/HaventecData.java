package com.haventec.authenticate.android.sdk.models;

public class HaventecData implements HaventecAuthenticateResponse {

    byte[] salt;
    String applicationUuid;
    String userUuid;
    String username;
    String deviceName;
    String deviceUuid;
    String authKey;
    Token token;

    public String getApplicationUuid() {
        return applicationUuid;
    }

    public void setApplicationUuid(String applicationUuid) {
        this.applicationUuid = applicationUuid;
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
        if ( this.token == null ) {
            this.token = new Token();
        }

        this.token.setAccessToken(accessToken);
    }
    public void setTokenType(String tokenType) {
        if ( this.token == null ) {
            this.token = new Token();
        }

        this.token.setType(tokenType);
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
