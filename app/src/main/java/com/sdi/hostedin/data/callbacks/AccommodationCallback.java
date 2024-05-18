package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Accommodation;

public interface AccommodationCallback {
    void onSuccess(Accommodation accommodation, String token);
    void onError(String errorMessage);
}
