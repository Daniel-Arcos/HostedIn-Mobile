package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
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
                    ResponseGetAccommodationsObject responseGetAccommodationsObject = response.body();
                    ArrayList<Accommodation> accommodation = new ArrayList<>();
                    for (Accommodation ac: responseGetAccommodationsObject.getAccommodations()) {
                        Accommodation accommodationRes = new Accommodation();
                        accommodationRes.setId(ac.getId());
                        accommodationRes.setTitle(ac.getTitle());
                        accommodationRes.setDescription(ac.getDescription());
                        accommodationRes.setRules(ac.getRules());
                        accommodationRes.setAccommodationType(ac.getAccommodationType());
                        accommodationRes.setNightPrice(ac.getNightPrice());
                        accommodationRes.setGuestsNumber(ac.getGuestsNumber());
                        accommodationRes.setRoomsNumber(ac.getRoomsNumber());
                        accommodationRes.setBedsNumber(ac.getBedsNumber());
                        accommodationRes.setBathroomsNumber(ac.getBathroomsNumber());
                        accommodationRes.setAccommodationServices(ac.getAccommodationServices());
                        accommodationRes.setUser(ac.getUser());
                        Location location = new Location();
                        location.set_id(ac.getLocation().get_id());
                        location.setLongitude(ac.getLocation().getCoordinates()[0]);
                        location.setLatitude(ac.getLocation().getCoordinates()[1]);
                        accommodationRes.setLocation(location);
                        accommodation.add(accommodationRes);
                    }
                    accommodationsCallback.onSuccess(accommodation, "");
                } else {
                    String message = "Ocurrio un error al actualizar";

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
