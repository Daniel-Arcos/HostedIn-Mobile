package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.BookingCallback;
import com.sdi.hostedin.data.callbacks.BookingsCallback;
import com.sdi.hostedin.data.datasource.remote.RemoteBookingsDataSource;
import com.sdi.hostedin.data.model.Booking;

import java.util.List;

public class BookingsRepository {
    RemoteBookingsDataSource remoteBookingsDataSource = new RemoteBookingsDataSource();

    public void getBookingsOfSpecificAccommodation(String accommodationId, String token, BookingsCallback bookingsListCallback){
        remoteBookingsDataSource.getBookingsListOfSpecificAccommodation(accommodationId, token, new BookingsCallback() {
            @Override
            public void onSuccess(List<Booking> bookingList, String message) {
                bookingsListCallback.onSuccess(bookingList, message);
            }

            @Override
            public void onError(String errorMessage) {
                bookingsListCallback.onError(errorMessage);
            }
        });
    }

    public void createBooking(Booking booking, String token, BookingCallback bookingCallback) {
        remoteBookingsDataSource.createBooking(booking, token, new BookingCallback() {
            @Override
            public void onSuccess(Booking booking, String token) {
                bookingCallback.onSuccess(booking, token);
            }

            @Override
            public void onError(String errorMessage) {
                bookingCallback.onError(errorMessage);
            }
        });
    }
}
