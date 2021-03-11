package com.haventec.authenticate.android.sdk.api;

import android.content.Context;

import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.authenticate.android.sdk.helpers.DeviceHelper;
import com.haventec.authenticate.android.sdk.helpers.StorageHelper;
import com.haventec.authenticate.android.sdk.helpers.TokenHelper;
import com.haventec.common.android.sdk.api.HaventecCommon;
import com.haventec.common.android.sdk.api.exceptions.HaventecCommonException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HaventecAuthenticate {

    /**
     *  It creates a Hash of the pin, along with the salt that is in Storage
     *
     * @param pin The chosen PIN in plain text
     * @return String Base64-encoded representation of the SHA-512 hashed pin and salt
     */
    public static String hashPin(String pin) {

        if ( StorageHelper.getData() == null || StorageHelper.getData().getSalt() == null ) {
            throw new HaventecAuthenticateException(AuthenticateError.NOT_INITIALIZED);
        }

        byte[] salt = StorageHelper.getData().getSalt();
        try {
            return HaventecCommon.hashPin(pin, salt);
        } catch (HaventecCommonException e) {
            throw new HaventecAuthenticateException(AuthenticateError.HAVENTEC_COMMON_ERROR, e);
        }
    }

    /**
     * It initialises Haventec data storage for the username
     * @param context of the application
     * @param username The haventec username of the app user
     */
    public static void initialiseStorage(Context context, String username) {
        StorageHelper.initialise(context, username);
    }

    /**
     * It updates Haventec data storage for the username with the JSON data
     *
     * @param context of the application
     * @param jsonObject the response from the App backend with Haventec details
     */
    public static void updateStorage(Context context, JSONObject jsonObject) {
        StorageHelper.update(context, jsonObject);
    }

    /**
     * It retrieves the Haventec JWT token in its current persisted state
     *
     * @return The current session access token of the authenticated user
     */
    public static String getAccessToken() {
        return StorageHelper.getData().getToken().getAccessToken();
    }

    /**
     * It nulls the JWT accessToken data. This can be executed as part of a logout user flow,
     * to ensure no further transactions can be executed.
     * A subsequent updateStorage invocation on the response of a successful login will update the data.
     *
     * @return The current session access token of the authenticated user
     */
    public static void clearAccessToken() {
        StorageHelper.clearAccessToken();
    }

    /**
     * It retrieves the Haventec authKey in its current persisted state
     *
     * @return The rolling Haventec authKey linked to the current Haventec deviceUuid
     */
    public static String getAuthKey() {
        return StorageHelper.getData().getAuthKey();
    }

    /**
     * It retrieves the Haventec userUuid in its current persisted state
     *
     * @return The Haventec deviceUuid of the current user
     */
    public static String getUserUuid() {
        return TokenHelper.getUserUuidFromJWT(getAccessToken());
    }

    /**
     * It retrieves the currently provisioned Haventec username in its current persisted state
     *
     * @return The Haventec username of the current user
     */
    public static String getUsername() {
        return StorageHelper.getData().getUsername();
    }

    /**
     * It retrieves the Haventec deviceUuid in its current persisted state
     *
     * @return The Haventec deviceUuid of the current user
     */
    public static String getDeviceUuid() {
        return StorageHelper.getData().getDeviceUuid();
    }

    /**
     * It retrieves the device name, as defined by the android.os.Build.MANUFACTURER/android.os.Build.MODEL value.
     *
     * @return The deviceName of the current user
     */
    public static String getDeviceName() {
        return DeviceHelper.getDeviceName();
    }

    /**
     *  This allows the functionality to rotate or regenerate the Salt used to hash a device PIN.
     *
     * @param context of the application
     * @param username The haventec username of the app user
     * @throws UnsupportedEncodingException if encoding the salt fails
     */
    public static void regenerateSalt(Context context, String username) throws UnsupportedEncodingException {
        StorageHelper.regenerateSalt(context, username);
    }
}
