package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.callbacks.BookedAccommodationsCallBack;
import com.sdi.hostedin.data.callbacks.GuestBookedAccommodationCallBack;
import com.sdi.hostedin.data.callbacks.PasswordCodeCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.moshiconverters.MoshiConverter;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAccommodationObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookedAccommodation;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetAccommodationsObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGuestBookedAccommodations;
import com.sdi.hostedin.data.model.Accommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteAccommodationsDataSource {
    ApiClient.Service service = ApiClient.getInstance().getService();

    public RemoteAccommodationsDataSource() {

    }

    public void getAllAccommodations(String token, AccommodationsCallback accommodationsCallback) {
        Call<ResponseGetAccommodationsObject> call = service.getAllAccommodations(token);
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Accommodation> accommodations =
                            MoshiConverter.convertAPIAccommodationsResponseToJavaObjects(response.body());
                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }
                    accommodationsCallback.onSuccess(accommodations, token);
                } else {
                    String message = "Ocurrio un error al actualizar";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonObject jsonObject = new Gson().fromJson(errorString, JsonObject.class);
                            if (jsonObject.has("message")) {
                                message = jsonObject.get("message").getAsString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    accommodationsCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAccommodationsObject> call, Throwable t) {
                accommodationsCallback.onError(t.getMessage());
            }
        });
    }


    public void getAllAccommodationsExceptUserAccommodations(String idUser, String token, AccommodationsCallback accommodationsCallback) {
        Call<ResponseGetAccommodationsObject> call = service.getAllAccommodationsExceptUserAccommodations(idUser, token);
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Accommodation> accommodations =
                            MoshiConverter.convertAPIAccommodationsResponseToJavaObjects(response.body());
                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }
                    accommodationsCallback.onSuccess(accommodations, token);
                } else {
                    String message = "Ocurrio un error al actualizar";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonObject jsonObject = new Gson().fromJson(errorString, JsonObject.class);
                            if (jsonObject.has("message")) {
                                message = jsonObject.get("message").getAsString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    accommodationsCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAccommodationsObject> call, Throwable t) {
                accommodationsCallback.onError(t.getMessage());
            }
        });
    }



    public void getAllAccommodationsByLocationExceptUserAccommodations(String idUser, double lat, double lng, String token, AccommodationsCallback accommodationsCallback) {
        Call<ResponseGetAccommodationsObject> call = service.getAccommodationsByLocationExceptUserAccommodations(lat, lng, idUser, token);
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Accommodation> accommodations =
                            MoshiConverter.convertAPIAccommodationsResponseToJavaObjects(response.body());
                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }
                    accommodationsCallback.onSuccess(accommodations, token);
                } else {
                    String message = "Ocurrio un error al actualizar";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonObject jsonObject = new Gson().fromJson(errorString, JsonObject.class);
                            if (jsonObject.has("message")) {
                                message = jsonObject.get("message").getAsString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    accommodationsCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAccommodationsObject> call, Throwable t) {
                accommodationsCallback.onError(t.getMessage());
            }
        });
    }

    public void getALLHostAccommodationsWithAtLeastOneBooking(String userId,String token, BookedAccommodationsCallBack accommodationsCallback){
        Call<ResponseBookedAccommodation> call = service.getHostBookedAccommodations(token, userId, true);
        call.enqueue(new Callback<ResponseBookedAccommodation>() {
            @Override
            public void onResponse(Call<ResponseBookedAccommodation> call, Response<ResponseBookedAccommodation> response) {
                if (response.isSuccessful()){
                    ResponseBookedAccommodation responseGetAccommodationsObject = response.body();
                    accommodationsCallback.onSuccess(responseGetAccommodationsObject.getAccommodations(), response.message());
                }
                else{
                    String message = "Ocurrio un error al recuperar los alojamiento";

                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            accommodationsCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    accommodationsCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseBookedAccommodation> call, Throwable t) {
                accommodationsCallback.onError(t.getMessage());
            }
        });
    }

    public void getGuestBookedAccommodations(String userId, String bookingStatus, String token,  GuestBookedAccommodationCallBack accommodationsCallback){
        Call<ResponseGuestBookedAccommodations> call = service.getGuestBookings(token, userId, bookingStatus);
        call.enqueue(new Callback<ResponseGuestBookedAccommodations>() {
            @Override
            public void onResponse(Call<ResponseGuestBookedAccommodations> call, Response<ResponseGuestBookedAccommodations> response) {
                if (response.isSuccessful()){
                    ResponseGuestBookedAccommodations responseGetAccommodationsObject = response.body();
                    accommodationsCallback.onSuccess(responseGetAccommodationsObject.getAccommodationsBooked(), response.message());
                }
                else{
                    String message = "Ocurrio un error al recuperar los alojamiento";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            accommodationsCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    accommodationsCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseGuestBookedAccommodations> call, Throwable t) {
                accommodationsCallback.onError(t.getMessage());
            }
        });
    }
    
    public void createAccommodation(Accommodation accommodation, String token, AccommodationCallback accommodationCallback) {
        Call<ResponseAccommodationObject> call = service.createAccommodation(accommodation, token);

        call.enqueue(new Callback<ResponseAccommodationObject>() {
            @Override
            public void onResponse(Call<ResponseAccommodationObject> call, Response<ResponseAccommodationObject> response) {
                if (response.isSuccessful()) {
                    ResponseAccommodationObject responseAccommodationObject = response.body();
                    Accommodation accommodationSaved = new Accommodation();
                    accommodationSaved = responseAccommodationObject.getAccommodation();
                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    accommodationCallback.onSuccess(accommodationSaved, token);
                } else {
                    String message = "Ocurrio un error al actualizar";

                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            accommodationCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    accommodationCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseAccommodationObject> call, Throwable t) {
                accommodationCallback.onError(t.getMessage());
            }
        });
    }

    public void updateAccommodation(Accommodation accommodation, String token, AccommodationCallback accommodationCallback) {
        Call<ResponseAccommodationObject> call = service.updateAccommodation(token, accommodation.getId(), accommodation);

        call.enqueue(new Callback<ResponseAccommodationObject>() {
            @Override
            public void onResponse(Call<ResponseAccommodationObject> call, Response<ResponseAccommodationObject> response) {
                if (response.isSuccessful()) {
                    ResponseAccommodationObject responseAccommodationObject = response.body();
                    Accommodation accommodationSaved = responseAccommodationObject.getAccommodation();
                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    accommodationCallback.onSuccess(accommodationSaved, token);
                } else {
                    String message = "Ocurrio un error al actualizar";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            accommodationCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    accommodationCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseAccommodationObject> call, Throwable t) {
                accommodationCallback.onError(t.getMessage());
            }
        });
    }

    public void getAllHostOwnedAccommodations(String userId, String token,  AccommodationsCallback accommodationsCallback){
        Call<ResponseGetAccommodationsObject> call = service.getAllHostAccommodations(token, userId);
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()){
                    ResponseGetAccommodationsObject responseGetAccommodationsObject = response.body();
                    accommodationsCallback.onSuccess(responseGetAccommodationsObject.getAccommodations(), response.message());
                }else{
                    String message = "Ocurrio un error al recuperar los alojamientos";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            accommodationsCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    accommodationsCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAccommodationsObject> call, Throwable t) {
                accommodationsCallback.onError(t.getMessage());
            }
        });
    }

    public void deleteAccommodationById(String accommodationId, String token, PasswordCodeCallback passwordCodeCallback){
        Call<Void> call = service.deleteAccommodation(token,accommodationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    passwordCodeCallback.onSucces(response.message());
                }
                else{
                    String message = "Ocurrio un error al recuperar los alojamientos";
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            message = jsonObject.get("message").getAsString();
                            passwordCodeCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    passwordCodeCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                passwordCodeCallback.onError(t.getMessage());
            }
        });
    }

}
