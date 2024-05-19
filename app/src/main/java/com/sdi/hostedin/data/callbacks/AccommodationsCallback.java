package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Accommodation;

import java.util.List;

public interface AccommodationsCallback {
    void onSuccess(List<Accommodation> accommodations, String token);
    void onError(String errorMessage);
}
