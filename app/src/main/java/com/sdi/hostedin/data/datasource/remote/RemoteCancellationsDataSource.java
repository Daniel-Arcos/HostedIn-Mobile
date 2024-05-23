package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdi.hostedin.data.callbacks.CancellationCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.CancellationResponse;
import com.sdi.hostedin.data.model.Cancellation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteCancellationsDataSource {

    ApiClient.Service service = ApiClient.getInstance().getService();

    public void cancelBooking (Cancellation cancellation, String token, CancellationCallback cancellationCallback) {
        Call<CancellationResponse> call = service.cancelBooking(token, cancellation);

        call.enqueue(new Callback<CancellationResponse>() {
            @Override
            public void onResponse(Call<CancellationResponse> call, Response<CancellationResponse> response) {
                String token = "";
                String refreshToken = response.headers().get("Set-Authorization");
                if (refreshToken != null) {
                    token = refreshToken;
                }
                if (response.isSuccessful()) {
                    cancellationCallback.onSuccess(response.body().getCancellation(), token);
                } else {
                    String message = "Ocurrio un error al cancelar la reservacion";
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
                    cancellationCallback.onError(message, token);
                }
            }

            @Override
            public void onFailure(Call<CancellationResponse> call, Throwable t) {
                cancellationCallback.onError(t.getMessage(), "");
            }
        });
    }
}
