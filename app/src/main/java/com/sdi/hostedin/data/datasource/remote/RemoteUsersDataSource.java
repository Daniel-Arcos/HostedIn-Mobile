package com.sdi.hostedin.data.datasource.remote;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAuthObject;
import com.sdi.hostedin.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteUsersDataSource {

    ApiClient.Service service = ApiClient.getInstance().getService();
    public interface AuthCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public RemoteUsersDataSource() {

    }

    public void createUserAccount(User user, AuthCallback authCallback){
        Call<ResponseAuthObject> call = service.signUp(user);
        call.enqueue(new Callback<ResponseAuthObject>() {
            @Override
            public void onResponse(Call<ResponseAuthObject> call, Response<ResponseAuthObject> response) {
                if (response.isSuccessful()) {
                    ResponseAuthObject responseAuthObject = response.body();
                    User userSaved = new User();
                    userSaved.setEmail(responseAuthObject.getUser().getEmail());
                    userSaved.setFullName(responseAuthObject.getUser().getFullName());
                    userSaved.setPhoneNumber(responseAuthObject.getUser().getPhoneNumber());
                    userSaved.setId(responseAuthObject.getUser().getPhoneNumber());
                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    authCallback.onSuccess(userSaved, token);
                } else {
                    String message = response != null ? response.body().getMessage() : "Ocurrio un error";
                    authCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseAuthObject> call, Throwable t) {
                authCallback.onError(t.getMessage());
            }
        });
    }

    public void login(User user, AuthCallback authCallback){
        Call<ResponseAuthObject> call = service.signIn(user);
        call.enqueue(new Callback<ResponseAuthObject>() {
            @Override
            public void onResponse(Call<ResponseAuthObject> call, Response<ResponseAuthObject> response) {
                if (response.isSuccessful()) {
                    ResponseAuthObject responseAuthObject = response.body();
                    User userLogged = new User();
                    userLogged.setEmail(responseAuthObject.getUser().getEmail());
                    userLogged.setFullName(responseAuthObject.getUser().getFullName());
                    userLogged.setPhoneNumber(responseAuthObject.getUser().getPhoneNumber());
                    userLogged.setId(responseAuthObject.getUser().getPhoneNumber());
                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    authCallback.onSuccess(userLogged, token);
                } else {
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            String message = jsonObject.get("message").getAsString();
                            authCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAuthObject> call, Throwable t) {
                authCallback.onError(t.getMessage());
            }
        });
    }
}
