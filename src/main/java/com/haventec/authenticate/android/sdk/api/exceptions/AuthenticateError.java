package com.haventec.authenticate.android.sdk.api.exceptions;

import com.haventec.authenticate.android.sdk.models.HaventecError;

public enum AuthenticateError implements HaventecError {
    /**
     * Encryption Errors
     */
    HAVENTEC_COMMON_ERROR("AN-COMM-1001", "Haventec Common Error")
    ;



    private final String errorCode;
    private final String message;
    AuthenticateError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * It prints the AutenticateError in a specific format.
     *
     * @return
     */
    public String toString() {
        return " ErrorCode=" + this.getErrorCode() + ", Message=" +this.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
