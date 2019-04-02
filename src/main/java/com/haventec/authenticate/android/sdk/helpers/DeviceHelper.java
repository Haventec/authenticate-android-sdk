package com.haventec.authenticate.android.sdk.helpers;

import android.os.Build;

import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;

public class DeviceHelper {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if ( manufacturer == null || model == null ) {
            throw new HaventecAuthenticateException(AuthenticateError.DEVICE_INIT_ERROR);
        }

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }}
