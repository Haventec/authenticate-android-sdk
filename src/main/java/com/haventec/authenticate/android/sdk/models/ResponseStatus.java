package com.haventec.authenticate.android.sdk.models;

public class ResponseStatus {
    private String status;
    private String message;
    private String code;

    public ResponseStatus() {

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResponseStatus(HaventecError error) {
        this.status = "ERROR";
        this.code = error.getErrorCode();
        this.message = error.getMessage();
    }
}