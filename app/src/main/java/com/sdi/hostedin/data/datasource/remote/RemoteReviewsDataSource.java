package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdi.hostedin.data.callbacks.ReviewCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ReponseReviewObject;
import com.sdi.hostedin.data.model.Review;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteReviewsDataSource {
    ApiClient.Service service = ApiClient.getInstance().getService();

    public RemoteReviewsDataSource() {
    }

    public void saveNewReview(Review review, String token, ReviewCallback reviewCallback){
        Call<ReponseReviewObject> call = service.createReview(token, review);
        call.enqueue(new Callback<ReponseReviewObject>() {
            @Override
            public void onResponse(Call<ReponseReviewObject> call, Response<ReponseReviewObject> response) {
                if (response.isSuccessful()){
                    reviewCallback.onSuccess(response.body().getReview(), response.body().getMessage());
                }else{
                    String message = "Ocurrio un error al guardar la review";
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
                    reviewCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ReponseReviewObject> call, Throwable t) {
                reviewCallback.onError(t.getMessage());
            }

        });
    }
}
