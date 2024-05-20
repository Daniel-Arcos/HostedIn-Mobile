package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.moshiconverters.MoshiConverter;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAccommodationObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseEditAccountObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetAccommodationsObject;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.data.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteAccommodationsDataSource {
    ApiClient.Service service = ApiClient.getInstance().getService();

    public RemoteAccommodationsDataSource() {

    }

    public void getAllAccommodations(AccommodationsCallback accommodationsCallback) {
        Call<ResponseGetAccommodationsObject> call = service.getAllAccommodations();
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Accommodation> accommodations =
                            MoshiConverter.convertAPIAccommodationsResponseToJavaObjects(response.body());
                    accommodationsCallback.onSuccess(accommodations, "");
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


    public void getAllAccommodationsExceptUserAccommodations(String idUser,AccommodationsCallback accommodationsCallback) {
        Call<ResponseGetAccommodationsObject> call = service.getAllAccommodationsExceptUserAccommodations(idUser);
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Accommodation> accommodations =
                            MoshiConverter.convertAPIAccommodationsResponseToJavaObjects(response.body());
                    accommodationsCallback.onSuccess(accommodations, "");
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



    public void getAllAccommodationsByLocationExceptUserAccommodations(String idUser, double lat, double lng, AccommodationsCallback accommodationsCallback) {
        Call<ResponseGetAccommodationsObject> call = service.getAccommodationsByLocationExceptUserAccommodations(lat, lng, idUser);
        call.enqueue(new Callback<ResponseGetAccommodationsObject>() {
            @Override
            public void onResponse(Call<ResponseGetAccommodationsObject> call, Response<ResponseGetAccommodationsObject> response) {
                if (response.isSuccessful()) {
                    ArrayList<Accommodation> accommodations =
                            MoshiConverter.convertAPIAccommodationsResponseToJavaObjects(response.body());
                    accommodationsCallback.onSuccess(accommodations, "");
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

    public void createAccommodation(Accommodation accommodation, AccommodationCallback accommodationCallback) {
        Call<ResponseAccommodationObject> call = service.createAccommodation(accommodation);

        call.enqueue(new Callback<ResponseAccommodationObject>() {
            @Override
            public void onResponse(Call<ResponseAccommodationObject> call, Response<ResponseAccommodationObject> response) {
                if (response.isSuccessful()) {
                    ResponseAccommodationObject responseAccommodationObject = response.body();
                    Accommodation accommodationSaved = new Accommodation();
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


}
