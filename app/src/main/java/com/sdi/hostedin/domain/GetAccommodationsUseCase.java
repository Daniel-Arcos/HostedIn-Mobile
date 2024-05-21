package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.callbacks.BookedAccommodationsCallBack;
import com.sdi.hostedin.data.callbacks.GuestBookedAccommodationCallBack;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.GuestBooking;
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

    public void getHostBookedAccommodations(String userId, BookedAccommodationsCallBack accommodationsCallback){
        accommodationsRepository.getHostBoookedAccommodations(userId, new BookedAccommodationsCallBack() {
            @Override
            public void onSuccess(List<BookedAccommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }

    public void getGuestBookedAccommodations(String userId, String bookingStatus, GuestBookedAccommodationCallBack accommodationsCallback){
        accommodationsRepository.getGuestBookedAccommodations(userId, bookingStatus,new GuestBookedAccommodationCallBack() {
            @Override
            public void onSuccess(List<GuestBooking> accommodations, String message) {
                accommodationsCallback.onSuccess(accommodations, message);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }

}
