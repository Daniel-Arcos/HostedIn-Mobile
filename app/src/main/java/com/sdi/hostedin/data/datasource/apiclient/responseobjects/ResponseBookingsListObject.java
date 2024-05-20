package com.sdi.hostedin.data.datasource.apiclient.responseobjects;

import com.sdi.hostedin.data.model.Booking;
import com.squareup.moshi.Json;

import java.util.List;

public class ResponseBookingsListObject {
    private String message;

    @Json(name = "bookings")
    private BookingsWrapper bookingsWrapper;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking> getBookings() {
        return bookingsWrapper.bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookingsWrapper.bookings = bookings;
    }

    public static class BookingsWrapper {
        @Json(name = "bookings")
        private List<Booking> bookings;

        public List<Booking> getBookings() {
            return bookings;
        }

        public void setBookings(List<Booking> bookings) {
            this.bookings = bookings;
        }
    }
}
