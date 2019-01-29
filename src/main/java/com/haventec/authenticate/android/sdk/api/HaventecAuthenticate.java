package com.haventec.authenticate.android.sdk.api;

import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.common.android.sdk.api.HaventecCommon;
import com.haventec.common.android.sdk.api.exceptions.HaventecCommonException;
import com.haventec.common.android.sdk.helpers.HashingHelper;

public class HaventecAuthenticate {

    public static byte[] generateSalt() throws HaventecAuthenticateException {
        try {
            return HaventecCommon.generateSalt();
        } catch (HaventecCommonException e) {
            throw new HaventecAuthenticateException(AuthenticateError.HAVENTEC_COMMON_ERROR, e);
        }
    }

    public static String hashPin(String pin, byte[] salt) throws HaventecAuthenticateException {
        try {
            return HashingHelper.createHash(pin, salt);
        } catch (HaventecCommonException e) {
            throw new HaventecAuthenticateException(AuthenticateError.HAVENTEC_COMMON_ERROR, e);
        }
    }
}
