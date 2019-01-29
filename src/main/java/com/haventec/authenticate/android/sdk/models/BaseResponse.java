package com.haventec.authenticate.android.sdk.models;

public class BaseResponse {
    private ResponseStatus responseStatus;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public BaseResponse() {
        this.responseStatus = new ResponseStatus();
        this.responseStatus.setStatus("");
        this.responseStatus.setMessage("");
        this.responseStatus.setCode("");
    }

    public BaseResponse(String status) {
        this.responseStatus = new ResponseStatus();

        this.responseStatus.setStatus(status);
        this.responseStatus.setMessage("");
        this.responseStatus.setCode("");
    }

    public BaseResponse(String status, String message) {
        this.responseStatus = new ResponseStatus();

        this.responseStatus.setStatus(status);
        this.responseStatus.setMessage(message);
        this.responseStatus.setCode("");
    }

    public BaseResponse(String status, String message, String code) {
        this.responseStatus = new ResponseStatus();

        this.responseStatus.setStatus(status);
        this.responseStatus.setMessage(message);
        this.responseStatus.setCode(code);
    }

    public void setStatus(String status) {

        if (this.responseStatus == null) {
            this.responseStatus = new ResponseStatus();
        }

        this.responseStatus.setStatus(status);
    }

    public void setMessage(String message) {

        if (this.responseStatus == null) {
            this.responseStatus = new ResponseStatus();
        }

        this.responseStatus.setMessage(message);
    }

    public void setCode(String code) {

        if (this.responseStatus == null) {
            this.responseStatus = new ResponseStatus();
        }

        this.responseStatus.setCode(code);
    }
}