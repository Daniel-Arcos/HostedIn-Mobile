package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.repositories.AccommodationsRepository;

import java.util.List;

public class GetAccommodationsUseCase {

    AccommodationsRepository accommodationsRepository = new AccommodationsRepository();
    public void getAllAccommodations(AccommodationsCallback accommodationsCallback) {
        accommodationsRepository.getAllAccommodations(new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }

}
