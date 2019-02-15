package com.haventec.authenticate.android.sdk.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.haventec.authenticate.android.sdk.R;
import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.authenticate.android.sdk.models.HaventecData;
import com.haventec.common.android.sdk.helpers.HashingHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class StorageHelper {

    private static HaventecData haventecDataCache;

    public static void initialise(Context context, String username) {

        try {
            SharedPreferences sharedPref = getSharedPreferences(context, username);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), username);

            // If the saltBits is null then we have to initialise it.
            String saltBase64 = sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), null);
            if ( saltBase64 == null ) {
                byte[] salt = HashingHelper.generateRandomSaltBytes();
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), HashingHelper.toBase64(salt));
            }

            // Get the stored haventecDataCache and keep it in memory
            haventecDataCache = getData(context);

            editor.commit();

        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }

    private static String getJSONString(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch ( JSONException je ) {
            return null;
        }
    }

    private static JSONObject getJSONObject(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONObject(name);
        } catch ( JSONException je ) {
            return null;
        }
    }

    public static void update(Context context, JSONObject jsonObject) {
        try {
            SharedPreferences sharedPref = getSharedPreferences(context);

            SharedPreferences.Editor editor = sharedPref.edit();

            // DeviceUuid
            String deviceUuid = getJSONString(jsonObject,"deviceUuid");
            if (deviceUuid != null && !deviceUuid.isEmpty() ) {
                haventecDataCache.setDeviceUuid(deviceUuid);
                editor.putString(context.getString(
                        com.haventec.authenticate.android.sdk.R.string.haventec_preference_deviceuuid),
                        deviceUuid);
            }

            // AutheKey
            String authKey = getJSONString(jsonObject,"authKey");
            if (authKey != null && !authKey.isEmpty() ) {
                haventecDataCache.setAuthKey(authKey);
                editor.putString(context.getString(
                        com.haventec.authenticate.android.sdk.R.string.haventec_preference_authkey),
                        authKey);
            }

            // AccessToken
            JSONObject tokenJson = getJSONObject(jsonObject, "accessToken");
            if ( tokenJson != null ) {
                String accessToken = getJSONString(tokenJson, "token");
                if (accessToken != null && !accessToken.isEmpty() ) {
                    haventecDataCache.setAccessToken(accessToken);
                    // We don't persist the access token
                }

                // AccessToken type
                String accessTokenType = getJSONString(tokenJson, "type");
                if (accessTokenType != null && !accessTokenType.isEmpty() ) {
                    haventecDataCache.setTokenType(accessTokenType);
                    // We don't persist the token type
                }
            }

            editor.commit();

        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }


    public static HaventecData getData(Context context) throws HaventecAuthenticateException {
        try {
            SharedPreferences sharedPref = getSharedPreferences(context);

            if ( haventecDataCache == null ) {
                haventecDataCache = new HaventecData();
            }

            haventecDataCache.setUsername(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), null));
            haventecDataCache.setDeviceName(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_devicename), null));
            haventecDataCache.setDeviceUuid(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_deviceuuid), null));
            haventecDataCache.setAuthKey(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_authkey), null));
            haventecDataCache.setSalt(HashingHelper.fromBase64(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), null)));

            return haventecDataCache;

        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }

    private static SharedPreferences getSharedPreferences(Context context, String username) {

        SharedPreferences sharedPreferencesGlobal = context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesGlobal.edit();
        editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_last_user), username);
        editor.commit();

        return context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key) + "_" + username, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences(Context context) throws HaventecAuthenticateException {

        SharedPreferences sharedPreferencesGlobal = context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key), Context.MODE_PRIVATE);
        String username = sharedPreferencesGlobal.getString(context.getString(R.string.haventec_preference_last_user), null);

        if ( username == null ) {
            throw new HaventecAuthenticateException(AuthenticateError.NOT_INITIALIZED);
        }
        return context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key) + "_" + username, Context.MODE_PRIVATE);
    }
}
