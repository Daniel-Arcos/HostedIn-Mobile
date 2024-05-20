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

    public void getAllAccommodationsExceptUserAccommodations(String idUser, AccommodationsCallback accommodationsCallback) {
        accommodationsRepository.getAllAccommodationsExceptUserAccommodations(idUser, new AccommodationsCallback() {
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


    public void getAllAccommodationsByLocationExceptUserAccommodations(String idUser, double lat, double lng, AccommodationsCallback accommodationsCallback) {
        accommodationsRepository.getAllAccommodationsByLocationExceptUserAccommodations(idUser, lat, lng, new AccommodationsCallback() {
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
