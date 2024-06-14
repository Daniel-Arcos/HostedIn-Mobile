package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.ReviewCallback;
import com.sdi.hostedin.data.callbacks.ReviewsCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetReviewsObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseReviewObject;
import com.sdi.hostedin.data.model.Review;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteReviewsDataSource {
    ApiClient.Service service = ApiClient.getInstance().getService();

    public RemoteReviewsDataSource() {
    }

    public void saveNewReview(Review review, String token, ReviewCallback reviewCallback){
        Call<ResponseReviewObject> call = service.createReview(token, review);
        call.enqueue(new Callback<ResponseReviewObject>() {
            @Override
            public void onResponse(Call<ResponseReviewObject> call, Response<ResponseReviewObject> response) {
                if (response.isSuccessful()){
                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }
                    reviewCallback.onSuccess(response.body().getReview(), token);
                } else {
                    String message = "Ocurrio un error al guardar la review";

                    String token = "";
                    String refreshToken = response.headers().get("Set-Authorization");
                    if (refreshToken != null) {
                        token = refreshToken;
                    }

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
                    reviewCallback.onError(message, token);
                }
            }

            @Override
            public void onFailure(Call<ResponseReviewObject> call, Throwable t) {
                reviewCallback.onError(t.getMessage(), "");
            }

        });
    }

    public void getReviewsOfAccommodation(String accommodationId, String token, ReviewsCallback reviewsCallback) {
        Call<ResponseGetReviewsObject> call = service.getReviewsOfAccommodation(token, accommodationId);

        call.enqueue(new Callback<ResponseGetReviewsObject>() {
            @Override
            public void onResponse(Call<ResponseGetReviewsObject> call, Response<ResponseGetReviewsObject> response) {
                if (response.isSuccessful()) {
                    ResponseGetReviewsObject responseGetReviewsObject = response.body();

                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    reviewsCallback.onSuccess(responseGetReviewsObject.getReviews(), token);
                } else {
                    String message = "Ocurrio un error al recuperar las reseñas";

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
                            reviewsCallback.onError(message, token);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    reviewsCallback.onError(message, token);
                }
            }

            @Override
            public void onFailure(Call<ResponseGetReviewsObject> call, Throwable t) {
                reviewsCallback.onError(t.getMessage(), "");
            }
        });
    }
}
