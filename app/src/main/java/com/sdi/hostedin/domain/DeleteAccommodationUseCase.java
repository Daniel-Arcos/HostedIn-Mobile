package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.PasswordCodeCallback;
import com.sdi.hostedin.data.repositories.AccommodationsRepository;

public class DeleteAccommodationUseCase {
    private AccommodationsRepository accommodationsRepository = new AccommodationsRepository();

    public void deleteAccommodation(String accommodationId, String token, PasswordCodeCallback passwordCodeCallback){
        accommodationsRepository.deleteAcommodation(accommodationId, token, new PasswordCodeCallback() {
            @Override
            public void onSucces(String token) {
                passwordCodeCallback.onSucces(token);
            }

            @Override
            public void onError(String errorMessage) {
                passwordCodeCallback.onError(errorMessage);
            }
        });
    }


}
