package com.sdi.hostedin.data.datasource.apiclient.responseobjects;

import com.sdi.hostedin.data.model.GuestBooking;
import com.squareup.moshi.Json;

import java.util.List;

public class ResponseGuestBookedAccommodations {
    @Json(name = "bookings")
    private List<GuestBooking> accommodationsBooked;
    private String message;

    public ResponseGuestBookedAccommodations() {
    }

    public ResponseGuestBookedAccommodations(List<GuestBooking> accommodationsBooked, String message) {
        this.accommodationsBooked = accommodationsBooked;
        this.message = message;
    }

    public List<GuestBooking> getAccommodationsBooked() {
        return accommodationsBooked;
    }

    public void setAccommodationsBooked(List<GuestBooking> accommodationsBooked) {
        this.accommodationsBooked = accommodationsBooked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
