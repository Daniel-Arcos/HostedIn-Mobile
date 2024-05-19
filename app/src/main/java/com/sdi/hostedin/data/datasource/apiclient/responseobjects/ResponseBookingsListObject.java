package com.sdi.hostedin.data.datasource.apiclient.responseobjects;

import com.sdi.hostedin.data.model.Booking;

import java.util.List;

public class ResponseBookingsListObject {
    private List<Booking> bookingsList;

    public ResponseBookingsListObject() {
    }

    public ResponseBookingsListObject(List<Booking> bookingsList) {

        this.bookingsList = bookingsList;
    }


    public List<Booking> getBookingsList() {
        return bookingsList;
    }

    public void setBookingsList(List<Booking> bookingsList) {
        this.bookingsList = bookingsList;
    }
}
