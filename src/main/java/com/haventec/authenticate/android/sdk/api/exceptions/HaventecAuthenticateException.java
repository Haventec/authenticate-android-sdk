package com.haventec.authenticate.android.sdk.api.exceptions;


import com.haventec.authenticate.android.sdk.models.BaseResponse;
import com.haventec.authenticate.android.sdk.models.HaventecError;

public class HaventecAuthenticateException extends Exception {

    private String errorCode;

    public HaventecAuthenticateException() {
    }

    public HaventecAuthenticateException(BaseResponse baseResponse) {

        super(baseResponse.getResponseStatus().getMessage());
        this.errorCode = baseResponse.getResponseStatus().getCode();
    }

    public HaventecAuthenticateException(HaventecError haventecError, String additionalInfo) {

        super(haventecError.getMessage() + ": " + additionalInfo);
        this.errorCode = haventecError.getErrorCode();
    }

    public HaventecAuthenticateException(HaventecError haventecError, Throwable throwable) {

        super(haventecError.getMessage() + ": " + throwable.getMessage(), throwable);
        this.errorCode = haventecError.getErrorCode();
    }

    public HaventecAuthenticateException(HaventecError haventecError) {
        super(haventecError.getMessage());
        this.errorCode = haventecError.getErrorCode();
    }
}
