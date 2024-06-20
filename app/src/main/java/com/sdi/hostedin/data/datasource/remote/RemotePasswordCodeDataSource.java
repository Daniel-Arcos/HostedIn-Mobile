package com.sdi.hostedin.data.datasource.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sdi.hostedin.data.callbacks.PasswordCodeCallback;
import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.model.GenericSingleString;
import com.sdi.hostedin.data.model.NewPasswordRecovery;
import com.sdi.hostedin.utils.ErrorMessagesHandler;

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
                    String message = ErrorMessagesHandler.getErrorMessageSendingCode();
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
                    sendPasswordCodeCallback.onError(message);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sendPasswordCodeCallback.onError(ErrorMessagesHandler.getGenericErrorMessageConnection());
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
                    String message = ErrorMessagesHandler.getErrorVerifyingCode();
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
                    verifyPasswordCode.onError(message);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                verifyPasswordCode.onError(ErrorMessagesHandler.getGenericErrorMessageConnection());
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
                    String message = ErrorMessagesHandler.getErrorUpdatingPassword();
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
                    newPassWordCallBack.onError(message);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                newPassWordCallBack.onError(ErrorMessagesHandler.getGenericErrorMessageConnection());
            }
        });
    }

}
