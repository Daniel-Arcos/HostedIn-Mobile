package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Accommodation;

public interface AccommodationCallback {
    void onSuccess(Accommodation accommodation, String successMessage, String newToken);
    void onError(String errorMessage, String newToken);
}
