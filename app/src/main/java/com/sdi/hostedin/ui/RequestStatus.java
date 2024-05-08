package com.sdi.hostedin.ui;

public class RequestStatus {
    private RequestStatusValues requestStatus;
    private String message;

    public RequestStatus(RequestStatusValues requestStatus, String message) {
        this.requestStatus = requestStatus;
        this.message = message;
    }

    public RequestStatusValues getRequestStatus() {
        return requestStatus;
    }

    public String getMessage() {
        return message;
    }
}
