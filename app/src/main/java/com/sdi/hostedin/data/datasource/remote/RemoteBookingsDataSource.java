package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.BookingsCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookingsListObject;
import com.sdi.hostedin.data.model.Booking;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteBookingsDataSource {
    public RemoteBookingsDataSource() {
    }

    ApiClient.Service service = ApiClient.getInstance().getService();

    public  void getBookingsListOfSpecificAccommodation(String accommodationId, BookingsCallback bookingsListCallback) {
        Call<ResponseBookingsListObject> call = service.getBookingsOfSpecificAccommodation(accommodationId);

        call.enqueue(new Callback<ResponseBookingsListObject>() {
            @Override
            public void onResponse(Call<ResponseBookingsListObject> call, Response<ResponseBookingsListObject> response) {
                if (response.isSuccessful()) {
                    ResponseBookingsListObject responseBookingsListObject = response.body();
                    ArrayList<Booking> bookingsRecovered = new ArrayList<>();
                    for (Booking book: responseBookingsListObject.getBookings() ) {
                        Booking newBooking = new Booking(
                                book.get_id(),
                                book.getAccommodationId(),
                                book.getBeginningDate(),
                                book.getEndingDate(),
                                book.getNumberOfGuests(),
                                book.getTotalCost(),
                                book.getBookingStatus(),
                                book.getGuestUserId(),
                                book.getGuestName(),
                                book.getHostName());
                        bookingsRecovered.add(newBooking);
                    }
                    bookingsListCallback.onSuccess(bookingsRecovered, response.message());
                } else {
                    String message = "Ocurrio un error al actualizar";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    bookingsListCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseBookingsListObject> call, Throwable t) {
                bookingsListCallback.onError(t.getMessage());
            }
        });
    }
}
