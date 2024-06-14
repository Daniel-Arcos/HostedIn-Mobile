package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.BookingCallback;
import com.sdi.hostedin.data.callbacks.BookingsCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.moshiconverters.MoshiConverter;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookingObject;
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

    public  void getBookingsListOfSpecificAccommodation(String accommodationId, String token, BookingsCallback bookingsListCallback) {
        Call<ResponseBookingsListObject> call = service.getBookingsOfSpecificAccommodation(accommodationId, token);

        call.enqueue(new Callback<ResponseBookingsListObject>() {
            @Override
            public void onResponse(Call<ResponseBookingsListObject> call, Response<ResponseBookingsListObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Booking> bookings = MoshiConverter.convertAPIBookingsResponseToJavaObjects(response.body());
                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }
                    bookingsListCallback.onSuccess(bookings, token);
                } else {
                    String message = "Ocurrio un error al recuperar las reservaciones";

                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }

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
                    bookingsListCallback.onError(message, token);
                }
            }

            @Override
            public void onFailure(Call<ResponseBookingsListObject> call, Throwable t) {
                bookingsListCallback.onError(t.getMessage(), "");
            }
        });
    }

    public void createBooking(Booking booking, String token, BookingCallback bookingCallback) {
        Call<ResponseBookingObject> call = service.createBooking(booking, token);

        call.enqueue(new Callback<ResponseBookingObject>() {
            @Override
            public void onResponse(Call<ResponseBookingObject> call, Response<ResponseBookingObject> response) {
                if (response.isSuccessful()) {
                    ResponseBookingObject responseBookingObject = response.body();
                    Booking bookingSaved = new Booking();
                    bookingSaved = responseBookingObject.getBooking();

                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    bookingCallback.onSuccess(bookingSaved, token);
                } else {
                    String message = "Ocurrio un error al registrar la reservacion";

                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            bookingCallback.onError(message, token);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    bookingCallback.onError(message, token);
                }
            }

            @Override
            public void onFailure(Call<ResponseBookingObject> call, Throwable t) {
                bookingCallback.onError(t.getMessage(), "");
            }
        });
    }
}
