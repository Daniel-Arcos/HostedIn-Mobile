package com.sdi.hostedin.data.datasource.apiclient.responseobjects;

import com.sdi.hostedin.data.model.BookedAccommodation;

import java.util.List;

public class ResponseBookedAccommodation {
    private String message;
    private List<BookedAccommodation> accommodations;

    public ResponseBookedAccommodation() {
    }

    public ResponseBookedAccommodation(String message, List<BookedAccommodation> accommodations) {
        this.message = message;
        this.accommodations = accommodations;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BookedAccommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<BookedAccommodation> accommodations) {
        this.accommodations = accommodations;
    }
}
