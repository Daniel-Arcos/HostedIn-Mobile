package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.PasswordCodeCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.model.GenericSingleString;
import com.sdi.hostedin.data.model.NewPasswordRecovery;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemotePasswordCodeDataSource {

    ApiClient.Service service = ApiClient.getInstance().getService();



    public RemotePasswordCodeDataSource(){}

    public void sendEmailPasswordCode(String email, PasswordCodeCallback sendPasswordCodeCallback){
        Call<Void> call = service.createPasswordCode(new GenericSingleString(email));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    sendPasswordCodeCallback.onSucces("Email sent successfully");
                }
                else{
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            String message = jsonObject.get("message").getAsString();
                            sendPasswordCodeCallback.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sendPasswordCodeCallback.onError(t.getMessage());
            }
        });
    }

    public void verifyPasswordCode(String code, PasswordCodeCallback verifyPasswordCode){
        Call<Void> call =  service.createCodeToken(new GenericSingleString(code));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    String token = response.headers().get("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    verifyPasswordCode.onSucces(token);
                }
                else{
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            String message = jsonObject.get("message").getAsString();
                            verifyPasswordCode.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                verifyPasswordCode.onError(t.getMessage());
            }
        });
    }

    public void changePassword(String token, String newPassword, String email, PasswordCodeCallback newPassWordCallBack){
        Call<Void> call = service.updateUserPassword(token, new NewPasswordRecovery(newPassword, email));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    newPassWordCallBack.onSucces(response.message());
                }
                else{
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(errorString).getAsJsonObject();
                            String message = jsonObject.get("message").getAsString();
                            newPassWordCallBack.onError(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                newPassWordCallBack.onError(t.getMessage());
            }
        });
    }

}
