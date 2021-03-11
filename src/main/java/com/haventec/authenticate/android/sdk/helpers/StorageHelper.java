package com.haventec.authenticate.android.sdk.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.haventec.authenticate.android.sdk.R;
import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.authenticate.android.sdk.models.HaventecData;
import com.haventec.common.android.sdk.helpers.HashingHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class StorageHelper {

    private static HaventecData haventecDataCache;

    public static void initialise(Context context, String username) {
        // Normalise the username to low letters in order to support username case insensitive
        String normaliseUsername = username.toLowerCase();

        try {
            // Set the normalised username as the current one
            setCurrentUser(context, normaliseUsername);

            // Initialise the user data at the Android Storage
            initialiseUserPersistedData(context, normaliseUsername);

            // Get the stored user data and keep it in memory
            initialiseUserCacheData(context, normaliseUsername);
        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }

    private static void initialiseUserPersistedData(Context context, String normaliseUsername)
            throws UnsupportedEncodingException {
        SharedPreferences sharedPref = getUserPreferences(context, normaliseUsername);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), normaliseUsername);

        // If the saltBits is null then we have to initialise it.
        String saltBase64 = sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), null);
        if (saltBase64 == null) {
            byte[] salt = HashingHelper.generateRandomSaltBytes();
            editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), HashingHelper.toBase64(salt));
        }
        editor.commit();
    }

    private static void initialiseUserCacheData(Context context, String normaliseUsername) {
        try {
            SharedPreferences sharedPref = getUserPreferences(context, normaliseUsername);

            haventecDataCache = new HaventecData();
            haventecDataCache.setUsername(normaliseUsername);
            haventecDataCache.setDeviceName(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_devicename), null));
            haventecDataCache.setDeviceUuid(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_deviceuuid), null));
            haventecDataCache.setAuthKey(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_authkey), null));
            haventecDataCache.setSalt(HashingHelper.fromBase64(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), null)));
        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }

    public static void update(Context context, JSONObject jsonObject) {
        try {
            SharedPreferences sharedPref = getCurrentUserPreferences(context);

            SharedPreferences.Editor editor = sharedPref.edit();

            // DeviceUuid
            String deviceUuid = JsonHelper.getJSONString(jsonObject, "deviceUuid");
            if (deviceUuid != null && !deviceUuid.isEmpty()) {
                haventecDataCache.setDeviceUuid(deviceUuid);
                editor.putString(context.getString(
                        com.haventec.authenticate.android.sdk.R.string.haventec_preference_deviceuuid),
                        deviceUuid);
            }

            // AuthKey
            String authKey = JsonHelper.getJSONString(jsonObject, "authKey");
            if (authKey != null && !authKey.isEmpty()) {
                haventecDataCache.setAuthKey(authKey);
                editor.putString(context.getString(
                        com.haventec.authenticate.android.sdk.R.string.haventec_preference_authkey),
                        authKey);
            }

            // AccessToken
            JSONObject tokenJson = JsonHelper.getJSONObject(jsonObject, "accessToken");
            if (tokenJson != null) {
                String accessToken = JsonHelper.getJSONString(tokenJson, "token");
                if (accessToken != null && !accessToken.isEmpty()) {
                    haventecDataCache.setAccessToken(accessToken);
                    // We don't persist the access token
                }

                // AccessToken type
                String accessTokenType = JsonHelper.getJSONString(tokenJson, "type");
                if (accessTokenType != null && !accessTokenType.isEmpty()) {
                    haventecDataCache.setTokenType(accessTokenType);
                    // We don't persist the token type
                }
            }

            editor.commit();

        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }

    public static HaventecData getData() {
            return haventecDataCache;
    }

    public static void clearAccessToken() {
        haventecDataCache.setAccessToken(null);
    }

    private static void setCurrentUser(Context context, String normaliseUsername) {
        SharedPreferences sharedPreferencesGlobal = context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesGlobal.edit();

        // Update current user
        editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_current_user), normaliseUsername);
        editor.commit();
    }

    private static SharedPreferences getCurrentUserPreferences(Context context) {
        SharedPreferences sharedPreferencesGlobal = context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key), Context.MODE_PRIVATE);
        String normaliseUsername = sharedPreferencesGlobal.getString(context.getString(R.string.haventec_preference_current_user), null);

        if (normaliseUsername == null) {
            throw new HaventecAuthenticateException(AuthenticateError.NOT_INITIALIZED);
        }
        return getUserPreferences(context, normaliseUsername);
    }

    private static SharedPreferences getUserPreferences(Context context, String normaliseUsername) {
        return context.getSharedPreferences(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_file_key) + "_" + normaliseUsername, Context.MODE_PRIVATE);
    }

    public static void regenerateSalt(Context context, String username) throws UnsupportedEncodingException {

        String normaliseUsername = username.toLowerCase();

        SharedPreferences sharedPref = getUserPreferences(context, normaliseUsername);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), normaliseUsername);

        byte[] salt = HashingHelper.generateRandomSaltBytes();
        editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), HashingHelper.toBase64(salt));
    }
}
