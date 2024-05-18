package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAccommodationObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseEditAccountObject;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteAccommodationsDataSource {
    ApiClient.Service service = ApiClient.getInstance().getService();

    public RemoteAccommodationsDataSource() {

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
