package com.sdi.hostedin.data.datasource.remote;

import android.util.Log;

import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.model.GenericSingleString;
import com.sdi.hostedin.data.model.NewPasswordRecovery;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemotePasswordCodeDataSource {

    ApiClient.Service service = ApiClient.getInstance().getService();

    public interface PasswordCodeCallback {
        void onSucces(String message);
        void onError(String errorMessage);
    }

    public RemotePasswordCodeDataSource(){}

    public void sendEmailPasswordCode(String email, PasswordCodeCallback sendPasswordCodeCallback){
        Call<Void> call = service.sendEmailCode(new GenericSingleString(email));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    sendPasswordCodeCallback.onSucces("Email sent successfully");
                }
                else{
                    Log.e("log", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sendPasswordCodeCallback.onError(t.getMessage());
            }
        });
    }

    public void verifyPasswordCode(String code, PasswordCodeCallback verifyPasswordCode){
        Call<Void> call =  service.verifyEmailCode(new GenericSingleString(code));
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
                    Log.e("log", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                verifyPasswordCode.onError(t.getMessage());
            }
        });
    }

    public void changePasswordWithCode(String token, String newPassword,String email, PasswordCodeCallback newPassWordCallBack){
        Call<Void> call = service.changePasswordByCode(new GenericSingleString(token), new NewPasswordRecovery(newPassword, email));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                newPassWordCallBack.onSucces(response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                newPassWordCallBack.onError(t.getMessage());
            }
        });
    }

}
