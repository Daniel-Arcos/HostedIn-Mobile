package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.BookingCallback;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.repositories.BookingsRepository;

public class CreateBookingUseCase {
    BookingsRepository bookingsRepository = new BookingsRepository();

    public void createBooking(Booking booking, String token, BookingCallback bookingCallback) {
        bookingsRepository.createBooking(booking, token, new BookingCallback() {
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
