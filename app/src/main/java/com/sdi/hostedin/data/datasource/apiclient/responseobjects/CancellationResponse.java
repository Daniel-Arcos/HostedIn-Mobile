package com.sdi.hostedin.data.datasource.apiclient.responseobjects;

import com.sdi.hostedin.data.model.Cancellation;

public class CancellationResponse {

    private Cancellation cancellation;
    private String message;

    public Cancellation getCancellation() {
        return cancellation;
    }

    public void setCancellation(Cancellation cancellation) {
        this.cancellation = cancellation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
