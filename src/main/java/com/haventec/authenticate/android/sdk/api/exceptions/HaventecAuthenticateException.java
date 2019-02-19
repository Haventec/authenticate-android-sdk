package com.haventec.authenticate.android.sdk.api.exceptions;


import com.haventec.common.android.sdk.api.exceptions.HaventecException;
import com.haventec.common.android.sdk.models.HaventecError;

public class HaventecAuthenticateException extends HaventecException {

    public HaventecAuthenticateException(HaventecError haventecError, Throwable throwable) {
        super(haventecError, throwable);
    }

    public HaventecAuthenticateException(HaventecError haventecError) {
        super(haventecError);
    }
}
