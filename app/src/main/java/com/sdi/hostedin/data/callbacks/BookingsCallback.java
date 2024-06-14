package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Booking;

import java.util.List;

public interface BookingsCallback {
        void onSuccess(List<Booking> bookingList, String newToken);
        void onError(String errorMessage, String newToken);

}
