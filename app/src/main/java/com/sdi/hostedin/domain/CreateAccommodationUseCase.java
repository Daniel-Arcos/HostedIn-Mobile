package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.repositories.AccommodationsRepository;

public class CreateAccommodationUseCase {

    AccommodationsRepository accommodationsRepository = new AccommodationsRepository();

    public void createAccommodation(Accommodation accommodation, String token, AccommodationCallback accommodationCallback) {
        accommodationsRepository.createAccommodation(accommodation, token, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String message, String newToken) {
                accommodationCallback.onSuccess(accommodation, message, newToken);
            }

            @Override
            public void onError(String errorMessage,String newToken) {
                accommodationCallback.onError(errorMessage, newToken );
            }
        });

    }
}
