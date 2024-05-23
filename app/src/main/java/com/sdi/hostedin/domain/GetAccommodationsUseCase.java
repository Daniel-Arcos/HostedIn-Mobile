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
    public void getAllAccommodations(String token, AccommodationsCallback accommodationsCallback) {
        accommodationsRepository.getAllAccommodations(token, new AccommodationsCallback() {
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

    public void getAllAccommodationsExceptUserAccommodations(String idUser, String token, AccommodationsCallback accommodationsCallback) {
        accommodationsRepository.getAllAccommodationsExceptUserAccommodations(idUser, token, new AccommodationsCallback() {
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


    public void getAllAccommodationsByLocationExceptUserAccommodations(String idUser, double lat, double lng, String token, AccommodationsCallback accommodationsCallback) {
        accommodationsRepository.getAllAccommodationsByLocationExceptUserAccommodations(idUser, lat, lng, token, new AccommodationsCallback() {
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

    public void getAlHostOwnedAccommodations(String userId, String token, AccommodationsCallback accommodationsCallback){
        accommodationsRepository.getAlLHostOwnedAccommodations(userId, token, new AccommodationsCallback() {
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
        accommodationsRepository.getHostBookedAccommodations(userId, new BookedAccommodationsCallBack() {
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
