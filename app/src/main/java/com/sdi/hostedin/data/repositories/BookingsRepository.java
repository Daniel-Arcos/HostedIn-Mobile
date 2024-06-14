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
            public void onSuccess(List<Booking> bookingList, String newToken) {
                bookingsListCallback.onSuccess(bookingList, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                bookingsListCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void createBooking(Booking booking, String token, BookingCallback bookingCallback) {
        remoteBookingsDataSource.createBooking(booking, token, new BookingCallback() {
            @Override
            public void onSuccess(Booking booking, String newToken) {
                bookingCallback.onSuccess(booking, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                bookingCallback.onError(errorMessage, newToken);
            }
        });
    }
}
