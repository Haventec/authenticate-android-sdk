package com.haventec.authenticate.android.sdk.models;

public class GenericResponse extends BaseResponse {

    public GenericResponse() {
        super();
    }

    public GenericResponse(String status) {
        super(status);
    }

    public GenericResponse(String status, String message) {
        super(status, message);
    }

    public GenericResponse(String status, String message, String code) {
        super(status, message, code);
    }
}