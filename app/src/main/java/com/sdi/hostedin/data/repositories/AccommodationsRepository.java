package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.datasource.remote.RemoteAccommodationsDataSource;
import com.sdi.hostedin.data.model.Accommodation;

public class AccommodationsRepository {

    private RemoteAccommodationsDataSource remoteAccommodationsDataSource = new RemoteAccommodationsDataSource();

    public void createAccommodation(Accommodation accommodation, AccommodationCallback accommodationCallback) {
        remoteAccommodationsDataSource.createAccommodation(accommodation, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String token) {
                accommodationCallback.onSuccess(accommodation, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationCallback.onError(errorMessage);
            }
        });
    }
}
