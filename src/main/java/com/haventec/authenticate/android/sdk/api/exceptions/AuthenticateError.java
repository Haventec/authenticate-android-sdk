package com.haventec.authenticate.android.sdk.api.exceptions;

import com.haventec.common.android.sdk.models.HaventecError;

public enum AuthenticateError implements HaventecError {

    /**
     * General Errors
     */
    NOT_INITIALIZED("AS-INIT-1001", "the initialize function has not been called for the user"),

    /**
     * Haventec Common Errors
     */
    HAVENTEC_COMMON_ERROR("AS-COMM-1001", "Haventec Common Error"),

    /**
     * Storage Errors
     */
    STORAGE_ERROR("CM-STOR-1001", "Storage Error"),

    /**
     * JSON Errors
     */
    JSON_ERROR("CM-JSON-1001", "JSON Error")
    ;



    private final String errorCode;
    private final String message;
    AuthenticateError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * It prints the error in a specific format.
     *
     * @return friendly String with information about the error code and the message
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
