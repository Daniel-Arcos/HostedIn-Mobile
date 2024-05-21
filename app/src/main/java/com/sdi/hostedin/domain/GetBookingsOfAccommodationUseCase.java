package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.BookingsCallback;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.repositories.BookingsRepository;

import java.util.List;

public class GetBookingsOfAccommodationUseCase {
    private BookingsRepository bookingsRepository = new BookingsRepository();

    public void getBookingsOfSpecificAccommodation(String accommodationId, String token, BookingsCallback bookingsListCallback){
        bookingsRepository.getBookingsOfSpecificAccommodation(accommodationId, token, new BookingsCallback() {
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
