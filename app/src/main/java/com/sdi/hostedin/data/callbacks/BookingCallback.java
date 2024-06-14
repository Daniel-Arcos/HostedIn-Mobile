package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Booking;

public interface BookingCallback {
    void onSuccess(Booking booking, String newToken);
    void onError(String errorMessage, String newToken);
}
