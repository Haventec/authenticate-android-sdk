package com.haventec.authenticate.android.sdk.models;

public interface HaventecAuthenticateResponse {

    String getApplicationUuid();
    String getUsername();
    String getUserUuid();
    String getDeviceUuid();
    String getDeviceName();
    Token getToken();
    String getAuthKey();
    byte[] getSalt();
}
