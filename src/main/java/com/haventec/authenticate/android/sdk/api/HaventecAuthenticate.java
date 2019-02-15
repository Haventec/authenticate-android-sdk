package com.haventec.authenticate.android.sdk.api;

import android.content.Context;

import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.authenticate.android.sdk.helpers.StorageHelper;
import com.haventec.common.android.sdk.api.HaventecCommon;
import com.haventec.common.android.sdk.api.exceptions.HaventecCommonException;

import org.json.JSONObject;

public class HaventecAuthenticate {

    /**
     *  It creates a Hash of the pin, along with the salt that is in Storage
     *
     * @param context of the application
     * @param pin The chosen PIN in plain text
     * @return String Base64-encoded representation of the SHA-512 hashed pin and salt
     */
    public static String hashPin(Context context, String pin) {

        if ( StorageHelper.getData(context) == null || StorageHelper.getData(context).getSalt() == null ) {
            throw new HaventecAuthenticateException(AuthenticateError.NOT_INITIALIZED);
        }

        byte[] salt = StorageHelper.getData(context).getSalt();
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
     * @param context of the application
     * @return The current session access token of the authenticated user
     */
    public static String getAccessToken(Context context) {
        return StorageHelper.getData(context).getToken().getAccessToken();
    }

    /**
     * It retrieves the Haventec authKey in its current persisted state
     *
     * @param context of the application
     * @return The rolling Haventec authKey linked to the current Haventec deviceUuid
     */
    public static String getAuthKey(Context context) {
        return StorageHelper.getData(context).getAuthKey();
    }

    /**
     * It retrieves the currently provisioned Haventec username in its current persisted state
     *
     * @param context of the application
     * @return The Haventec username of the current user
     */
    public static String getUsername(Context context) {
        return StorageHelper.getData(context).getUsername();
    }

    /**
     * It retrieves the Haventec deviceuuid in its current persisted state
     *
     * @param context of the application
     * @return The Haventec deviceUuid of the current user
     */
    public static String getDeviceUuid(Context context) {
        return StorageHelper.getData(context).getDeviceUuid();
    }
}
