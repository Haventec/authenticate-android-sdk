package com.haventec.authenticate.android.sdk.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.haventec.authenticate.android.sdk.R;
import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.authenticate.android.sdk.models.HaventecAuthenticateResponse;
import com.haventec.authenticate.android.sdk.models.HaventecData;
import com.haventec.authenticate.android.sdk.models.Token;
import com.haventec.common.android.sdk.helpers.HashingHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class StorageHelper {

    private static HaventecData data;

    public static void initialise(Context context, String username) throws HaventecAuthenticateException {

        try {
            SharedPreferences sharedPref = getSharedPreferences(context, username);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), username);

            // Don't overwrite an existing salt for this username
            String saltBase64 = sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), null);
            if ( saltBase64 == null ) {
                byte[] salt = HashingHelper.generateRandomSaltBytes();
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), HashingHelper.toBase64(salt));
            }

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

    public static void update(Context context, JSONObject jsonObject) throws HaventecAuthenticateException {
        HaventecData data = new HaventecData();

        String applicationUuid = getJSONString(jsonObject,"applicationUuid");
        if ( applicationUuid != null ) {
            data.setApplicationUuid(applicationUuid);
        }
        String userUuid = getJSONString(jsonObject,"userUuid");
        if ( userUuid != null ) {
            data.setUserUuid(userUuid);
        }
        String deviceUuid = getJSONString(jsonObject,"deviceUuid");
        if ( deviceUuid != null ) {
            data.setDeviceUuid(deviceUuid);
        }
        String deviceName = getJSONString(jsonObject,"deviceName");
        if ( deviceName != null ) {
            data.setDeviceName(deviceName);
        }
        String authKey = getJSONString(jsonObject,"authKey");
        if ( authKey != null ) {
            data.setAuthKey(authKey);
        }

        JSONObject tokenJson = getJSONObject(jsonObject, "accessToken");
        if ( tokenJson != null ) {
            data.setAccessToken(getJSONString(tokenJson,"token"));
            data.setTokenType(getJSONString(tokenJson,"type"));
        }

        update(context, data);
    }

    public static void update(Context context, HaventecAuthenticateResponse response) throws HaventecAuthenticateException {

        try {
            SharedPreferences sharedPref = getSharedPreferences(context);

            SharedPreferences.Editor editor = sharedPref.edit();

            if ( response.getApplicationUuid() != null && !response.getApplicationUuid().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_applicationuuid), response.getApplicationUuid());
            }
            if ( response.getUsername() != null && !response.getUsername().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), response.getUsername());
            }
            if ( response.getUserUuid() != null && !response.getUserUuid().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_useruuid), response.getUserUuid());
            }
            if ( response.getDeviceName() != null && !response.getDeviceName().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_devicename), response.getDeviceName());
            }
            if ( response.getDeviceUuid() != null && !response.getDeviceUuid().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_deviceuuid), response.getDeviceUuid());
            }
            if ( response.getAuthKey() != null && !response.getAuthKey().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_authkey), response.getAuthKey());
            }
            if ( response.getToken() != null && response.getToken().getAccessToken() != null && !response.getToken().getAccessToken().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_accesstoken), response.getToken().getAccessToken());
            }
            if ( response.getToken() != null && response.getToken().getType() != null && !response.getToken().getType().isEmpty() ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_token_type), response.getToken().getType());
            }
            if ( response.getSalt() != null && response.getSalt().length > 0 ) {
                editor.putString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), HashingHelper.toBase64(response.getSalt()));
            }

            editor.commit();

        } catch (Exception e) {
            throw new HaventecAuthenticateException(AuthenticateError.STORAGE_ERROR, e);
        }
    }


    public static HaventecData getData(Context context) throws HaventecAuthenticateException {
        try {
            SharedPreferences sharedPref = getSharedPreferences(context);

            if ( data == null ) {
                data = new HaventecData();
            }

            data.setApplicationUuid(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_applicationuuid), null));
            data.setUsername(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_username), null));
            data.setUserUuid(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_useruuid), null));
            data.setDeviceName(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_devicename), null));
            data.setDeviceUuid(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_deviceuuid), null));
            data.setAuthKey(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_authkey), null));
            data.setSalt(HashingHelper.fromBase64(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_salt), null)));

            Token token = new Token();
            token.setType(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_token_type), null));
            token.setAccessToken(sharedPref.getString(context.getString(com.haventec.authenticate.android.sdk.R.string.haventec_preference_accesstoken), null));

            data.setToken(token);

            return data;

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
