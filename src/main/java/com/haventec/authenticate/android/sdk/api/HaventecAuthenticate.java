package com.haventec.authenticate.android.sdk.api;

import android.app.Application;
import android.content.Context;

import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.authenticate.android.sdk.helpers.StorageHelper;
import com.haventec.authenticate.android.sdk.models.HaventecAuthenticateResponse;
import com.haventec.authenticate.android.sdk.models.HaventecData;
import com.haventec.common.android.sdk.api.HaventecCommon;
import com.haventec.common.android.sdk.api.exceptions.HaventecCommonException;

import org.json.JSONObject;

public class HaventecAuthenticate {

    /**
     *  It creates a Hash of the pin, along with the salt that is in Storage
     *
     * @param pin
     * @return String Base64-encoded representation of the SHA-512 hashed pin and salt
     * @throws HaventecCommonException
     */
    public static String hashPin(Context context, String pin) throws HaventecAuthenticateException {

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
     *
     * @return void
     */
    public static void initialiseStorage(Context context, String username) throws HaventecAuthenticateException {
        StorageHelper.initialise(context, username);
    }

    /**
     * It updates Haventec data storage for the username with the haventecAuthenticateResponse data
     *
     * @return void
     */
    public static void updateStorage(Context context, HaventecAuthenticateResponse haventecAuthenticateResponse) throws HaventecAuthenticateException {
        StorageHelper.update(context, haventecAuthenticateResponse);
    }

    /**
     * It updates Haventec data storage for the username with the JSON data
     *
     * @return void
     */
    public static void updateStorage(Context context, JSONObject jsonObject) throws HaventecAuthenticateException {
        StorageHelper.update(context, jsonObject);
    }

    /**
     * It retrieves the Haventec data in its current persisted state
     *
     * @return void
     */
    public static HaventecData getData(Context context) throws HaventecAuthenticateException {
        return StorageHelper.getData(context);
    }

    /**
     * It retrieves the Haventec JWT token in its current persisted state
     *
     * @return void
     */
    public static String getAccessToken(Context context) throws HaventecAuthenticateException {
        return StorageHelper.getData(context).getToken().getAccessToken();
    }

    /**
     * It retrieves the Haventec authKey in its current persisted state
     *
     * @return void
     */
    public static String getAuthKey(Context context) throws HaventecAuthenticateException {
        return StorageHelper.getData(context).getAuthKey();
    }

    /**
     * It retrieves the currently provisioned Haventec username in its current persisted state
     *
     * @return void
     */
    public static String getUsername(Context context) throws HaventecAuthenticateException {
        return StorageHelper.getData(context).getUsername();
    }

    /**
     * It retrieves the Haventec deviceuuid in its current persisted state
     *
     * @return void
     */
    public static String getDeviceUuid(Context context) throws HaventecAuthenticateException {
        return StorageHelper.getData(context).getDeviceUuid();
    }

    /**
     * It retrieves the Haventec userUuid in its current persisted state
     *
     * @return void
     */
    public static String getUserUuid(Context context) throws HaventecAuthenticateException {
        return StorageHelper.getData(context).getUserUuid();
    }
}
