package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.GuestBooking;

import java.util.List;

public interface GuestBookedAccommodationCallBack {
    void onSuccess(List<GuestBooking> accommodations, String message);
    void onError(String errorMessage);
}
