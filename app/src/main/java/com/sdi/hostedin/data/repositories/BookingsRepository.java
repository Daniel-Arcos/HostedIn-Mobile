package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.BookingCallBackStore;
import com.sdi.hostedin.data.datasource.remote.RemoteBookingsDataSource;
import com.sdi.hostedin.data.model.Booking;

import java.util.List;

public class BookingsRepository {
    RemoteBookingsDataSource remoteBookingsDataSource = new RemoteBookingsDataSource();

    public void getBookingsOfSpecificAccommodation(String accommodationId, BookingCallBackStore.BookingsListCallback bookingsListCallback){
        remoteBookingsDataSource.getBookingsListOfSpecificAccommodation(accommodationId, new BookingCallBackStore.BookingsListCallback() {
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

}
