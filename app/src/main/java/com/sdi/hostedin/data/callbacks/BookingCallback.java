package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Booking;

public interface BookingCallback {
    void onSuccess(Booking booking, String token);
    void onError(String errorMessage);
}
